package ch.famreiser.hsr.seminar.typechecker.liquid;

import java.util.Collections;

public class LiquidTypeVariable implements Constraint {

    private static LiquidTypeVariable NU_INSTANCE = new LiquidTypeVariable("\u03BD");
    private static LiquidTypeVariable STAR = new LiquidTypeVariable("\u2605");
    private static int counter = 0;

    private String name;

    /**
     * Creates a new liquid type variable that is distinct from all the others
     */
    public static LiquidTypeVariable kappa() {
        String name = "\u03BA";

        int number = ++counter;
        while (number > 0) {
            name += new String(Character.toChars(8320 + (number % 10)));
            number = number / 10;
        }

        return new LiquidTypeVariable(name);
    }

    /**
     * Returns the fixed type variable nu
     */
    public static LiquidTypeVariable nu() {
        return NU_INSTANCE;
    }

    public static LiquidTypeVariable star() {
        return STAR;
    }

    /**
     * Creates a type variable with the given name
     */
    public static LiquidTypeVariable named(String name) {
        return new LiquidTypeVariable(name);
    }

    private LiquidTypeVariable(String name) {
        this.name = name;
    }

    public boolean isNu() {
        return this == NU_INSTANCE;
    }

    @Override
    public Iterable<LiquidTypeVariable> getLiquidVariables() {
        return Collections.singleton(this);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Constraint substitute(Constraint toSubstitute, Constraint with) {
        if (this == toSubstitute) {
            return with;
        }
        return this;
    }
}
