package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function outer, inner;
    public Composition(Function outer, Function inner) {
        this.outer = outer;
        this.inner = inner;
    }

    public double getLeftDomainBorder() {
        return inner.getLeftDomainBorder();
    }
    public double getRightDomainBorder() {
        return inner.getRightDomainBorder();
    }
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("x вне области определения");
        }
        double innerValue = inner.getFunctionValue(x);
        // проверка, что innerValue в области определения outer
        if (innerValue < outer.getLeftDomainBorder() || innerValue > outer.getRightDomainBorder()) {
            throw new IllegalArgumentException("значение внутренней функции вне области определения внешней");
        }
        return outer.getFunctionValue(innerValue);
    }
}