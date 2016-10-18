package ch.famreiser.hsr.seminar.interpreter;

import ch.famreiser.hsr.seminar.generated.LambdaCalculusBaseVisitor;
import ch.famreiser.hsr.seminar.generated.LambdaCalculusParser;

/**
 * Visitor for the simply typed lambda calculus... does only add runtime checks not guaranteed by a correctly typeset program.
 */
public class InterpreterVisitor extends LambdaCalculusBaseVisitor<Object> {

    private final Scope scope;

    public InterpreterVisitor() {
        this(new Scope());
    }

    private InterpreterVisitor(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Object visitAbstraction(LambdaCalculusParser.AbstractionContext ctx) {
        return new Function(ctx.Identifier().getText(), ctx.expression(), this.scope);
    }

    @Override
    public Object visitApplication(LambdaCalculusParser.ApplicationContext ctx) {
        Function callee = (Function) ctx.expression(0).accept(this);
        Object argument = ctx.expression(1).accept(this);

        Scope childScope = callee.getClosure().set(callee.getArgumentName(), argument);
        return callee.getBody().accept(new InterpreterVisitor(childScope));
    }

    @Override
    public Object visitBinaryExpression(LambdaCalculusParser.BinaryExpressionContext ctx) {
        Object left = ctx.expression(0).accept(this);
        Object right = ctx.expression(1).accept(this);
        String operator = ctx.Operator().getText();

        switch (operator) {
            case "+":
                return (int) left + (int) right;
            case "-":
                return (int) left - (int) right;
            case "*":
                return (int) left * (int) right;
            case "/":
                return (int) left / (int) right;
            case "<":
                return (int) left < (int) right;
            case ">":
                return (int) left > (int) right;
            case "<=":
                return (int) left <= (int) right;
            case ">=":
                return (int) left >= (int) right;
            case "==":
                return left.equals(right);
            default:
                throw new UnsupportedOperationException("Illegal Operator " + operator);
        }
    }

    @Override
    public Object visitConstant(LambdaCalculusParser.ConstantContext ctx) {
        if (ctx.Int() != null) {
            return Integer.parseInt(ctx.Int().getText());
        }
        return Boolean.parseBoolean(ctx.Bool().getText());
    }

    @Override
    public Object visitIfThenElse(LambdaCalculusParser.IfThenElseContext ctx) {
        boolean condition = (boolean) ctx.expression().accept(this);

        if (condition) {
            return ctx.body(0).accept(this);
        }

        return ctx.body(1).accept(this);
    }

    @Override
    public Object visitLetIn(LambdaCalculusParser.LetInContext ctx) {
        Object variable = ctx.expression().accept(this);
        Scope letScope = scope.set(ctx.Identifier().getText(), variable);

        return ctx.body().accept(new InterpreterVisitor(letScope));
    }

    @Override
    public Object visitVariable(LambdaCalculusParser.VariableContext ctx) {
        return this.scope.get(ctx.Identifier().getText());
    }

    @Override
    public Object visitBrackets(LambdaCalculusParser.BracketsContext ctx) {
        return ctx.expression().accept(this);
    }
}
