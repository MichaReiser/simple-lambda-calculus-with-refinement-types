package ch.famreiser.hsr.seminar.typechecker.liquid.solver;


import ch.famreiser.hsr.seminar.typechecker.liquid.*;
import com.google.common.collect.Lists;
import com.microsoft.z3.*;

import java.util.List;

public class ConstraintsTranslator {

    private final Solver solver;
    private final Context context;

    public ConstraintsTranslator(Context context, Solver solver) {
        this.context = context;
        this.solver = solver;
    }


}
