import functions.*;
import functions.basic.*;
import java.io.*;

public class MainLab7 {  // ← Имя класса должно совпадать с именем файла

    public static void main(String[] args) {
        // 7 лаболаторная работа 1 задание тест
        System.out.println("7 лаболаторная работа 1 задание тест");
        // ArrayTabulatedFunction
        TabulatedFunction f1 = new ArrayTabulatedFunction(0, 10, new double[]{0, 1, 4, 9, 16, 25});
        System.out.println("ArrayTabulatedFunction:");
        for (FunctionPoint p : f1) {
            System.out.println(p);
        }
        // LinkedListTabulatedFunction
        FunctionPoint[] points = {new FunctionPoint(0, 0), new FunctionPoint(2, 4), new FunctionPoint(4, 16)};
        TabulatedFunction f2 = new LinkedListTabulatedFunction(points);
        System.out.println("LinkedListTabulatedFunction:");
        for (FunctionPoint p : f2) {
            System.out.println(p);
        }

        // тесты фабрики
        Function f_1 = new Cos();
        TabulatedFunction tf;

        tf = TabulatedFunctions.tabulate(f_1, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f_1, 0, Math.PI, 11); // переопределяем существующую tf
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(
                new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f_1, 0, Math.PI, 11); // снова переопределяем tf
        System.out.println(tf.getClass());

        // тесты рефлексии
        TabulatedFunction f_2;
        f_2 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f_2.getClass());
        System.out.println(f_2);
        f_2 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f_2.getClass());
        System.out.println(f_2);
        f_2 = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)});
        System.out.println(f_2.getClass());
        System.out.println(f_2);
        f_2 = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f_2.getClass());
        System.out.println(f_2);
    }
}