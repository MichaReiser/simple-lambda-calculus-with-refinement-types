package ch.famreiser.hsr.seminar.typechecker.liquid;

import com.google.common.collect.Iterables;

public class SubtypeConstraint implements Constraint {
    private final TemplateEnvironment environment;
    private final Template left;
    private final Template right;

    public SubtypeConstraint(TemplateEnvironment environment, Template left, Template right) {
        this.environment = environment;
        this.left = left;
        this.right = right;
    }

    @Override
    public Iterable<LiquidTypeVariable> getLiquidVariables() {
        return Iterables.concat(left.getPredicate().getLiquidVariables(), right.getPredicate().getLiquidVariables());
    }

    @Override
    public String toString() {
        return environment.toString() + "\u22A2" + left + "<:" + right;
    }

    @Override
    public Constraint substitute(Constraint toSubstitute, Constraint with) {
        // TODO Correct?
        return this;
    }

    public Template getLeft() {
        return left;
    }
}
