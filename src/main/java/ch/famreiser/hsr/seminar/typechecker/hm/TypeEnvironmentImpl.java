package ch.famreiser.hsr.seminar.typechecker.hm;

import ch.famreiser.hsr.seminar.typechecker.types.Shape;
import com.google.common.collect.ImmutableMap;

public class TypeEnvironmentImpl implements TypeEnvironment {

    private final ImmutableMap<String, Shape> environment;

    private TypeEnvironmentImpl(ImmutableMap<String, Shape> environment) {
        this.environment = environment;
    }

    public static TypeEnvironment empty() {
        return new TypeEnvironmentImpl(ImmutableMap.of());
    }

    @Override
    public Shape getType(String name) {
        return environment.get(name);
    }

    @Override
    public TypeEnvironment setType(String name, Shape type) {
        return new TypeEnvironmentImpl(ImmutableMap.<String, Shape>builder().putAll(environment).put(name, type).build());
    }
}
