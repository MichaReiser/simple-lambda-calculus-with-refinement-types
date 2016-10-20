package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.typechecker.Symbol;
import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.*;
import ch.famreiser.hsr.seminar.typechecker.types.AbstractionType;
import ch.famreiser.hsr.seminar.typechecker.types.Shape;
import ch.famreiser.hsr.seminar.typechecker.types.TypeError;
import ch.famreiser.hsr.seminar.typechecker.types.TypeVariable;
import com.google.common.base.Throwables;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.CharStreams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Wrapper for the fixpoint binary
 */
public class LiquidFixPointSolverInstance {

    private static final Pattern SOLUTION_PATTERN = Pattern.compile("Saving Solution:\\s(.*\\.fq\\.fqout)");

    private static String getFixPointSolverPathFromEnv() {
        String path = System.getenv("FIXPOINT_SOLVER");
        if (path == null) {
            throw new IllegalArgumentException("The path to the fixpoint solver needs to be defined as 'FIXPOINT_SOLVER' environment variable.");
        }

        return path;
    }

    private static Path tempDirectory;
    private final String fixPointSolverPath;
    private final StringBuilder output = new StringBuilder();
    private final Map<Symbol, Integer> variableIds = new HashMap<>();
    private final BiMap<Symbol, String> uniqueSymbolNames = HashBiMap.create();
    private final List<Constraint> constraints = new LinkedList<>();
    private int nextVariableId = 0;

    public LiquidFixPointSolverInstance() {
        this(getFixPointSolverPathFromEnv());
    }

    public LiquidFixPointSolverInstance(String fixPointSolverPath) {
        this.fixPointSolverPath = fixPointSolverPath;
    }

    public void addBinding(Symbol variable, Template type) {
        int variableId = getVariableId(variable);

        output.append(String.format("bind %d %s : {v: %s | %s }\n\n",
                variableId,
                symbolToString(variable),
                shapeToString(type.getShape()),
                constraintToString(type.getPredicate())
        ));
    }

    public void addSubtypingConstraint(SubtypeConstraint constraint) {
        output.append(String.format("constraint:\n" +
                        "  env %s\n" +
                        "  lhs %s\n" +
                        "  rhs %s\n" +
                        "  id %d tag []\n\n",
                environmentToString(constraint.getEnvironment()),
                templateToString(constraint.getLeft()),
                templateToString(constraint.getRight()),
                constraints.size()));
        constraints.add(constraint);
    }

    public void addWellFormednessConstraint(WellFormednessConstraint constraint) {
        output.append(String.format("wf:\n" +
                "  env %s\n" +
                "  reft %s\n\n",
                environmentToString(constraint.getEnvironment()),
                templateToString(constraint.getTemplate())
        ));
    }

