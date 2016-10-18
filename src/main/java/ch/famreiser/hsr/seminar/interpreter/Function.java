package ch.famreiser.hsr.seminar.interpreter;

import ch.famreiser.hsr.seminar.generated.LambdaCalculusParser;

/**
 * Created by micha on 05.10.16.
 */
public class Function {

    private final String argumentName;
    private final LambdaCalculusParser.ExpressionContext body;
    private final Scope closure;

    public Function(String argumentName, LambdaCalculusParser.ExpressionContext body, Scope closure) {
        this.argumentName = argumentName;
        this.body = body;
        this.closure = closure;
    }
    public LambdaCalculusParser.ExpressionContext getBody() {
        return this.body;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public Scope getClosure() {
        return closure;
    }
}
