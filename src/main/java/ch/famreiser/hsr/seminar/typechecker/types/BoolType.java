package ch.famreiser.hsr.seminar.typechecker.types;

public class BoolType implements Shape {

    private static BoolType INSTANCE = new BoolType();

    private BoolType() {

    }

    public static BoolType create() {
        return BoolType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Bool";
    }
}
