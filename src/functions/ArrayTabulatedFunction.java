package functions;

import java.util.Iterator;
import java.util.NoSuchElementException;

import functions.FunctionPoint;
import functions.FunctionPointIndexOutOfBoundsException;
import functions.InappropriateFunctionPointException;
import functions.TabulatedFunction;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private static final long serialVersionUID = 1L;  // версия для сериализации
    // создаем поле в виде массива
    private FunctionPoint[] points;
    // поле количестов точек
    private int pointsCount;

    // 1 ЗАДАНИЕ 1 ЧАСТЬ
    // в классе ArrayTabulatedFunction добавляем массив чисел
    public ArrayTabulatedFunction(FunctionPoint[] points) {
        // проверка на null
        if (points == null) {
            throw new IllegalArgumentException("массив не может быть 0");
        }

        // проверка количества точек
        if (points.length < 2) {
            throw new IllegalArgumentException("необходимо минимум 2 точки");
        }

        // создание копий
        pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount + 10]; // с запасом

        // копируем первую точку
        this.points[0] = new FunctionPoint(points[0]);

        // копируем остальные и проверяем упорядоченность
        for (int i = 1; i < pointsCount; i++) {
            // проверка на то, что x[i] > x[i-1] чтобы шли по попорядку
            if (points[i].getX() <= points[i-1].getX()) {
                throw new IllegalArgumentException(" нарушение в точках " + (i-1) + " и " + i );
            }
            // инкапсуляция
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    // первый конструктор по заданию
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        // добавляем исключения по 3 заданию
        if (leftX>= rightX)
            throw new IllegalArgumentException(
                    "Левая граница является правой в данном случае."
            );
        if (pointsCount<2)
            throw new IllegalArgumentException(
                    "Количество точек должно быть >= 2."
            );
        this.points = new FunctionPoint[pointsCount]; // создаем объект для массива точек
        this.pointsCount = pointsCount; // инициализируем количество точек
        double step = (rightX - leftX) / (pointsCount - 1); // считаем шаг между точками
        for (int i = 0; i < pointsCount; i++) {
            double x_c = leftX + i * step;// считаем координату x
            points[i] = new FunctionPoint(x_c, 0);// записываем наши точки в объект вида массива
        }
    }
    // второй конструктор по заданию
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        // аналогичные проверки исключений
        if (leftX >= rightX) {
            throw new IllegalArgumentException(
                    "Левая граница является правой в данном случае."
            );
        }
        if (values == null) {
            throw new IllegalArgumentException("Массив  не может быть равен null");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException(
                    "Количество точек должно быть >= 2 "
            );
        }
        this.points = new FunctionPoint[values.length]; // создаем объект для массива точек
        this.pointsCount = values.length; // инициализируем количество точек
        double step = (rightX - leftX) / (values.length - 1); // считаем шаг между точками
        for (int i = 0; i < values.length; i++) {
            double x_c = leftX + i * step; // считаем координату x
            points[i] = new FunctionPoint(x_c, values[i]); // записываем наши точки в объект вида массива
        }
    }

    // метод для возращения самой левой границы области определения
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    // метод для возращения самой правой границы области определения
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    // метод для получения значение функции в точке x, если эта точка лежит в области определения функции
    public double getFunctionValue(double x) {
        // значение эпсилон как константа
        final double EPSILON = 1e-10;

        // проверка на пустоту и на существование(без нее у меня ломалось)
        if (points == null || pointsCount == 0) {
            return Double.NaN;
        }

        // получаем промежутки для проверки границ
        double leftX = points[0].getX();
        double rightX = points[pointsCount - 1].getX();

        // проверка границ с переменной эпсилон
        if ((x < (leftX - EPSILON)) || (x > (rightX + EPSILON))) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(x - points[i].getX()) < EPSILON) {
                return points[i].getY(); // возвращаем соответствующий y (также у меня не было вроде)
            }
        }

        // ищем интервал с учетом эпсилон для интерполяции
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();

            // ищем значения x с учетом эпсилон
            if (x >= x1 - EPSILON && x <= x2 + EPSILON) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return Double.NaN; // возвращаем на всякий случай
    }

    // метод для получения количества точек
    public int getPointsCount() {
        return pointsCount;
    }

    // метод для вброса исключения FunctionPointIndexOutOfBoundsException(идея была взяла с сайта metanit.com)
    private void checkIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(
                    "Значение" + index + " находится вне  заданного диапазона [0, " + (pointsCount - 1) + "]"
            );
        }
    }
    // метод для получения копии точки
    public FunctionPoint getPoint(int index) {
        checkIndex(index);
        return new FunctionPoint(points[index]);
    }
    // измененный методы с исключениями
    // метод для получения значения абциссы
    public double getPointX(int index) {
        checkIndex(index);
        return points[index].getX();
    }

    // метод для получения значения ординаты
    public double getPointY(int index) {
        checkIndex(index);
        return points[index].getY();
    }

    // метод для изменения значения ординаты точки с указанным номером
    public void setPointY(int index, double y) {
        checkIndex(index);
        points[index].setY(y);
    }

    // метод для замены точки на табулированную
    public void setPoint(int index, FunctionPoint point) throws functions.InappropriateFunctionPointException {
        checkIndex(index);
        // проверка на null
        if (point == null) {
            //исключение
            throw new IllegalArgumentException("Точка не может быть null");
        }
        // проверяем, что новый x между соседними точками
        if (index > 0 && point.getX() <= points[index - 1].getX()) {
            throw new functions.InappropriateFunctionPointException(
                    "значение x " + point.getX() + " должен быть > " + points[index - 1].getX()
            );
        }
        if (index < pointsCount - 1 && point.getX() >= points[index + 1].getX()) {
            //исключение
            throw new functions.InappropriateFunctionPointException(
                    "значение x " + point.getX() + " должен быть < " + points[index + 1].getX()
            );
        }

        points[index] = new FunctionPoint(point);  // сохраняем копию
    }

    // метод для замены абциссы точки с указанным номером ( c добавленными исключениями)
    public void setPointX(int index, double x) throws functions.InappropriateFunctionPointException {
        checkIndex(index);

        // проверка, что x между соседними точками
        if (index > 0 && x <= points[index - 1].getX()) {
            //исключение
            throw new functions.InappropriateFunctionPointException(
                    "значение x " + x + " должен быть > " + points[index - 1].getX()
            );
        }
        if (index < pointsCount - 1 && x >= points[index + 1].getX()) {
            throw new functions.InappropriateFunctionPointException(
                    "значение x " + x + " должен быть < " + points[index + 1].getX()
            );
        }

        points[index].setX(x);
    }

    public void deletePoint(int index) {
        checkIndex(index);

        // проверка, что останется минимум 2 точки
        if (pointsCount <= 2) {
            throw new IllegalStateException( "Ошибка! Должно остаться минимум 2 точки" );
        }

        for (int i = index; i < pointsCount - 1; i++) {
            points[i] = points[i + 1];
        }
        pointsCount--;
    }

    public void addPoint(FunctionPoint point) throws functions.InappropriateFunctionPointException {
        // проверка на null
        if (point == null) {
            throw new IllegalArgumentException("Точка не может быть null");
        }

        // проверяем, что такой x еще не существует
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(points[i].getX() - point.getX()) < 1e-10) {
                throw new InappropriateFunctionPointException(
                        "Точка с X=" + point.getX() + " уже существует"
                );
            }
        }
        // проверяем, заполнен ли массив
        if (pointsCount >= points.length) {
            // увеличиваем массив в 2 раза
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }
        // ищем позицию для вставки x
        int pos = 0;
        while (pos < pointsCount && points[pos].getX() < point.getX()) {
            pos++;
        }
        // сдвигаем точки вправо, чтобы освободить место для новой точки
        System.arraycopy(points, pos, points, pos + 1, pointsCount - pos);
        // вставляем новую точку
        points[pos] = new FunctionPoint(point);
        pointsCount++;
    }

    // 5 задание 2 пункт
    public String toString() {
        String result = "{";
        // перебираем все точки массива
        for (int i = 0; i < pointsCount; i++) {
            // добавляем строковое представление текущей точки
            result += points[i]; // в этом случае уже   автоматически вызывает points[i].toString()
            if (i != pointsCount - 1) result += ", ";  // если это не последняя точка, добавляем запятую и пробел
        }
        //записываем последнюю скобку
        result += "}";
        return result; // возвращаем итоговую строку
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        // проверка на табулированню функцию
        if (!(o instanceof TabulatedFunction)) return false;
        TabulatedFunction that = (TabulatedFunction) o;
        // количество точек
        if (this.getPointsCount() != that.getPointsCount()) return false;
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction other = (ArrayTabulatedFunction) o;
            // прямой доступ к массиву точек
            for (int i = 0; i < pointsCount; i++) {
                if (!this.points[i].equals(other.points[i])) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < pointsCount; i++) {
                if (!this.getPoint(i).equals(that.getPoint(i))) {
                    return false;
                }
            }
        }
        return true;
    }
    public int hashCode() {
        int hash = pointsCount;  // начинаем с количества точек
        for (int i = 0; i < pointsCount; i++) {
            // XOR с хэшкодом каждой точки
            hash ^= points[i].hashCode();
        }
        return hash; }

    public Object clone() throws CloneNotSupportedException {
        // создаём копии всех точек
        FunctionPoint[] pointsCopy = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            pointsCopy[i] = new FunctionPoint(points[i].getX(), points[i].getY());
        }
        // создаём новую функцию
        return new ArrayTabulatedFunction(pointsCopy);
    }
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int cur_index = 0;
            public boolean hasNext() {
                return cur_index < pointsCount;
            }
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Больше точек нет в табулированной функции");
                }
                return new FunctionPoint(points[cur_index++]);
            }

            public void remove() {
                throw new UnsupportedOperationException("Удаление не поддерживается для табулированных функций");
            }
        };
    }
    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

        // метод для создания по границам и количеству точек
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }
        // метод для создания по границам и массиву значений y
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }
        //метод для создания по массиву точек
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    }
}