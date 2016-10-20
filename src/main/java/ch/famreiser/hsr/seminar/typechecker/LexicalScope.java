package ch.famreiser.hsr.seminar.typechecker;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import java.util.HashMap;
import java.util.Map;

public class LexicalScope {
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final LexicalScope parent;

    private LexicalScope(LexicalScope parent) {
        this.parent = parent;
    }

    public static LexicalScope empty() {
        return new LexicalScope(null);
    }

    public Symbol add(String name) {
        Symbol symbol = Symbol.named(name);
        this.add(symbol);
        return symbol;
    }

    public void add(Symbol symbol) {
        Preconditions.checkState(this.get(symbol.getName()) == null, "A variable with the name %s is already defined", symbol.getName());

        symbols.put(symbol.getName(), symbol);
    }

    public LexicalScope createChildScope() {
        return new LexicalScope(this);
    }

    public LexicalScope getParent() {
        return parent;
    }

    public Iterable<Symbol> getSymbols() {
        Iterable<Symbol> allSymbols = symbols.values();
        if (parent != null) {
            allSymbols = Iterables.concat(parent.getSymbols(), allSymbols);
        }

        return allSymbols;
    }

    public Symbol get(String name) {
        LexicalScope current = this;

        while ((current != null)) {
            Symbol symbol = current.getOwn(name);
            if (symbol != null) {
                return symbol;
            }
            current = current.getParent();
        }

        return null;
    }

    public Symbol getOwn(String name) {
        return this.symbols.get(name);
    }

    @Override
    public String toString() {
        return Joiner.on(", ").join(getSymbols());
    }
}
