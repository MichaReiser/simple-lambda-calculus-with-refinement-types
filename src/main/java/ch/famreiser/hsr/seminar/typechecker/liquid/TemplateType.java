package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.typechecker.types.Shape;

public class TemplateType implements Template {

    private final LiquidTypeVariable variable;
    private final Shape shape;
    private final Constraint predicate;

    public TemplateType(Shape shape, Constraint predicate) {
        this(LiquidTypeVariable.kappa(), shape, predicate);
    }

    public TemplateType(LiquidTypeVariable variable, Shape shape, Constraint predicate) {
        this.shape = shape;
        this.predicate = predicate;
        this.variable = variable;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public Constraint getPredicate() {
        return predicate;
    }

    @Override
    public LiquidTypeVariable getVariable() {
        return variable;
    }

    @Override
    public String toString() {
        return "{" + variable + ":" + this.shape + "|" + this.predicate + "}";
    }
}
