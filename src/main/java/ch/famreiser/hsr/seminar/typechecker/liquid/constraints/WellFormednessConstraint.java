package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;

import ch.famreiser.hsr.seminar.typechecker.liquid.Template;
import ch.famreiser.hsr.seminar.typechecker.liquid.TemplateEnvironment;

public class WellFormednessConstraint implements Constraint {

    private TemplateEnvironment environment;
    private Template template;

    public WellFormednessConstraint(TemplateEnvironment in, Template template) {
        this.environment = in;
        this.template = template;
    }

    public TemplateEnvironment getEnvironment() {
        return environment;
    }

    public Template getTemplate() { return template; }

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
        return environment.toString() + "\u22A2" + template;
    }
}
