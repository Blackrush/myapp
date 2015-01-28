package com.example.myapp;

import java.math.BigDecimal;

/**
 * @author Antoine CHAUVIN INFOB1
 */
public final class Calc {
    private Calc() {}

    public static interface Exp {
        String repr();

        BigDecimal eval();

        Exp reduce();
    }

    public static abstract class BinOpFactory {
        public abstract Exp apply(Exp left, Exp right);

        public String repr(Exp left, Exp right) {
            return apply(left, right).repr();
        }
    }

    public static final Exp Nothing = new Exp() {
        @Override
        public String repr() {
            return "";
        }

        @Override
        public BigDecimal eval() {
            return BigDecimal.ZERO;
        }

        @Override
        public Exp reduce() {
            return this;
        }
    };

    public static class Number implements Exp {

        private final BigDecimal number;

        public Number(BigDecimal number) {
            this.number = number;
        }

        @Override
        public String repr() {
            return number.toString();
        }

        @Override
        public Exp reduce() {
            return this;
        }

        @Override
        public BigDecimal eval() {
            return number;
        }
    }

    public static abstract class BinOp implements Exp {
        private final Exp left, right;
        private final String symbol;

        protected BinOp(Exp left, Exp right, String symbol) {
            this.left = left;
            this.right = right;
            this.symbol = symbol;
        }

        protected abstract BigDecimal eval(BigDecimal left, BigDecimal right);

        @Override
        public String repr() {
            return String.format("(%s %s %s)", left.repr(), symbol, right.repr());
        }

        @Override
        public Exp reduce() {
            return new Number(eval(left.eval(), right.eval()));
        }

        @Override
        public BigDecimal eval() {
            return reduce().eval();
        }
    }

    public static class Add extends BinOp {
        public Add(Exp left, Exp right) {
            super(left, right, "+");
        }

        @Override
        protected BigDecimal eval(BigDecimal left, BigDecimal right) {
            return left.add(right);
        }
    }

    public static class Sub extends BinOp {
        public Sub(Exp left, Exp right) {
            super(left, right, "-");
        }

        @Override
        protected BigDecimal eval(BigDecimal left, BigDecimal right) {
            return left.subtract(right);
        }
    }

    public static class Mul extends BinOp {
        public Mul(Exp left, Exp right) {
            super(left, right, "x");
        }

        @Override
        protected BigDecimal eval(BigDecimal left, BigDecimal right) {
            return left.multiply(right);
        }
    }

    public static class Div extends BinOp {
        public Div(Exp left, Exp right) {
            super(left, right, "/");
        }

        @Override
        protected BigDecimal eval(BigDecimal left, BigDecimal right) {
            return left.divide(right);
        }
    }

    public static class Rem extends BinOp {
        public Rem(Exp left, Exp right) {
            super(left, right, "%");
        }

        @Override
        protected BigDecimal eval(BigDecimal left, BigDecimal right) {
            return left.remainder(right);
        }
    }

    public static Exp number(BigDecimal i) {
        return new Number(i);
    }

    public static final BinOpFactory LeftIdentity = new BinOpFactory() {
        @Override
        public Exp apply(Exp left, Exp right) {
            return left;
        }
    };

    public static final BinOpFactory Add = new BinOpFactory() {
        @Override
        public Exp apply(Exp left, Exp right) {
            return new Add(left, right);
        }
    };
    public static final BinOpFactory Sub = new BinOpFactory() {
        @Override
        public Exp apply(Exp left, Exp right) {
            return new Sub(left, right);
        }
    };
    public static final BinOpFactory Mul = new BinOpFactory() {
        @Override
        public Exp apply(Exp left, Exp right) {
            return new Mul(left, right);
        }
    };
    public static final BinOpFactory Div = new BinOpFactory() {
        @Override
        public Exp apply(Exp left, Exp right) {
            return new Div(left, right);
        }
    };
    public static final BinOpFactory Rem = new BinOpFactory() {
        @Override
        public Exp apply(Exp left, Exp right) {
            return new Rem(left, right);
        }
    };
}
