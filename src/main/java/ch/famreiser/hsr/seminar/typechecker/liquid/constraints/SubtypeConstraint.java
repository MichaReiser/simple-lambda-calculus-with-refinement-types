package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;

import ch.famreiser.hsr.seminar.typechecker.liquid.AbstractionTemplate;
import ch.famreiser.hsr.seminar.typechecker.liquid.Template;
import ch.famreiser.hsr.seminar.typechecker.liquid.TemplateEnvironment;

import java.util.Collections;

public class SubtypeConstraint implements Constraint {
    private final TemplateEnvironment environment;
    private final Template left;
    private final Template right;

    public static Iterable<SubtypeConstraint> createFor(TemplateEnvironment environment, Template left, Template right) {
        if (left instanceof AbstractionTemplate) {
            return createFor(environment, ((AbstractionTemplate) left).getArgument(), right);
        }

        if (right.getPredicate() instanceof BoolConstraint && ((BoolConstraint) right.getPredicate()
        ).isFulfilled()) {
            return Collections.emptyList();
        }

        return Collections.singleton(new SubtypeConstraint(environment, left, right));
    }

    private SubtypeConstraint(TemplateEnvironment environment, Template left, Template right) {
        this.environment = environment;
        this.left = left;
        this.right = right;
    }

    public Template getLeft() {
        return left;
    }

    public TemplateEnvironment getEnvironment() {
        return environment;
    }

    public Template getRight() {
        return right;
    }

    @Override
    public <T> T accept(ConstraintVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Constraint substitute(Constraint to, Constraint with) {
        return this;
    }

    @Override
    public String toString() {
        return environment.toString() + "\u22A2" + left + "<:" + right;
    }
}
