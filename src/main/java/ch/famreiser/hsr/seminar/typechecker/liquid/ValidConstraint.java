package ch.famreiser.hsr.seminar.typechecker.liquid;

/**
 * Created by micha on 12.10.16.
 */
public class ValidConstraint implements Constraint {

    private final Constraint inner;

    public ValidConstraint(Constraint inner) {
        this.inner = inner;
    }

    @Override
    public Constraint substitute(Constraint toSubstitute, Constraint with) {
        return new ValidConstraint(inner.substitute(toSubstitute, with));
    }

    @Override
    public Iterable<LiquidTypeVariable> getLiquidVariables() {
        return inner.getLiquidVariables();
    }

    public Constraint getInner() {
        return inner;
    }

    @Override
    public String toString() {
        return "Valid(" + inner + ")";
    }
}
