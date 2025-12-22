package functions.basic;
//должен импортировать из Function
import functions.Function;

public class Exp implements Function {
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;  // экспонента (не понял сначала что такое посмотрел в интернете)
    }
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        return Math.exp(x);  // e^x
    }
}