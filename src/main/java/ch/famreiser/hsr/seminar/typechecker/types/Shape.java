package ch.famreiser.hsr.seminar.typechecker.types;

import java.util.HashMap;
import java.util.Map;

public interface Shape {
    default Shape resolved() {
        return this;
    }

    default boolean contains(Shape other) {
        return false;
    }

    String toString();

    default Shape fresh() {
        return this.resolved().fresh(new HashMap<TypeVariable, TypeVariable>());
    }

    default Shape fresh(Map<TypeVariable, TypeVariable> mappings) {
        return this;
    }
}
