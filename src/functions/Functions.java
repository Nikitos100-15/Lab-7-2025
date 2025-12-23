package functions;

import functions.meta.*;
public class Functions {

    // проверка одной функции(взял такую же идею, что использовал с интернета, чтобы не и так усугублять код)
    private static void checkFunction(Function f) {
        if (f == null) {
            throw new IllegalArgumentException("функция не может быть null");
        }
    }

    // проверка двух функций
    private static void checkFunctions(Function f1, Function f2) {
        checkFunction(f1);
        checkFunction(f2);
    }

    // проверка коэффициентов масштабирования
    private static void checkScaleCoefficients(double scaleX, double scaleY) {
        if (scaleX == 0 || scaleY == 0) {
            throw new IllegalArgumentException("коэффициенты масштабирования не могут быть равны 0");
        }
    }

    // основные методы, используются методы из пакета functions.meta
    public static Function shift(Function f, double shiftX, double shiftY) {
        checkFunction(f);  // ← Теперь одна строка!
        return new Shift(f, shiftX, shiftY);
    }
    public static Function scale(Function f, double scaleX, double scaleY) {
        checkFunction(f);
        checkScaleCoefficients(scaleX, scaleY);
        return new Scale(f, scaleX, scaleY);
    }
    public static Function power(Function f, double power) {
        checkFunction(f);
        return new Power(f, power);
    }
    public static Function sum(Function f1, Function f2) {
        checkFunctions(f1, f2);
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2) {
        checkFunctions(f1, f2);
        return new Mult(f1, f2);
    }
    public static Function composition(Function f1, Function f2) {
        checkFunctions(f1, f2);
        return new Composition(f1, f2);
    }
    public static double Integral(Function function, double left, double right, double step) {
        // не выходим ли за границы области определения
        if (left < function.getLeftDomainBorder() || right > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы области определения функции");
        }
        // вычисление интеграла методом трапеций
        double integral = 0.0;
        double need_x = left;
        double need_y = function.getFunctionValue(need_x);
        // идем пока не дойдем до правой границы
        while (need_x < right) {
            // следующая точка
            double next_x = need_x + step;
            if (next_x > right) {
                next_x = right;
            }
            // значение функции в следующей точке
            double next_y = function.getFunctionValue(next_x);
            // площадь трапеции: (основание1 + основание2) * высота / 2
            double trapezoid = (need_y + next_y) * (next_x - need_x) / 2.0;
            integral += trapezoid;
            // переходим к следующему отрезку
            need_x = next_x;
            need_y = next_y;
        }
        return integral;
    }
}
