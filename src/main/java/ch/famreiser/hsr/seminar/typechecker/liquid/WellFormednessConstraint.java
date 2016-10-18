package ch.famreiser.hsr.seminar.typechecker.liquid;

public class WellFormednessConstraint implements Constraint {

    private TemplateEnvironment environment;
    private Template template;

    public WellFormednessConstraint(TemplateEnvironment in, Template template) {
        this.environment = in;
        this.template = template;
    }

    @Override
    public Iterable<LiquidTypeVariable> getLiquidVariables() {
        return template.getPredicate().getLiquidVariables();
    }

    public TemplateEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public String toString() {
        return environment.toString() + "\u22A2" + template;
    }

    @Override
    public Constraint substitute(Constraint toSubstitute, Constraint with) {
        // TODO correct
        return this;
    }
}
