package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.generated.LambdaCalculusBaseVisitor;
import ch.famreiser.hsr.seminar.generated.LambdaCalculusParser;
import ch.famreiser.hsr.seminar.typechecker.LexicalScope;
import ch.famreiser.hsr.seminar.typechecker.Symbol;
import ch.famreiser.hsr.seminar.typechecker.hm.HindleyMilnerTypeInference;
import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.*;
import ch.famreiser.hsr.seminar.typechecker.types.AbstractionType;
import ch.famreiser.hsr.seminar.typechecker.types.BoolType;
import ch.famreiser.hsr.seminar.typechecker.types.IntType;
import ch.famreiser.hsr.seminar.typechecker.types.Shape;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.antlr.v4.runtime.ParserRuleContext;

public class LiquidTypeChecker {

    public Template infer(ParserRuleContext expression) {
        Preconditions.checkNotNull(expression);

        TemplateEnvironment templateEnvironment = TemplateEnvironment.empty();
        LiquidInferenceResult constraintsResult = constraints(templateEnvironment, expression);

        solve(templateEnvironment, constraintsResult.constraints);
        return constraintsResult.template;
    }

    private LiquidInferenceResult constraints(TemplateEnvironment environment, ParserRuleContext expression) {
        LiquidInferenceResult result = expression.accept(new LiquidTypeCheckerVisitor(environment, LexicalScope.empty()));

        System.out.println(result);
        return result;
    }

    private void solve(TemplateEnvironment environment, Constraints constraints) {
        LiquidSolver solver = new LiquidSolver();
        solver.solve(environment, constraints);
    }

    private static class LiquidTypeCheckerVisitor extends LambdaCalculusBaseVisitor<LiquidInferenceResult> {

        private final HindleyMilnerTypeInference hindleyMilnerTypeInference = new HindleyMilnerTypeInference();
        private final TempSymbols tempSymbols;
        private final TemplateEnvironment templateEnvironment;
        private final LexicalScope scope;

        public LiquidTypeCheckerVisitor(TemplateEnvironment environment, LexicalScope scope) {
            this.templateEnvironment = environment;
            this.tempSymbols = new TempSymbols(environment, scope);
            this.scope = scope;
        }

        @Override
        public LiquidInferenceResult visitVariable(LambdaCalculusParser.VariableContext ctx) {
            hm(ctx); // ensures that the variable is defined
            Symbol variable = scope.get(ctx.Identifier().getText());
            return new LiquidInferenceResult(templateEnvironment.getType(variable), Constraints.empty());
        }

        @Override
        public LiquidInferenceResult visitConstant(LambdaCalculusParser.ConstantContext ctx) {
            Shape type = hm(ctx);

            Constraint valueConstraint;

            if (type instanceof BoolType) {
                valueConstraint = new BoolConstraint(Boolean.parseBoolean(ctx.Bool().getText()));
            } else if (type instanceof IntType) {
                valueConstraint = new NumberConstraint(Integer.parseInt(ctx.Int().getText()));
            } else {
                throw new UnsupportedOperationException("Unsupported base type " + type + " for constant.");
            }

            Constraint assignmentConstraint = new AssignmentConstraint(LiquidTypeVariable.nu(), valueConstraint);

            return new LiquidInferenceResult(new TemplateType(type, assignmentConstraint));
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

            Constraints constraints = abstraction.constraints
                    .union(argument.constraints)
                    .addAll(SubtypeConstraint.createFor(environmentForCurrentScope(), argument.template, calleeType.getArgument()));

            // FIXME substitution
            // Template returnType = calleeType.getReturnType().substitute(calleeType.getArgument().getPredicate(), argument.template.getPredicate());
            return new LiquidInferenceResult(calleeType.getReturnType(), constraints);
        }

        @Override
        public LiquidInferenceResult visitAbstraction(LambdaCalculusParser.AbstractionContext ctx) {
            AbstractionTemplate type = fresh((AbstractionType) hm(ctx), ctx.Identifier().getText());


            LexicalScope bodyScope = scope.createChildScope();
            Symbol argument = bodyScope.add(ctx.Identifier().getText());
            templateEnvironment.setType(argument, type.getArgument());
            LiquidInferenceResult inferredBody = ctx.expression().accept(new LiquidTypeCheckerVisitor(templateEnvironment, bodyScope));

            TemplateEnvironment bodyEnvironment = templateEnvironment.forScope(bodyScope);
            Constraints constraints = inferredBody.constraints
                    .add(new WellFormednessConstraint(bodyEnvironment, type))
                    .addAll(SubtypeConstraint.createFor(bodyEnvironment, inferredBody.template, type.getReturnType()));

            return new LiquidInferenceResult(type, constraints);
        }

