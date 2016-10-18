package ch.famreiser.hsr.seminar.typechecker.liquid.solver;

import ch.famreiser.hsr.seminar.typechecker.liquid.*;
import com.microsoft.z3.*;

public class LiquidSolver {
    private final Context context;
    private final Solver solver;

    public LiquidSolver(Context context) {
        this.context = context;
        this.solver = context.mkSolver();
    }

    public boolean satisfies(Constraint c, Constraint condition) {
        solver.push();

        try {
            solver.add(this.testIfSatisfies(c, condition));
            Status status = solver.check();

            if (status == Status.UNSATISFIABLE) {
                return true;
            }

            Model model = solver.getModel();
            System.out.println(model);
            return false;
        } finally {
            solver.pop();
        }
    }

    public BoolExpr testIfSatisfies(Constraint toTest, Constraint toSatisfy) {
        BoolExpr expr;
        if (toSatisfy instanceof SubtypeConstraint) {
            Template left = ((SubtypeConstraint) toSatisfy).getLeft();
            expr = context.mkImplies((BoolExpr) this.visit(left.getPredicate()), (BoolExpr) this.visit(toTest));
        } else if (toSatisfy instanceof WellFormednessConstraint) {
            expr = context.mkTrue();
        } else {
            expr = context.mkImplies((BoolExpr) this.visit(toTest), (BoolExpr)this.visit(toSatisfy));
        }

        return context.mkNot(expr);
    }

    private Expr visit(Constraint constraint) {
        if (constraint instanceof BinaryConstraint) {
            return this.visit((BinaryConstraint) constraint);
        }
        if (constraint instanceof BoolConstraint) {
            return this.visit((BoolConstraint) constraint);
        }
        if (constraint instanceof NotConstraint) {
            return this.visit((NotConstraint) constraint);
        }
        if (constraint instanceof NumberConstraint) {
            return this.visit((NumberConstraint) constraint);
        }

        if (constraint instanceof ValidConstraint) {
            // Muss für alle k1 gelten, bzw für alle Variablen im Ausdruck

            return context.mkNot((BoolExpr) this.visit(((ValidConstraint)constraint).getInner()));
        }

        if (constraint instanceof LiquidTypeVariable) {
            return this.visit((LiquidTypeVariable) constraint);
        }

        if (constraint instanceof SubtypeConstraint) {
            return context.mkTrue();
        }

        if (constraint instanceof WellFormednessConstraint) {
//            List<Formula> variables = Lists.newLinkedList();
//            variables.add(this.visit(LiquidTypeVariable.nu()));
//
//            for (Template template : ((WellFormednessConstraint) toSatisfy).getEnvironment().templates()) {
//                variables.add(this.visit(template.getVariable()));
//            }
//
//            return context.mkExists()
//
//            return formulaManager.getQuantifiedFormulaManager().exists(variables, (BooleanFormula) this.visit(toTest));
            return context.mkFalse();
        }

        throw new IllegalArgumentException("Unhandled constraint " + constraint);
    }

    private BoolExpr visit(BinaryConstraint constraint) {
        switch (constraint.getOperation()) {
            case IMPLIES:
                return context.mkImplies((BoolExpr)this.visit(constraint.getLeft()), (BoolExpr) this.visit(constraint.getRight()));
            case LESS_THAN:
                return context.mkLt((ArithExpr) this.visit(constraint.getLeft()), (ArithExpr) this.visit(constraint.getRight()));
            case LESS_OR_EQUAL_THAN:
                return context.mkLe((ArithExpr) this.visit(constraint.getLeft()), (ArithExpr) this.visit(constraint.getRight()));
            case EQUAL:
                ArithExpr left = (ArithExpr) this.visit(constraint.getLeft());
                ArithExpr right = (ArithExpr) this.visit(constraint.getRight());
                return context.mkAnd(context.mkLe(left, right), context.mkGe(left, right));
            case EQUIVALENCE:
                return context.mkNot(context.mkEq(this.visit(constraint.getLeft()), this.visit(constraint.getRight())));
        }

        throw new IllegalArgumentException("Not yet supported operand " + constraint.getOperation());
    }

    private Expr visit(LiquidTypeVariable variable) {
        // Hmm need to know the type here
        Symbol symbol = context.mkSymbol(variable.getName());
        return context.mkIntConst(symbol);
    }

    private BoolExpr visit(BoolConstraint constraint) {
        return context.mkBool(constraint.isFulfilled());
    }

    private BoolExpr visit(NotConstraint constraint) {
        return context.mkNot(this.visit(constraint.getInner()));
    }

    private IntExpr visit(NumberConstraint constraint) {
        return context.mkInt(constraint.getValue());
    }

    public void requestShutdown() {
        context.close();
    }
}
