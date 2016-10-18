package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.generated.LambdaCalculusBaseVisitor;
import ch.famreiser.hsr.seminar.generated.LambdaCalculusParser;
import ch.famreiser.hsr.seminar.typechecker.hm.HindleyMilnerTypeInference;
import ch.famreiser.hsr.seminar.typechecker.liquid.solver.LiquidSolver;
import ch.famreiser.hsr.seminar.typechecker.liquid.solver.SolverFactory;
import ch.famreiser.hsr.seminar.typechecker.types.AbstractionType;
import ch.famreiser.hsr.seminar.typechecker.types.BoolType;
import ch.famreiser.hsr.seminar.typechecker.types.NumberType;
import ch.famreiser.hsr.seminar.typechecker.types.Shape;
import com.google.common.base.MoreObjects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;

public class LiquidTypeChecker {

    public Template infer(ParserRuleContext expression) {
        return infer(TemplateEnvironment.empty(), expression);
    }

    public Template infer(TemplateEnvironment environment, ParserRuleContext expression) {
        LiquidInferenceResult result = expression.accept(new LiquidTypeCheckerVisitor(environment));


        System.out.println(result);
        Constraints splitConstraints = splitConstraints(result.constraints);
        Solve(instantiate(environment, result.template), splitConstraints);
        return result.template;
    }

    private Constraints splitConstraints(Constraints constraints) {
        Set<Constraint> result = new HashSet<>();

        for (Constraint constraint : constraints) {
            Iterables.addAll(result, constraint.split());
        }

        return Constraints.of(result);
    }

    private void Solve(Map<LiquidTypeVariable, Set<Constraint>> assignments, Constraints constraints) {
        LiquidSolver solver = new SolverFactory().create();

        System.out.println("Constraints " + constraints);

        for (Constraint constraint : constraints) {
            FluentIterable.from(constraint.getLiquidVariables())
                .filter(variable -> !variable.equals(LiquidTypeVariable.nu()))
                .forEach(variable -> {
                    Iterator<Constraint> variableConstraintsIterator = assignments.get(variable).iterator();
                    while (variableConstraintsIterator.hasNext()) {
                        Constraint variableConstraint = variableConstraintsIterator.next();
                        if (!solver.satisfies(variableConstraint, constraint)) {
                            variableConstraintsIterator.remove();
                        }
                    }
            });
        }

        solver.requestShutdown();

        System.out.println(assignments);
    }

    private Map<LiquidTypeVariable, Set<Constraint>> instantiate(TemplateEnvironment environment, Template template) {
        Map<LiquidTypeVariable, Set<Constraint>> assignments = new HashMap<>();
        Iterable<LiquidTypeVariable> freeVariables = Sets.newHashSet(Iterables.filter(template.getPredicate().getLiquidVariables(), variable -> !variable.isNu()));

        for (LiquidTypeVariable variable: freeVariables) {
            Set<Constraint> constraints = Sets.newHashSet();
            constraints.add(new BinaryConstraint(new NumberConstraint(0), BinaryConstraint.Operation.LESS_OR_EQUAL_THAN, LiquidTypeVariable.nu()));

            for (LiquidTypeVariable otherVariable: freeVariables) {
                constraints.add(new BinaryConstraint(LiquidTypeVariable.nu(), BinaryConstraint.Operation.LESS_THAN, otherVariable));
                constraints.add(new BinaryConstraint(otherVariable, BinaryConstraint.Operation.LESS_OR_EQUAL_THAN, LiquidTypeVariable.nu()));
            }

            assignments.put(variable, constraints);
        }

        System.out.println(assignments);

        return assignments;
    }

    private static class LiquidTypeCheckerVisitor extends LambdaCalculusBaseVisitor<LiquidInferenceResult> {

        private final HindleyMilnerTypeInference hindleyMilnerTypeInference = new HindleyMilnerTypeInference();
        private final TemplateEnvironment typeEnvironment;

        public LiquidTypeCheckerVisitor() {
            this(TemplateEnvironment.empty());
        }

        private LiquidTypeCheckerVisitor(TemplateEnvironment typeEnvironment) {
            this.typeEnvironment = typeEnvironment;
        }

        @Override
        public LiquidInferenceResult visitVariable(LambdaCalculusParser.VariableContext ctx) {
            hm(ctx); // ensures that the variable is defined
            return new LiquidInferenceResult(typeEnvironment.getType(ctx.Identifier().getText()), Constraints.empty());
        }

        @Override
        public LiquidInferenceResult visitConstant(LambdaCalculusParser.ConstantContext ctx) {
            Shape type = hm(ctx);

            Constraint constraint;

            if (type instanceof BoolType) {
                constraint = new BoolConstraint(Boolean.parseBoolean(ctx.Bool().getText()));
            } else if (type instanceof NumberType) {
                constraint = new NumberConstraint(Integer.parseInt(ctx.Int().getText()));
            } else {
                throw new UnsupportedOperationException("Unsupported base type " + type + " for constant.");
            }

            constraint = new BinaryConstraint(LiquidTypeVariable.nu(), BinaryConstraint.Operation.EQUAL, constraint);

            return new LiquidInferenceResult(new TemplateType(LiquidTypeVariable.nu(), type, constraint));
        }

