package ch.famreiser.hsr.seminar.typechecker.liquid.solver;


import com.microsoft.z3.Context;
import com.microsoft.z3.Global;
import com.microsoft.z3.Log;

public class SolverFactory {
    public LiquidSolver create() {
        Global.ToggleWarningMessages(true);
        Log.open("z3.log");
        Context ctx = new Context();
        return new LiquidSolver(ctx);
    }
}
