package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;


public interface Constraint {

    <T> T accept(ConstraintVisitor<? extends T> visitor);

    Constraint substitute(Constraint to, Constraint with);

    String toString();
}
