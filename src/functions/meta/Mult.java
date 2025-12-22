package functions.meta;

import functions.Function;

public class Mult implements Function {
    // аналогичные наши переменные
    private Function f1, f2;
    public Mult(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }
    public double getLeftDomainBorder() {
        // пересечение областей определения
        return Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }
    public double getRightDomainBorder() {
        // пересечение областей определения
        return Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }

    public double getFunctionValue(double x) {
        // проверка, что x в области определения обеих функций
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("x вне области определения");
        }
        // возвращаем уже само произведение
        return f1.getFunctionValue(x) * f2.getFunctionValue(x);
    }
}