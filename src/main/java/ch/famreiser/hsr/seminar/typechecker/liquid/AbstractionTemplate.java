package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.Constraint;
import ch.famreiser.hsr.seminar.typechecker.types.AbstractionType;

public class AbstractionTemplate implements Template {

    private final Template returnType;
    private final Template argument;
    private final Constraint constraint;

    private AbstractionTemplate(Template argument, Template returnType, Constraint constraint) {
        this.returnType = returnType;
        this.argument = argument;
        this.constraint = constraint;
    }

    public static AbstractionTemplate create(Template argument, Template returnType, Constraint constraint) {
        return new AbstractionTemplate(argument, returnType, constraint);
    }

    public Template getArgument() {
        return argument;
    }

    public Template getReturnType() {
        return returnType;
    }

    public AbstractionType getShape() {
        return AbstractionType.create(this.argument.getShape(), this.returnType.getShape());
    }

    @Override
    public Constraint getPredicate() {
        return constraint;
    }

    @Override
    public String toString() {
        return "(" + argument + " \u21A6 " + returnType + ")";
    }
}
