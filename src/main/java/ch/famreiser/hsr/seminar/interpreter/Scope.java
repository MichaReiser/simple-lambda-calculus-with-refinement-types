package ch.famreiser.hsr.seminar.interpreter;

import com.google.common.collect.ImmutableMap;

/**
 * Created by micha on 05.10.16.
 */
public class Scope {
    private final ImmutableMap<String, Object> scope;

    public Scope() {
        this(ImmutableMap.of());
    }

    private Scope(ImmutableMap<String, Object> mappings) {
        this.scope = mappings;
    }

    public Scope set(String name, Object value) {
        return new Scope(ImmutableMap.<String, Object>builder().putAll(scope).put(name, value).build());
    }

    public Object get(String name) {
        return this.scope.get(name);
    }

    public boolean has(String name) {
        return this.scope.containsKey(name);
    }
}