    public void solve() {
        try {
            Path fqFile = writeFqFile(createOutputDirectory());
            String programOutput = runFixPointSolver(fqFile);
            String solution = getSolution(programOutput);
            System.out.println(solution);
        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
    }

    private int getVariableId(Symbol symbol) {
        Integer id = variableIds.get(symbol);
        if (id == null) {
            id = nextVariableId++;
            variableIds.put(symbol, id);
        }

        return id;
    }

    private String symbolToString(Symbol symbol) {
        if (symbol.equals(Symbol.nu())) {
            return "v";
        }

        String postFix = "";
        Integer nextIndex = 0;
        while (uniqueSymbolNames.inverse().containsKey(symbol.getName() + postFix)) {
            postFix = "$" + nextIndex++;
        }

        return symbol.getName() + postFix;
    }

    private String environmentToString(TemplateEnvironment environment) {
        String variableIds = StreamSupport
                .stream(environment.getSymbols().spliterator(), false)
                .map(variable -> getVariableId(variable) + "")
                .sorted()
                .collect(Collectors.joining("; "));
        return "[" + variableIds + "]";
    }

    private String templateToString(Template template) {
        return String.format("{v : %s | %s}",
                shapeToString(template.getShape()),
                constraintToString(template.getPredicate()));
    }

    private String constraintToString(Constraint constraint) {
        if (constraint instanceof BinaryConstraint && !((BinaryConstraint) constraint).getOperator().isBoolExpr()) {
            constraint = new AssignmentConstraint(LiquidTypeVariable.nu(), constraint);
        }

        return constraint.accept(new ConstraintTranslator());
    }

    private String shapeToString(Shape shape) {
        if (shape instanceof AbstractionType) {
            return String.format("(func(1, [(%s %s)]))",
                    shapeToString(((AbstractionType) shape).getArgument()),
                    shapeToString(((AbstractionType) shape).getReturnType())
                    );
        }
        if (shape instanceof TypeVariable) {
            return String.format("@(%d)", ((TypeVariable) shape).getIndex());
        }
        return shape.toString();
    }

    private final Path createOutputDirectory() throws IOException {
        if (tempDirectory == null) {
            tempDirectory = Files.createTempDirectory(".liquid");
        }
        return tempDirectory;
    }

    private final Path writeFqFile(Path directory) throws IOException {
        Path outputPath = Files.createTempFile(directory, "output", ".fq");;
        try (FileOutputStream output = new FileOutputStream(outputPath.toFile())) {
            try (OutputStreamWriter writer = new OutputStreamWriter(output)) {
                String fqContent = this.output.toString();
                System.out.println(fqContent);
                writer.write(fqContent);
            }
        }

        return outputPath;
    }

    private final String runFixPointSolver(Path fqFilePath) throws IOException {
        Process fixPointProcess = new ProcessBuilder(fixPointSolverPath, fqFilePath.toAbsolutePath().toString(), "--save").redirectErrorStream(true).start();

        try {
            int result = fixPointProcess.waitFor();
            try (InputStreamReader programOutput = new InputStreamReader(fixPointProcess.getInputStream())) {
                String output = CharStreams.toString(new InputStreamReader(fixPointProcess.getInputStream()));
                if (result == 1) {
                    throw new TypeError("Constraint solving failed for reason:\n" + output);
                }
                return output;
            }
        } catch (InterruptedException ex) {
            throw Throwables.propagate(ex);
        }
    }

    private final String getSolution(String programOutput) throws IOException {
        Matcher matcher = SOLUTION_PATTERN.matcher(programOutput);
        if (matcher.find()) {
            String solutionPath = matcher.group(1);
            String solution = CharStreams.toString(new InputStreamReader(new FileInputStream(solutionPath)));

            return solution;
        }

        throw new IllegalStateException("Failed to process the output of liquid fixpoint");
    }

    private class ConstraintTranslator implements ConstraintVisitor<String> {
        @Override
        public String visit(BinaryConstraint binaryConstraint) {
            return binaryConstraint.getLeft().accept(this) + " " + binaryConstraint.getOperator().toString() + " " + binaryConstraint.getRight().accept(this);
        }

        @Override
        public String visit(AssignmentConstraint assignmentConstraint) {
            return assignmentConstraint.getVariable().accept(this) + " = " + assignmentConstraint.getValue().accept(this);
        }

        @Override
        public String visit(BoolConstraint boolConstraint) {
            return boolConstraint.isFulfilled() ? "true" : "false";
        }

        @Override
        public String visit(LiquidTypeVariable liquidTypeVariable) {
            if (liquidTypeVariable.isNu()) {
                return "v";
            }

            char[] name = liquidTypeVariable.getSymbol().getName().toCharArray();
            if (name[0] == '\u03BA') {
                // kappa, the following symbols define the index. The number 0 is mapped to 8320
                int index = 0;
                for (int i = 1; i < name.length; ++i) {
                    char c = name[i];
                    index += c - 8320 * Math.pow(10, i -1);
                }

                return String.format("$k%d", index);
            }

            return new String(name);
        }

        @Override
        public String visit(NotConstraint notConstraint) {
            return "not " + notConstraint.getInner().accept(this);
        }

        @Override
        public String visit(NumberConstraint numberConstraint) {
            return numberConstraint.getValue() + "";
        }

        @Override
        public String visit(SubtypeConstraint subtypeConstraint) {
            throw new IllegalStateException("SubtypeConstraint should never be a nested constraint");
        }

        @Override
        public String visit(WellFormednessConstraint wellFormednessConstraint) {
            throw new IllegalStateException("Well Formedness constraint should never be a nested constraint");
        }
    }
}
