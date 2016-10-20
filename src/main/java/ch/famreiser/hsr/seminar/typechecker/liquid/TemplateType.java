package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.typechecker.Symbol;
import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.Constraint;
import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.LiquidTypeVariable;
import ch.famreiser.hsr.seminar.typechecker.types.Shape;

public class TemplateType implements Template {

    private final Shape shape;
    private final Constraint predicate;


    public TemplateType(Shape shape, Constraint predicate) {
        this.shape = shape;
        this.predicate = predicate;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public Constraint getPredicate() {
        return predicate;
    }

    @Override
    public String toString() {
        return "{\u03BD:" + this.shape + "|" + this.predicate + "}";
    }
}
