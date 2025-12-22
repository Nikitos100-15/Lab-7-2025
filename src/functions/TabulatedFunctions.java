package functions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import functions.ArrayTabulatedFunction;
import functions.Function;
import functions.TabulatedFunction;

import java.io.*;

import java.util.StringTokenizer;
public class TabulatedFunctions {
    // статическая фабрика
    private static TabulatedFunctionFactory factory =
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // метод для смены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        factory = newFactory;
    }
    // метод создания функций теперь используют фабрику
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    //  запрещаем создание объектов
    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создавать объекты класса TabulatedFunctions");
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        // проверки параметров
        if (function == null) {
            throw new IllegalArgumentException("функция не может быть null");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount должен быть >= 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("leftX должен быть < rightX");
        }
        // проверка, что отрезок в области определения функции
        if (leftX < function.getLeftDomainBorder() ||  rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                    "Отрезок табулирования [" + leftX + ", " + rightX + "] " +  "выходит за область определения функции [" + function.getLeftDomainBorder() + ", " +  function.getRightDomainBorder() + "]"
            );
        }

        // создаем массивы для точек
        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];

        // ищем шаг
        double step = (rightX - leftX) / (pointsCount - 1);

        // заполняем массивы
        for (int i = 0; i < pointsCount; i++) {
            xValues[i] = leftX + i * step;
            yValues[i] = function.getFunctionValue(xValues[i]);
        }
        // возвращаем табулированную фабричную (изменено) функцию
        return factory.createTabulatedFunction(leftX, rightX, yValues);
    }
        // 7 ЗАДАНИЕ МЕТОДЫ ВВОДА/ВЫВОДА

        public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out)
                throws IOException {
            // try-with-resources автоматически закроет поток
            try (DataOutputStream dos = new DataOutputStream(out)) {
                // записываем количество точек
                int pointsCount = function.getPointsCount();
                dos.writeInt(pointsCount);

                // записываем координаты всех точек
                for (int i = 0; i < pointsCount; i++) {
                    dos.writeDouble(function.getPointX(i));
                    dos.writeDouble(function.getPointY(i));
                }
            }
        }
        public static TabulatedFunction inputTabulatedFunction(InputStream in)
                throws IOException {
            try (DataInputStream dis = new DataInputStream(in)) {
                // читаем количество точек
                int pointsCount = dis.readInt();

                // также создаем массивыы для точке
                double[] xValues = new double[pointsCount];
                double[] yValues = new double[pointsCount];

                // и также чиитаем координаты всех точек
                for (int i = 0; i < pointsCount; i++) {
                    xValues[i] = dis.readDouble();  // читаем X
                    yValues[i] = dis.readDouble();  // читаем Y
                }
                // Можно выбрать любую реализацию - здесь ArrayTabulatedFunction
                return factory.createTabulatedFunction(xValues[0], xValues[pointsCount-1], yValues);
            }
            // поток  закрывается благодаря try-with-resources
        }

        public static void writeTabulatedFunction(TabulatedFunction function, Writer out)
                throws IOException {
            try (BufferedWriter bw = new BufferedWriter(out)) {
                // 1. Записываем количество точек на отдельной строке
                int pointsCount = function.getPointsCount();
                bw.write(String.valueOf(pointsCount));
                bw.newLine();  // переходим на новую строку

                // записываем каждую точку на отдельной строке
                for (int i = 0; i < pointsCount; i++) {
                    bw.write(function.getPointX(i) + " " + function.getPointY(i));
                    bw.newLine();
                }
            }
        }

        public static TabulatedFunction readTabulatedFunction(Reader in)
                throws IOException {

            try (BufferedReader br = new BufferedReader(in)) {
                // читаем первую строку - количество точек
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Нет данных в потоке");
                }
                int pointsCount = Integer.parseInt(line.trim());

                // создаем массивы для координат
                double[] xValues = new double[pointsCount];
                double[] yValues = new double[pointsCount];

                // читаем строки с координатами точек
                for (int i = 0; i < pointsCount; i++) {
                    line = br.readLine();
                    if (line == null) {
                        throw new IOException("Недостаточно данных: ожидалось " + pointsCount + " точек, получено " + i);
                    }
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    if (tokenizer.countTokens() < 2) {
                        throw new IOException("Неверный формат строки: " + line);
                    }

                    // парсим координаты
                    xValues[i] = Double.parseDouble(tokenizer.nextToken());
                    yValues[i] = Double.parseDouble(tokenizer.nextToken());
                }

                // создаем и возвращаем функцию
                return factory.createTabulatedFunction(xValues[0], xValues[pointsCount-1], yValues);
            }
        }
    // методы рефлексии
    // создание по границам и количеству точек через рефлексию
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    // создание по границам и массиву значений через рефлексию
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    // создание по массиву точек через рефлексию
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            FunctionPoint[] points) {
        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    // метод tabulate с рефлексией (перегрузка)
    public static TabulatedFunction tabulate(
            Class<? extends TabulatedFunction> functionClass,
            Function function, double leftX, double rightX, int pointsCount) {
        // проверки параметров
        if (function == null) {
            throw new IllegalArgumentException("функция не может быть null");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount должен быть >= 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("leftX должен быть < rightX");
        }

        double step = (rightX - leftX) / (pointsCount - 1);
        double[] values = new double[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        // используем рефлексию для создания
        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }
    }
