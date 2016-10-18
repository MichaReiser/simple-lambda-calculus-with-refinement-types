package ch.famreiser.hsr.seminar;

import ch.famreiser.hsr.seminar.generated.LambdaCalculusLexer;
import ch.famreiser.hsr.seminar.generated.LambdaCalculusParser;
import ch.famreiser.hsr.seminar.interpreter.InterpreterVisitor;
import ch.famreiser.hsr.seminar.typechecker.hm.HindleyMilnerTypeInference;
import ch.famreiser.hsr.seminar.typechecker.liquid.LiquidTypeChecker;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class Program {
    public static void main(String... args) throws IOException {
        CharStream input = new ANTLRInputStream(args[0]);
        LambdaCalculusLexer lexer = new LambdaCalculusLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        LambdaCalculusParser parser = new LambdaCalculusParser(tokenStream);

        System.out.println("Hindley Milner Checking Program '" + args[0] + "',");
        System.out.println("Inferred Type for program is: " + new HindleyMilnerTypeInference().infer(parser.program()));

        parser.reset();

        System.out.println();
        System.out.println("Liquid Type Checking Program");
        System.out.println("Inferred Type for program is " + new LiquidTypeChecker().infer(parser.program()));
        System.out.println();
        parser.reset();

        System.out.println("Program compiled!, Let's run it");
        Object result = parser.program().accept(new InterpreterVisitor());

        System.out.println("Result: " + result);
    }
}
