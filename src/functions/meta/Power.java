package functions.meta;

import functions.Function;

public class Power implements Function {
    // значение и число
    private Function f;
    private double power;

    public Power(Function f, double power) {
        this.f = f;
        this.power = power;
    }
    public double getLeftDomainBorder() {
        // пересечение областей определения
        return f.getLeftDomainBorder();
    }
    public double getRightDomainBorder() {
        // пересечение областей определения
        return f.getRightDomainBorder();
    }
    public double getFunctionValue(double x) {
        // проверка, что x в области определения обеих функций
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("x вне области определения"); // исключение
        }
        return Math.pow(f.getFunctionValue(x), power); //возвращаем число в степени
    }
}