package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.typechecker.hm.TypeEnvironment;
import ch.famreiser.hsr.seminar.typechecker.types.Shape;

public class ShapeTypeEnvironment implements TypeEnvironment {

    private final TemplateEnvironment templateEnvironment;

    public ShapeTypeEnvironment(TemplateEnvironment templateEnvironment) {
        this.templateEnvironment = templateEnvironment;
    }

    @Override
    public Shape getType(String name) {
        Template template = templateEnvironment.getType(name);
        return template == null ? null: template.getShape();
    }

    @Override
    public TypeEnvironment setType(String name, Shape type) {
        return new ShapeTypeEnvironment(templateEnvironment.setType(name, new TemplateType(LiquidTypeVariable.nu(), type, BoolConstraint.alwaysTrue())));
    }
}
