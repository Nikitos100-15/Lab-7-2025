package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base;  // основание логарифма

    public Log(double base) {
        // проверка логарифма(сам не додумался)
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("основание логарифма >0 и не равно 1 должно быть");
        }
        this.base = base;
    }
    public double getBase() {
        return base;
    }
    public double getLeftDomainBorder() {
        return 0;  // логарифм определен при x > 0
    }
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    public double getFunctionValue(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("Логарифм определен только при x > 0");
        }
        return Math.log(x) / Math.log(base);  // logₐ(x) = ln(x)/ln(a)
    }
}