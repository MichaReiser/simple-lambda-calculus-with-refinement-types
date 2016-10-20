package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;

import ch.famreiser.hsr.seminar.typechecker.Symbol;

public class LiquidTypeVariable implements Constraint {

    private static LiquidTypeVariable NU_INSTANCE = new LiquidTypeVariable(Symbol.nu());
    private static int kappaCounter = 0;

    private final Symbol symbol;

    /**
     * Creates a new liquid type variable that is distinct from all the others
     */
    public static LiquidTypeVariable kappa() {
        String name = "\u03BA";

        int number = ++kappaCounter;
        while (number > 0) {
            name += new String(Character.toChars(8320 + (number % 10)));
            number = number / 10;
        }

        return new LiquidTypeVariable(Symbol.named(name));
    }

    /**
     * Returns the fixed type variable nu
     */
    public static LiquidTypeVariable nu() {
        return NU_INSTANCE;
    }

    /**
     * Creates a type variable with the given symbol
     */
    public static LiquidTypeVariable named(Symbol symbol) {
        return new LiquidTypeVariable(symbol);
    }

    private LiquidTypeVariable(Symbol symbol) {
        this.symbol = symbol;
    }

    public boolean isNu() {
        return this == NU_INSTANCE;
    }

    public boolean isKappa() {
        return symbol.getName().toCharArray()[0] == '\u03BA';
    }

    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public <T> T accept(ConstraintVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Constraint substitute(Constraint to, Constraint with) {
        if (this.equals(to)) {
            return with;
        }
        return this;
    }

    @Override
    public String toString() {
        return symbol.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj.getClass().equals(LiquidTypeVariable.class)) {
            return ((LiquidTypeVariable) obj).symbol.equals(this.symbol);
        }

        return false;
    }
}
