package functions.basic;

import functions.Function;

public abstract class TrigonometricFunction implements Function {
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;  // тригонометрические функции определяются
    }
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    // как я понял нужно делать абстрактным чтобы не было ошибки
    public abstract double getFunctionValue(double x);
}