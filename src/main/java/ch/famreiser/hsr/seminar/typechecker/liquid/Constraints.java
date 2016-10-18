package ch.famreiser.hsr.seminar.typechecker.liquid;


import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Iterator;

public class Constraints implements Iterable<Constraint> {
    private final ImmutableSet<Constraint> constraints;

    private Constraints(ImmutableSet<Constraint> constraints) {
        this.constraints = constraints;
    }

    public static Constraints of(Iterable<Constraint> constraints) {
        return new Constraints(ImmutableSet.copyOf(constraints));
    }

    public static Constraints empty() {
        return new Constraints(ImmutableSet.of());
    }

    public Constraints union(Constraints other) {
        return new Constraints(Sets.union(constraints, other.constraints).immutableCopy());
    }

    public Constraints add(Constraint constraint) {
        return new Constraints(ImmutableSet.<Constraint>builder().addAll(constraints).add(constraint).build());
    }

    @Override
    public String toString() {
        return constraints.toString();
    }

    @Override
    public Iterator<Constraint> iterator() {
        return constraints.iterator();
    }
}
