package ch.famreiser.hsr.seminar.typechecker.liquid;

import ch.famreiser.hsr.seminar.typechecker.Symbol;
import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.Constraint;
import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.Constraints;
import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.SubtypeConstraint;
import ch.famreiser.hsr.seminar.typechecker.liquid.constraints.WellFormednessConstraint;

import java.util.Map;

/**
 * Created by micha on 18.10.16.
 */
public class LiquidSolver {

    public void solve(TemplateEnvironment environment, Constraints constraints) {
        LiquidFixPointSolverInstance solverContext = new LiquidFixPointSolverInstance();

        for (Map.Entry<Symbol, Template> mapping : environment) {
            solverContext.addBinding(mapping.getKey(), mapping.getValue());
        }

        for (Constraint constraint : constraints) {
            if (constraint instanceof WellFormednessConstraint) {
                solverContext.addWellFormednessConstraint((WellFormednessConstraint) constraint);
            } else if (constraint instanceof SubtypeConstraint) {
                solverContext.addSubtypingConstraint((SubtypeConstraint) constraint);
            } else {
                throw new IllegalArgumentException("Unexpected constraint" + constraint);
            }
        }

        solverContext.solve();
    }
}
