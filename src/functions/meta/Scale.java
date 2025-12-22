package functions.meta;

import functions.Function;

public class Scale implements Function {
    // коэффиценты
    private Function f;
    private double scaleX, scaleY;

    public Scale(Function f, double scaleX, double scaleY) {
        this.f = f;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getLeftDomainBorder() {
        // масштабирование области определения
        return f.getLeftDomainBorder() * scaleX;
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder() * scaleX;
    }

    public double getFunctionValue(double x) {
        // обратное масштабирование x, затем масштабирование y
        double originalX = x / scaleX;
        if (originalX < f.getLeftDomainBorder() || originalX > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("x вне области определения"); // исключение
        }
        return f.getFunctionValue(originalX) * scaleY;
    }
}