package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;


public interface ConstraintVisitor<T> {

    default T visit(Constraint constraint) {
        return constraint.accept(this);
    }

    T visit(AssignmentConstraint assignmentConstraint);

    T visit(BinaryConstraint binaryConstraint);

    T visit(BoolConstraint boolConstraint);

    T visit(LiquidTypeVariable liquidTypeVariable);

    T visit(NotConstraint notConstraint);

    T visit(NumberConstraint numberConstraint);

    T visit(SubtypeConstraint subtypeConstraint);

    T visit(WellFormednessConstraint wellFormednessConstraint);
}
