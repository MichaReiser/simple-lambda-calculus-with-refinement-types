package ch.famreiser.hsr.seminar.typechecker.hm;

import ch.famreiser.hsr.seminar.generated.LambdaCalculusBaseVisitor;
import ch.famreiser.hsr.seminar.generated.LambdaCalculusParser;
import ch.famreiser.hsr.seminar.typechecker.LexicalScope;
import ch.famreiser.hsr.seminar.typechecker.Symbol;
import ch.famreiser.hsr.seminar.typechecker.types.*;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Implementation of the Hindley Milner Type Inference algorithm
 */
public class HindleyMilnerTypeInference {

    public Shape infer(ParserRuleContext expression) throws TypeError {
        return infer(TypeEnvironment.empty(), LexicalScope.empty(), expression);
    }

    public Shape infer(TypeEnvironment typeEnvironment, LexicalScope lexicalScope, ParserRuleContext expression) throws TypeError {
        Shape result = expression.accept(new HindleyMilnerVisitor(typeEnvironment, lexicalScope));
        System.out.println(typeEnvironment);
        return result;
    }

    public Shape unify(Shape a, Shape b) throws TypeError {
        Shape resolvedA = a.resolved();
        Shape resolvedB = b.resolved();

        if (resolvedA == resolvedB) {
            return resolvedB;
        }

        if (resolvedA instanceof TypeVariable) {
            if (resolvedA.contains(resolvedB)) {
                throw new TypeError("The type " + resolvedB + " occurres in the type " + resolvedA + ".");
            }

            ((TypeVariable) resolvedA).resolve(resolvedB);
            return resolvedB;
        } else if (resolvedB instanceof TypeVariable) {
            return unify(resolvedB, resolvedA);
        }

        if (a instanceof AbstractionType && b instanceof AbstractionType) {
            AbstractionType aAbstraction = (AbstractionType) a;
            AbstractionType bAbstraction = (AbstractionType) b;

            return AbstractionType.create(unify(aAbstraction.getArgument(), bAbstraction.getArgument()), unify(aAbstraction.getReturnType(), bAbstraction.getReturnType()));
        }

        throw new TypeError("The type " + a + " and " + b + " cannot be unified, no unification therefore exists");
    }

    private class HindleyMilnerVisitor extends LambdaCalculusBaseVisitor<Shape> {
        private final TypeEnvironment typeEnvironment;
        private final LexicalScope lexicalScope;

        HindleyMilnerVisitor(TypeEnvironment typeEnvironment, LexicalScope lexicalScope) {
            this.typeEnvironment = typeEnvironment;
            this.lexicalScope = lexicalScope;
        }

        @Override
        public Shape visitConstant(LambdaCalculusParser.ConstantContext ctx) {
            if (ctx.Bool() != null) {
                return BoolType.create();
            }
            return IntType.create();
        }

        @Override
        public Shape visitVariable(LambdaCalculusParser.VariableContext ctx) {
            Symbol symbol = this.lexicalScope.get(ctx.Identifier().getText());

            if (symbol == null) {
                throw new TypeError("The variable " + ctx.Identifier().getText() + " is not defined in the current scope");
            }

            Shape type = this.typeEnvironment.getType(symbol);

            if (type == null) {
                throw new TypeError("Type for variable '" + ctx.Identifier().getText() + "' is unknown.");
            }

            return type;
        }

        @Override
        public Shape visitLetIn(LambdaCalculusParser.LetInContext ctx) {
            Shape variableType = ctx.expression().accept(this);

            LexicalScope bodyScope = lexicalScope.createChildScope();
            Symbol variable = bodyScope.add(ctx.Identifier().getText());
            typeEnvironment.setType(variable, variableType);

            return ctx.body().accept(new HindleyMilnerVisitor(typeEnvironment, bodyScope));
        }

        @Override
        public Shape visitAbstraction(LambdaCalculusParser.AbstractionContext ctx) {
            Shape returnType = TypeVariable.create();
            Shape argumentType = TypeVariable.create();

            LexicalScope bodyScope = lexicalScope.createChildScope();
            Symbol argument = bodyScope.add(ctx.Identifier().getText());
            typeEnvironment.setType(argument, argumentType);
            Shape inferredReturnType = ctx.expression().accept(new HindleyMilnerVisitor(typeEnvironment, bodyScope));

            return AbstractionType.create(argumentType, unify(returnType, inferredReturnType));
        }

        @Override
        public Shape visitApplication(LambdaCalculusParser.ApplicationContext ctx) {
            Shape callee = ctx.expression(0).accept(this);

            if (!(callee instanceof AbstractionType)) {
                throw new TypeError("Tried to invoke function on " + callee);
            }

            AbstractionType abstraction = (AbstractionType) callee.fresh();
            Shape argumentType = ctx.expression(1).accept(this);

            unify(abstraction.getArgument(), argumentType);

            return abstraction.getReturnType();
        }

        @Override
        public Shape visitBinaryExpression(LambdaCalculusParser.BinaryExpressionContext ctx) {
            String operator = ctx.Operator().getText();

            switch (operator) {
                case "+":
                case "-":
                case "*":
                case "/":
                    unify(ctx.expression(0).accept(this), IntType.create());
                    unify(ctx.expression(1).accept(this), IntType.create());
                    return IntType.create();
                case "<":
                case "<=":
                case ">":
                case ">=":
                    unify(ctx.expression(0).accept(this), IntType.create());
                    unify(ctx.expression(1).accept(this), IntType.create());
                    return BoolType.create();
                case "==": {
                    // Maybe we should not allow comparing abstraction but whatever.
                    unify(ctx.expression(0).accept(this), ctx.expression(1).accept(this));
                    return BoolType.create();
                }

                default:
                    throw new TypeError("Unknown operator " + operator);
            }
        }

        @Override
        public Shape visitIfThenElse(LambdaCalculusParser.IfThenElseContext ctx) {
            unify(ctx.expression().accept(this), BoolType.create());

            Shape ifType = ctx.body(0).accept(this);
            Shape elseType = ctx.body(1).accept(this);
            return unify(ifType, elseType);
        }

        @Override
        public Shape visitBrackets(LambdaCalculusParser.BracketsContext ctx) {
            return ctx.expression().accept(this);
        }

        @Override
        public Shape visitBody(LambdaCalculusParser.BodyContext ctx) {
            return ctx.expression().accept(this);
        }
    }
}
