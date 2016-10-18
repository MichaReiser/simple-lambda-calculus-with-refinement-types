package ch.famreiser.hsr.seminar.typechecker.hm;

import ch.famreiser.hsr.seminar.typechecker.types.Shape;

public interface TypeEnvironment {
    Shape getType(String name);

    TypeEnvironment setType(String name, Shape type);
}