        @Override
        public LiquidInferenceResult visitApplication(LambdaCalculusParser.ApplicationContext ctx) {
            // x:F_x -> F
            LiquidInferenceResult abstraction = ctx.expression(0).accept(this);
            LiquidInferenceResult argument = ctx.expression(1).accept(this);

            return application(abstraction, argument);
        }

        private LiquidInferenceResult application(LiquidInferenceResult abstraction, LiquidInferenceResult argument) {
            AbstractionTemplate calleeType = (AbstractionTemplate) abstraction.template;
            // Template type = calleeType.getReturnType().substitute(calleeType.getArgument().getVariable(), calleeType.getVariable());  // TODO Substitute correctly

            Constraints constraints = abstraction.constraints
                    .union(argument.constraints)
                    .add(new SubtypeConstraint(typeEnvironment, argument.template, calleeType.getArgument()));

            return new LiquidInferenceResult(calleeType, constraints);
        }

        @Override
        public LiquidInferenceResult visitAbstraction(LambdaCalculusParser.AbstractionContext ctx) {
            AbstractionTemplate type = fresh((AbstractionType) hm(ctx), ctx.Identifier().getText());

            TemplateEnvironment bodyEnvironment = typeEnvironment.setType(ctx.Identifier().getText(), type.getArgument());
            LiquidInferenceResult inferredBody = ctx.expression().accept(new LiquidTypeCheckerVisitor(bodyEnvironment));

            Constraints constraints = inferredBody.constraints
                    .add(new WellFormednessConstraint(bodyEnvironment, type))
                    .add(new SubtypeConstraint(bodyEnvironment, inferredBody.template, type.getReturnType()));

            return new LiquidInferenceResult(type, constraints);
        }

        @Override
        public LiquidInferenceResult visitIfThenElse(LambdaCalculusParser.IfThenElseContext ctx) {
            super.visitChildren(ctx);
            Shape type = hm(ctx);
            return new LiquidInferenceResult(new TemplateType(LiquidTypeVariable.nu(), type, BoolConstraint.alwaysTrue()), Constraints.empty());
        }

        @Override
        public LiquidInferenceResult visitLetIn(LambdaCalculusParser.LetInContext ctx) {
            Template returnType = fresh(hm(ctx));

            LiquidInferenceResult variable = ctx.expression().accept(this);
            TemplateEnvironment bodyEnvironment = typeEnvironment.setType(ctx.Identifier().getText(), variable.template);
            LiquidInferenceResult body = ctx.body().accept(new LiquidTypeCheckerVisitor(bodyEnvironment));

            Constraints constraints = variable.constraints
                    .union(body.constraints)
                    .add(new WellFormednessConstraint(typeEnvironment, returnType))
                    .add(new SubtypeConstraint(bodyEnvironment, body.template, returnType));
            return new LiquidInferenceResult(returnType, constraints);
        }

        @Override
        public LiquidInferenceResult visitBinaryExpression(LambdaCalculusParser.BinaryExpressionContext ctx) {
            LiquidInferenceResult left = ctx.expression(0).accept(this); // HM now happens multiple times..
            LiquidInferenceResult right = ctx.expression(1).accept(this);
            Shape returnShape = hm(ctx);

            LiquidTypeVariable leftVariable = LiquidTypeVariable.named("left");
            LiquidTypeVariable rightVariable = LiquidTypeVariable.named("right");

            AbstractionTemplate resultType = AbstractionTemplate.create(
                    new TemplateType(rightVariable, left.template.getShape(), BoolConstraint.alwaysTrue()),
                    new TemplateType(LiquidTypeVariable.nu(), returnShape, new BinaryConstraint(leftVariable, BinaryConstraint.Operation.fromString(ctx.Operator().getText()), rightVariable)),
                    BoolConstraint.alwaysTrue()
            );

            AbstractionTemplate first = AbstractionTemplate.create(
                    new TemplateType(leftVariable, right.template.getShape(), BoolConstraint.alwaysTrue()),
                    resultType,
                    BoolConstraint.alwaysTrue());

            // do we have to apply the abstraction rule too?

            return application(application(new LiquidInferenceResult(first), left), right);
        }

        private Shape hm(ParserRuleContext node) {
            return hindleyMilnerTypeInference.infer(new ShapeTypeEnvironment(typeEnvironment), node);
        }

        private Template fresh(Shape shape) {
            return fresh(shape, LiquidTypeVariable.kappa());
        }

        private Template fresh(Shape shape, LiquidTypeVariable variable) {
            return new TemplateType(LiquidTypeVariable.nu(), shape.resolved(), new BinaryConstraint(LiquidTypeVariable.nu(), BinaryConstraint.Operation.EQUAL, variable));
        }

        private AbstractionTemplate fresh(AbstractionType shape, String argumentName) {
            return AbstractionTemplate.create(fresh(shape.getArgument(), LiquidTypeVariable.named(argumentName)), fresh(shape.getReturnType()), LiquidTypeVariable.kappa());
        }

        @Override
        public LiquidInferenceResult visitBrackets(LambdaCalculusParser.BracketsContext ctx) {
            return ctx.expression().accept(this);
        }
    }

    private static class LiquidInferenceResult {
        public final Template template;
        public final Constraints constraints;

        public LiquidInferenceResult(Template template) {
            this(template, Constraints.empty());
        }

        public LiquidInferenceResult(Template template, Constraints constraints) {
            this.template = template;
            this.constraints = constraints;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("template", template).add("constraints", constraints).toString();
        }
    }
}


