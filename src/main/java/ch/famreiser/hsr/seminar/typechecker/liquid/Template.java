package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.Constraint;
import ch.famreiser.hsr.seminar.typechecker.types.Shape;

public interface Template {
    Shape getShape();

    Constraint getPredicate();

    String toString();
}
