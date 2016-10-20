package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;


public class NumberConstraint implements Constraint {

    private final int value;

    public NumberConstraint(int value) {
        this.value = value;
    }

    public int getValue() { return value; }

    @Override
    public <T> T accept(ConstraintVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Constraint substitute(Constraint to, Constraint with) {
        return this;
    }

    @Override
    public String toString() {
        return value + "";
    }

    public static Constraint valueOf(int i) {
        return new NumberConstraint(i);
    }
}
