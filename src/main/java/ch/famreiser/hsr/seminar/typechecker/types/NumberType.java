package ch.famreiser.hsr.seminar.typechecker.types;

public class NumberType implements Shape {

    private static NumberType INSTANCE = new NumberType();

    private NumberType() {}

    public static NumberType create() {
        return NumberType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Number";
    }
}
