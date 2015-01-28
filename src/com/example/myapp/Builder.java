package com.example.myapp;

import java.math.BigDecimal;

/**
 * @author Antoine CHAUVIN INFOB1
 */
public class Builder {
    private BigDecimal res = BigDecimal.ZERO;
    private boolean point = false;
    private int pointIndex = 1;

    public void push(int sigil) {
        if (point) {
            int place = pointIndex++;
            BigDecimal toAdd = BigDecimal.valueOf(sigil);
            for (int i = 0; i < place; i++) {
                toAdd = toAdd.divide(BigDecimal.TEN);
            }
            res = res.add(toAdd);
        } else {
            res = res
                .multiply(BigDecimal.TEN)
                .add(BigDecimal.valueOf(sigil));
        }
    }

    public void setPoint() {
        point = true;
    }

    public Calc.Exp build() {
        return Calc.number(res);
    }

    public void reset() {
        this.res = BigDecimal.ZERO;
        this.point = false;
        this.pointIndex = 1;
    }
}
