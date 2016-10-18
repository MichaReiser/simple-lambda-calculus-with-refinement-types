package ch.famreiser.hsr.seminar.typechecker.liquid;


import java.util.Collections;

public interface Constraint {

    String toString();

    default Iterable<Constraint> split() {
        return Collections.singleton(this);
    }

    Constraint substitute(Constraint toSubstitute, Constraint with);

    Iterable<LiquidTypeVariable> getLiquidVariables();
}