        @Override
        public LiquidInferenceResult visitIfThenElse(LambdaCalculusParser.IfThenElseContext ctx) {
            Template returnType = fresh(hm(ctx));

            LiquidInferenceResult condition = ctx.expression().accept(this);
            LiquidInferenceResult thenBranch = ctx.body(0).accept(this);
            LiquidInferenceResult elseBranch = ctx.body(1).accept(this);

            TemplateEnvironment currentScopeEnvironment = environmentForCurrentScope();
            Constraints constraints = condition.constraints
                    .union(thenBranch.constraints)
                    .union(elseBranch.constraints)
                    .add(new WellFormednessConstraint(currentScopeEnvironment, returnType))
                    .addAll(SubtypeConstraint.createFor(currentScopeEnvironment, thenBranch.template, returnType))
                    .addAll(SubtypeConstraint.createFor(currentScopeEnvironment, elseBranch.template, returnType)); // FIXME use if environment

            return new LiquidInferenceResult(returnType, constraints);
        }

        @Override
        public LiquidInferenceResult visitLetIn(LambdaCalculusParser.LetInContext ctx) {
            Template returnType = fresh(hm(ctx));

            LiquidInferenceResult initializerType = ctx.expression().accept(this);

            LexicalScope bodyScope = scope.createChildScope();
            Symbol variableSymbol = bodyScope.add(ctx.Identifier().getText());
            templateEnvironment.setType(variableSymbol, initializerType.template);
            LiquidInferenceResult body = ctx.body().accept(new LiquidTypeCheckerVisitor(templateEnvironment, bodyScope));

            TemplateEnvironment bodyEnvironment = templateEnvironment.forScope(bodyScope);
            Constraints constraints = initializerType.constraints
                    .union(body.constraints)
                    .add(new WellFormednessConstraint(environmentForCurrentScope(), returnType))
                    .addAll(SubtypeConstraint.createFor(bodyEnvironment, body.template, returnType));
            return new LiquidInferenceResult(returnType, constraints);
        }

        @Override
        public LiquidInferenceResult visitBinaryExpression(LambdaCalculusParser.BinaryExpressionContext ctx) {
            LiquidInferenceResult left = ctx.expression(0).accept(this); // HM now happens multiple times..
            LiquidInferenceResult right = ctx.expression(1).accept(this);
            BinaryConstraint.Operator operator = BinaryConstraint.Operator.fromString(ctx.Operator().getText());

            Shape returnShape = hm(ctx);

            Symbol leftVariable = tempSymbols.get(left.template);
            Symbol rightVariable = tempSymbols.get(right.template);

            Constraint rightConstraint = BoolConstraint.alwaysTrue();

            if (operator == BinaryConstraint.Operator.DIVISION) {
                rightConstraint = new BinaryConstraint(LiquidTypeVariable.named(rightVariable), BinaryConstraint.Operator.NOT_EQUAL_TO, NumberConstraint.valueOf(0));
            }

            AbstractionTemplate resultType = AbstractionTemplate.create(
                    new TemplateType(right.template.getShape(), rightConstraint),
                    new TemplateType(returnShape, new BinaryConstraint(LiquidTypeVariable.named(leftVariable), operator, LiquidTypeVariable.named(rightVariable))),
                    BoolConstraint.alwaysTrue()
            );

            AbstractionTemplate first = AbstractionTemplate.create(
                    new TemplateType(left.template.getShape(), BoolConstraint.alwaysTrue()),
                    resultType,
                    BoolConstraint.alwaysTrue());

            return application(application(new LiquidInferenceResult(first), left), right);
        }

        private Shape hm(ParserRuleContext node) {
            return hindleyMilnerTypeInference.infer(templateEnvironment.asTypeEnvironment(), scope, node);
        }

        private TemplateEnvironment environmentForCurrentScope() {
            return templateEnvironment.forScope(scope);
        }

        private Template fresh(Shape shape) {
            return fresh(shape, LiquidTypeVariable.kappa());
        }

        private Template fresh(Shape shape, LiquidTypeVariable variable) {
            return new TemplateType(shape.resolved(), variable);
        }

        private AbstractionTemplate fresh(AbstractionType shape, String argumentName) {
            return AbstractionTemplate.create(fresh(shape.getArgument(), LiquidTypeVariable.kappa()), fresh(shape.getReturnType()), LiquidTypeVariable.kappa());
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

    private static class TempSymbols {
        private final TemplateEnvironment environment;
        private final LexicalScope scope;
        private int nextTempId = 0;

        private TempSymbols(TemplateEnvironment environment, LexicalScope scope) {
            this.environment = environment;
            this.scope = scope;
        }

        public Symbol get(Template template) {
            Preconditions.checkNotNull(template);

            Symbol symbol = environment.getSymbol(template);
            if (symbol == null) {
                symbol = fresh();
                scope.add(symbol);
                environment.setType(symbol, template);
            }

            return symbol;
        }

        public Symbol fresh() {
            return Symbol.named("temp$$" + nextTempId++);
        }
    }
}


