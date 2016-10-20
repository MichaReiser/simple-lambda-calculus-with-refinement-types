package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.typechecker.LexicalScope;
import ch.famreiser.hsr.seminar.typechecker.Symbol;
import ch.famreiser.hsr.seminar.typechecker.types.TypeEnvironment;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TemplateEnvironment implements Iterable<Map.Entry<Symbol, Template>>{

    private final BiMap<Symbol, Template> environment;

    private TemplateEnvironment(Map<Symbol, Template> environment) {
        this.environment = HashBiMap.create(environment);
    }

    public static TemplateEnvironment empty() {
        return new TemplateEnvironment(new HashMap<>());
    }

    public void setType(Symbol symbol, Template type) {
        Preconditions.checkNotNull(symbol);
        Preconditions.checkNotNull(type);
        Preconditions.checkState(!environment.containsKey(symbol), "The symbol " + symbol.getName() + " is already defined in the environment");

        environment.put(symbol, type);
    }

    public Template getType(Symbol symbol) {
        Preconditions.checkNotNull(symbol);
        return environment.get(symbol);
    }

    public Symbol getSymbol(Template template) {
        return environment.inverse().get(template);
    }

    /**
     * Returns a copy of this template environment that only contains the type information
     * @return the type environment
     */
    public TypeEnvironment asTypeEnvironment() {
        return TypeEnvironment.of(Maps.transformEntries(environment, (symbol, template) -> template.getShape()));
    }

    public Iterable<Template> templates() {
        return environment.values();
    }

    public Iterable<Symbol> getSymbols() { return environment.keySet(); }

    public TemplateEnvironment forScope(LexicalScope scope) {
        Preconditions.checkNotNull(scope);

        Map<Symbol, Template> scopeEnvironment = new HashMap<>();

        for (Symbol symbol : scope.getSymbols()) {
            scopeEnvironment.put(symbol, this.getType(symbol));
        }

        return new TemplateEnvironment(scopeEnvironment);
    }

    @Override
    public Iterator<Map.Entry<Symbol, Template>> iterator() {
        return environment.entrySet().iterator();
    }

    @Override
    public String toString() {
        return environment.toString();
    }
}
