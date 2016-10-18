package ch.famreiser.hsr.seminar.typechecker.types;

import ch.famreiser.hsr.seminar.typechecker.liquid.BoolConstraint;
import ch.famreiser.hsr.seminar.typechecker.liquid.Constraint;

/**
 * Created by micha on 02.10.16.
 */
public class DependentType {

    private final Shape shape;
    private final Constraint predicate;

    public static DependentType of(Shape shape) {
        return new DependentType(shape, BoolConstraint.alwaysTrue());
    }

    private DependentType(Shape shape, Constraint predicate) {
        this.shape = shape;
        this.predicate = predicate;
    }

    public DependentType withConstraint(Constraint constraint ) {
        return new DependentType(this.shape, constraint);
    }

    public String toString() {
        return "{v:" + shape + "|" + predicate + "}";
    }

    public Shape getShape() {
        return shape;
    }
}