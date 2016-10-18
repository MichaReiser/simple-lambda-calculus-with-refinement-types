// Generated from /Users/micha/OneDrive/Hsr/Master/Seminar 2/simple-lambda-calculus/src/main/resources/LambdaCalculus.g4 by ANTLR 4.5.3
package ch.famreiser.hsr.seminar.generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LambdaCalculusParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LambdaCalculusVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LambdaCalculusParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(LambdaCalculusParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryExpression}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpression(LambdaCalculusParser.BinaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code letIn}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetIn(LambdaCalculusParser.LetInContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constant}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(LambdaCalculusParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code application}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApplication(LambdaCalculusParser.ApplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifThenElse}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfThenElse(LambdaCalculusParser.IfThenElseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code abstraction}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstraction(LambdaCalculusParser.AbstractionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variable}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(LambdaCalculusParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code brackets}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBrackets(LambdaCalculusParser.BracketsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LambdaCalculusParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(LambdaCalculusParser.BodyContext ctx);
}