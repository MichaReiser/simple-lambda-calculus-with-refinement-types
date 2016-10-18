package ch.famreiser.hsr.seminar.typechecker.liquid;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Map;

public class TemplateEnvironment implements Iterable<Map.Entry<String, Template>> {

    private final ImmutableMap<String, Template> environment;

    public static TemplateEnvironment empty() {
        return new TemplateEnvironment(ImmutableMap.of());
    }

    private TemplateEnvironment(ImmutableMap<String, Template> environment) {
        this.environment = environment;
    }

    public TemplateEnvironment setType(String name, Template type) {
        return new TemplateEnvironment(ImmutableMap.<String, Template>builder().putAll(environment).put(name, type).build());
    }

    public Template getType(String name) {
        return environment.get(name);
    }

    public Iterable<Template> templates() {
        return environment.values();
    }

    @Override
    public String toString() {
        return environment.toString();
    }

    @Override
    public UnmodifiableIterator<Map.Entry<String, Template>> iterator() {
        return this.environment.entrySet().iterator();
    }
}
