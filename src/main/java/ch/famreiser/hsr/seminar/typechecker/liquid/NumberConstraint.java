package ch.famreiser.hsr.seminar.typechecker.liquid;


import java.util.Collections;

public class NumberConstraint implements Constraint {

    private final int value;

    public NumberConstraint(int value) {
        this.value = value;
    }

    @Override
    public Iterable<LiquidTypeVariable> getLiquidVariables() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return value + "";
    }

    public int getValue() { return value; }

    @Override
    public Constraint substitute(Constraint toSubstitute, Constraint with) {
        return this;
    }
}
