package ch.famreiser.hsr.seminar.typechecker.hm;

import ch.famreiser.hsr.seminar.generated.LambdaCalculusBaseVisitor;
import ch.famreiser.hsr.seminar.generated.LambdaCalculusParser;
import ch.famreiser.hsr.seminar.typechecker.types.*;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Implementation of the Hindley Milner Type Inference algorithm
 */
public class HindleyMilnerTypeInference {

    public Shape infer(ParserRuleContext expression) throws TypeInferenceException {
        return infer(TypeEnvironmentImpl.empty(), expression);
    }

    public Shape infer(TypeEnvironment typeEnvironment, ParserRuleContext expression) throws TypeInferenceException {
        return expression.accept(new HindleyMilnerVisitor(typeEnvironment));
    }

    public Shape unify(Shape a, Shape b) throws TypeInferenceException {
        Shape resolvedA = a.resolved();
        Shape resolvedB = b.resolved();

        if (resolvedA == resolvedB) {
            return resolvedB;
        }

        if (resolvedA instanceof TypeVariable) {
            if (resolvedA.contains(resolvedB)) {
                throw new TypeInferenceException("The type " + resolvedB + " occurres in the type " + resolvedA + ".");
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

        throw new TypeInferenceException("The type " + a + " and " + b + " cannot be unified, no unification therefore exists");
    }

    private class HindleyMilnerVisitor extends LambdaCalculusBaseVisitor<Shape> {
        private final TypeEnvironment typeEnvironment;

        HindleyMilnerVisitor(TypeEnvironment typeEnvironment) {
            this.typeEnvironment = typeEnvironment;
        }

        @Override
        public Shape visitConstant(LambdaCalculusParser.ConstantContext ctx) {
            if (ctx.Bool() != null) {
                return BoolType.create();
            }
            return NumberType.create();
        }

        @Override
        public Shape visitVariable(LambdaCalculusParser.VariableContext ctx) {
            Shape type = this.typeEnvironment.getType(ctx.Identifier().getText());

            if (type == null) {
                throw new TypeInferenceException("The unknown variable '" + ctx.Identifier().getText() + "' is referenced in the program.");
            }

            return type;
        }

        @Override
        public Shape visitLetIn(LambdaCalculusParser.LetInContext ctx) {
            Shape initializerType = ctx.expression().accept(this);

            TypeVariable bodyType = TypeVariable.create();
            TypeEnvironment bodyTypeEnvironment = typeEnvironment.setType(ctx.Identifier().getText(), initializerType);
            Shape inferredBodyType = ctx.body().accept(new HindleyMilnerVisitor(bodyTypeEnvironment));

            return unify(bodyType, inferredBodyType);
        }

        @Override
        public Shape visitAbstraction(LambdaCalculusParser.AbstractionContext ctx) {
            Shape returnType = TypeVariable.create();
            Shape argumentType = TypeVariable.create();

            TypeEnvironment bodyTypeEnvironment = typeEnvironment.setType(ctx.Identifier().getText(), argumentType);
            Shape inferredReturnType = ctx.expression().accept(new HindleyMilnerVisitor(bodyTypeEnvironment));

            return AbstractionType.create(argumentType, unify(returnType, inferredReturnType));
        }

        @Override
        public Shape visitApplication(LambdaCalculusParser.ApplicationContext ctx) {
            Shape callee = ctx.expression(0).accept(this);

            if (!(callee instanceof AbstractionType)) {
                throw new TypeInferenceException("Tried to invoke function on " + callee);
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
                    unify(ctx.expression(0).accept(this), NumberType.create());
                    unify(ctx.expression(1).accept(this), NumberType.create());
                    return NumberType.create();
                case "<":
                case "<=":
                case ">":
                case ">=":
                    unify(ctx.expression(0).accept(this), NumberType.create());
                    unify(ctx.expression(1).accept(this), NumberType.create());
                    return BoolType.create();
                case "==": {
                    // Maybe we should not allow comparing abstraction but whatever.
                    unify(ctx.expression(0).accept(this), ctx.expression(1).accept(this));
                    return BoolType.create();
                }

                default:
                    throw new TypeInferenceException("Unknown operator " + operator);
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
