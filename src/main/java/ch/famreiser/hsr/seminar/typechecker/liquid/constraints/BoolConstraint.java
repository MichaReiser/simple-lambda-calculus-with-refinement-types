package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;

/**
 * A constant constraint that is either false or true
 */
public class BoolConstraint implements Constraint {

    private final boolean fulfilled;

    public static BoolConstraint alwaysTrue() {
        return new BoolConstraint(true);
    }

    public static BoolConstraint alwaysFalse() {
        return new BoolConstraint(false);
    }

    public BoolConstraint(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    @Override
    public <T> T accept(ConstraintVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public BoolConstraint substitute(Constraint to, Constraint with) {
        return this;
    }

    @Override
    public String toString() {
        return fulfilled + "";
    }
}
