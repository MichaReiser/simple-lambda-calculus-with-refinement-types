package ch.famreiser.hsr.seminar.typechecker.liquid;

import com.google.common.collect.Iterables;

/**
 * Binary expression constraint
 */
public class BinaryConstraint implements Constraint {
    private final Constraint left;
    private final Constraint right;
    private final Operation operation;

    public Operation getOperation() {
        return operation;
    }

    public Constraint getLeft() {
        return left;
    }

    public Constraint getRight() {
        return right;
    }

    public static enum Operation {
        EQUAL("="),
        LESS_THAN("<"),
        GREATER_THAN(">"),
        LESS_OR_EQUAL_THAN("<="),
        GREATER_OR_EQUAL_THAN(">="),
        ADDITION("+"),
        SUBTRACTION("-"),
        DIVISION("/"),
        MULTIPLICATION("*"), IMPLIES("\u21D2"), EQUIVALENCE("<=>");

        private final String sign;

        public static Operation fromString(String sign) {
            switch (sign) {
                case "=":
                    return EQUAL;
                case "<":
                    return LESS_THAN;
                case ">":
                    return GREATER_THAN;
                case "<=":
                    return LESS_OR_EQUAL_THAN;
                case ">=":
                    return GREATER_OR_EQUAL_THAN;
                case "+":
                    return ADDITION;
                case "-":
                    return SUBTRACTION;
                case "/":
                    return DIVISION;
                case "*":
                    return MULTIPLICATION;
                case "=>":
                    return IMPLIES;
                default:
                    throw new IllegalArgumentException("Unknown operation " + sign);
            }
        }

        Operation(String sign) {
            this.sign = sign;
        }

        @Override
        public String toString() {
            return sign;
        }
    }

    public BinaryConstraint(Constraint left, Operation operation, Constraint right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    @Override
    public Iterable<LiquidTypeVariable> getLiquidVariables() {
        return Iterables.concat(left.getLiquidVariables(), right.getLiquidVariables());
    }

    @Override
    public Constraint substitute(Constraint toSubstitute, Constraint with) {
        return new BinaryConstraint(left.substitute(toSubstitute, with), operation, right.substitute(toSubstitute, with));
    }

    @Override
    public String toString() {
        return left + " " + this.operation + " " + right;
    }
}
