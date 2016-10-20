package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;

/**
 * Not Constraint (!a)
 */
public class NotConstraint implements Constraint {

    private final BoolConstraint inner;

    public NotConstraint(BoolConstraint inner) {
        this.inner = inner;
    }

    public BoolConstraint getInner() {
        return inner;
    }

    @Override
    public <T> T accept(ConstraintVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public NotConstraint substitute(Constraint to, Constraint with) {
        return new NotConstraint(inner.substitute(to, with));
    }

    @Override
    public String toString() {
        return "\uc2ac" + inner;
    }
}
