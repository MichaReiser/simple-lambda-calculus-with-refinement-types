// Generated from /Users/micha/OneDrive/Hsr/Master/Seminar 2/simple-lambda-calculus/src/main/resources/LambdaCalculus.g4 by ANTLR 4.5.3
package ch.famreiser.hsr.seminar.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LambdaCalculusParser}.
 */
public interface LambdaCalculusListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LambdaCalculusParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(LambdaCalculusParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LambdaCalculusParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(LambdaCalculusParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpression}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpression(LambdaCalculusParser.BinaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpression}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpression(LambdaCalculusParser.BinaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code letIn}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLetIn(LambdaCalculusParser.LetInContext ctx);
	/**
	 * Exit a parse tree produced by the {@code letIn}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLetIn(LambdaCalculusParser.LetInContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constant}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterConstant(LambdaCalculusParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constant}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitConstant(LambdaCalculusParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code application}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterApplication(LambdaCalculusParser.ApplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code application}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitApplication(LambdaCalculusParser.ApplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifThenElse}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIfThenElse(LambdaCalculusParser.IfThenElseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifThenElse}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIfThenElse(LambdaCalculusParser.IfThenElseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code abstraction}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAbstraction(LambdaCalculusParser.AbstractionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code abstraction}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAbstraction(LambdaCalculusParser.AbstractionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variable}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVariable(LambdaCalculusParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variable}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVariable(LambdaCalculusParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code brackets}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBrackets(LambdaCalculusParser.BracketsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code brackets}
	 * labeled alternative in {@link LambdaCalculusParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBrackets(LambdaCalculusParser.BracketsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LambdaCalculusParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(LambdaCalculusParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LambdaCalculusParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(LambdaCalculusParser.BodyContext ctx);
}