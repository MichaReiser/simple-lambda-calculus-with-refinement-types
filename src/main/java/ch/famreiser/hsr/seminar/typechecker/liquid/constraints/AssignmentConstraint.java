package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;

/**
 * Created by micha on 19.10.16.
 */
public class AssignmentConstraint implements Constraint {

    private final Constraint variable;
    private final Constraint value;

    public AssignmentConstraint(Constraint variable, Constraint value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public <T> T accept(ConstraintVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AssignmentConstraint substitute(Constraint to, Constraint with) {
        return new AssignmentConstraint(variable.substitute(to, with), value.substitute(to, with));
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, value);
    }

    public Constraint getVariable() {
        return variable;
    }

    public Constraint getValue() {
        return value;
    }
}
