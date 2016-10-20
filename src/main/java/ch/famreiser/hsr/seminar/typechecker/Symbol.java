package ch.famreiser.hsr.seminar.typechecker;

import com.google.common.base.Preconditions;

public class Symbol {

    private static Symbol NU = new Symbol("\u03BD");

    public static Symbol nu() {
        return NU;
    }

    public static Symbol named(String name) {
        Preconditions.checkNotNull(name);

        return new Symbol(name);
    }

    private final String name;

    private Symbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
