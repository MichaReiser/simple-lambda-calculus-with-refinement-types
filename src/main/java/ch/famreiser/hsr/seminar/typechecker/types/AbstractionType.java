package ch.famreiser.hsr.seminar.typechecker.types;

import java.util.Map;

public class AbstractionType implements Shape {

    private final Shape returnType;
    private final Shape argument;

    private AbstractionType(Shape argument, Shape returnType) {
        this.returnType = returnType;
        this.argument = argument;
    }

    public static AbstractionType create(Shape argument, Shape returnType) {
        return new AbstractionType(argument, returnType);
    }

    public Shape getArgument() {
        return argument;
    }

    public Shape getReturnType() {
        return returnType;
    }

    @Override
    public boolean contains(Shape other) {
        return argument.contains(other) || returnType.contains(other);
    }

    @Override
    public Shape fresh(Map<TypeVariable, TypeVariable> mappings) {
       return create(argument.resolved().fresh(mappings), returnType.resolved().fresh(mappings));
    }

    @Override
    public String toString() {
        return argument + " => " + returnType;
    }
}
