package ch.famreiser.hsr.seminar.typechecker.liquid.constraints;

/**
 * Binary expression constraint
 */
public class BinaryConstraint implements Constraint {
    private final Constraint left;
    private final Constraint right;
    private final Operator operator;

    public enum Operator {
        EQUAL("==", true),
        NOT_EQUAL_TO("/=", true),
        LESS_THAN("<", true),
        GREATER_THAN(">", true),
        LESS_OR_EQUAL_THAN("<=", true),
        GREATER_OR_EQUAL_THAN(">=", true),
        ADDITION("+"),
        SUBTRACTION("-"),
        DIVISION("/"),
        MULTIPLICATION("*");

        private final String sign;
        private final boolean boolExpr;

        public static Operator fromString(String sign) {
            for (Operator operator : Operator.values()) {
                if (operator.sign.equals(sign)) {
                    return operator;
                }
            }

            throw new IllegalArgumentException("Unknown operation " + sign);
        }

        Operator(String sign) {
            this(sign, false);
        }

        Operator(String sign, boolean boolExpr) {
            this.sign = sign; this.boolExpr = boolExpr;
        }

        public String getSign() {
            return sign;
        }

        public boolean isBoolExpr() {
            return boolExpr;
        }

        @Override
        public String toString() {
            return sign;
        }
    }

    public BinaryConstraint(Constraint left, Operator operator, Constraint right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Operator getOperator() {
        return operator;
    }

    public Constraint getLeft() {
        return left;
    }

    public Constraint getRight() {
        return right;
    }

    @Override
    public <T> T accept(ConstraintVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public BinaryConstraint substitute(Constraint to, Constraint with) {
        return new BinaryConstraint(this.left.substitute(to, with), operator, this.right.substitute(to, with));
    }

    @Override
    public String toString() {
        return left + " " + this.operator + " " + right;
    }
}
