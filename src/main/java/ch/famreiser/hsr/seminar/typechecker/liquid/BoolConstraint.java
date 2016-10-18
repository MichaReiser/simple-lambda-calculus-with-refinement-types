package ch.famreiser.hsr.seminar.typechecker.liquid;

import java.util.Collections;

/**
 * A constant constraint that is either false or true
 */
public class BoolConstraint implements Constraint {

    private final boolean fulfilled;

    public static BoolConstraint alwaysTrue() {
        return new BoolConstraint(true);
    }

    public static BoolConstraint alwaysFalse() {
        return new BoolConstraint(false);
    }

    public BoolConstraint(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    @Override
    public Iterable<LiquidTypeVariable> getLiquidVariables() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return fulfilled + "";
    }

    @Override
    public Constraint substitute(Constraint toSubstitute, Constraint with) {
        return this;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }
}
