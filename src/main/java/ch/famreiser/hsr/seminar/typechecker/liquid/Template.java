package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.typechecker.types.Shape;

public interface Template {
    Shape getShape();

    Constraint getPredicate();

    LiquidTypeVariable getVariable();

    String toString();
}
