package ch.famreiser.hsr.seminar.typechecker.liquid;

/**
 * Not Constraint (!a)
 */
public class NotConstraint implements Constraint {

    private final BoolConstraint inner;

    public NotConstraint(BoolConstraint inner) {
        this.inner = inner;
    }

    @Override
    public Iterable<LiquidTypeVariable> getLiquidVariables() {
        return inner.getLiquidVariables();
    }

    @Override
    public String toString() {
        return "\uc2ac" + inner;
    }

    public BoolConstraint getInner() {
        return inner;
    }

    @Override
    public Constraint substitute(Constraint toSubstitute, Constraint with) {
        return this;
    }
}
