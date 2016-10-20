package ch.famreiser.hsr.seminar.typechecker.types;

public class IntType implements Shape {

    private static IntType INSTANCE = new IntType();

    private IntType() {}

    public static IntType create() {
        return IntType.INSTANCE;
    }

    @Override
    public String toString() {
        return "int";
    }
}
