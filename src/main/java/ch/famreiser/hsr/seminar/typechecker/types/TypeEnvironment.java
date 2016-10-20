package ch.famreiser.hsr.seminar.typechecker.types;

import ch.famreiser.hsr.seminar.typechecker.Symbol;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class TypeEnvironment {

    public static TypeEnvironment empty() {
        return new TypeEnvironment(new HashMap<>());
    }

    public static TypeEnvironment of(Map<Symbol, Shape> mappings) {
        return new TypeEnvironment(Maps.newHashMap(mappings));
    }

    private final Map<Symbol, Shape> environment;

    private TypeEnvironment(Map<Symbol, Shape> environment) {
        this.environment = environment;
    }

    public Shape getType(Symbol symbol) {
        Preconditions.checkNotNull(symbol);
        return environment.get(symbol);
    }

    public void setType(Symbol symbol, Shape type) {
        Preconditions.checkNotNull(symbol);
        Preconditions.checkNotNull(type);
        Preconditions.checkState(!environment.containsKey(symbol));

        environment.put(symbol, type);
    }

    @Override
    public String toString() {
        return "\u0393 " + environment.toString();
    }
}
