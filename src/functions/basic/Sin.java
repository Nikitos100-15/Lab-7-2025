package functions.basic;

import functions.basic.TrigonometricFunction;

//cинус
public class Sin extends TrigonometricFunction {
    public double getFunctionValue(double x) {
        return Math.sin(x);
    }
}