package functions;

import java.util.Iterator;
import java.util.NoSuchElementException;

import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable, Externalizable {
    private static final long serialVersionUID = 2L;  // версия для сериализации

    private class FunctionNode implements Serializable {
        private static final long serialVersionUID = 3L;
        // информационное поле для хранения данных типа FunctionPoint
        private FunctionPoint point;
        // поля для хранения ссылок на предыдущий и следующий элемент
        private FunctionNode prev;
        private FunctionNode next;

        // конструкторы
        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }

        public FunctionNode() {
            this.point = null;
        }

        // геттеры и сеттеры
        public FunctionPoint getPoint() { return point; }
        public void setPoint(FunctionPoint point) { this.point = point; }
        public FunctionNode getPrev() { return prev; }
        public void setPrev(FunctionNode prev) { this.prev = prev; }
        public FunctionNode getNext() { return next; }
        public void setNext(FunctionNode next) { this.next = next; }
    }

    // 1 ЗАДАНИЕ 2 ЧАСТЬ
    // в классе LinkedListTabulatedFunction  конструктор
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        // проверка на null
        if (points == null) {
            throw new IllegalArgumentException("массив не может быть 0");
        }
        // проверка количества точек
        if (points.length < 2) {
            throw new IllegalArgumentException("необходимо минимум 2 точки");
        }
        // проверка упорядоченности
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i-1].getX()) {
                throw new IllegalArgumentException("нарушение в точках " + (i-1) + " и " + i);
            }
        }
        // список из первой пустой головы
        head = new FunctionNode(); // голова списка
        head.setNext(head);
        head.setPrev(head);
        FunctionNode current = head;
        // добавляем все точки в список
        for (FunctionPoint point : points) {
            FunctionNode newNode = new FunctionNode(point);
            // добавляем в конец списка
            newNode.setPrev(current);
            newNode.setNext(head);
            current.setNext(newNode);
            head.setPrev(newNode);
            current = newNode;
            pointsCount++;
        }
    }

    // конструктор без параметров для Externalizable (ДОБАВЛЕН)
    public LinkedListTabulatedFunction() {
        head = new FunctionNode();
        head.setNext(head);
        head.setPrev(head);
        pointsCount = 0;
    }

    // голова списка
    private FunctionNode head;

    // количество точек
    private int pointsCount;
    private FunctionNode lastAccessedNode;  // последний доступный узел
    private int lastAccessedIndex;          // индекс последнего доступного узла

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        // проверка параметров
        if (leftX >= rightX) {
            throw new IllegalArgumentException(
                    "Левая граница (" + leftX + ") должна быть < правой (" + rightX + ")"
            );
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException(
                    "Количество точек должно быть >= 2"
            );
        }
        // инициализация списка: создаем голову
        head = new FunctionNode(null);
        head.setNext(head);  // цикличность: ссылается сам на себя
        head.setPrev(head);
        this.pointsCount = 0;

        // создаем точки с равномерным распределением
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            // используем метод addNodeToTail()
            addNodeToTail().setPoint(new FunctionPoint(x, 0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        // аналогичная проверка
        if (values == null) {
            throw new IllegalArgumentException("Массив  не может быть null");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException(
                    "Количество точек должно быть >= 2"
            );
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException(
                    "Левая граница (" + leftX + ") должна быть < правой (" + rightX + ")"
            );
        }

        // используем первый конструктор для создания структуры списка
        this(leftX, rightX, values.length);

        // заполняем значения y(посмотрел как сделать в интернете)
        FunctionNode current = head.getNext();
        for (int i = 0; i < values.length; i++) {
            double x = current.getPoint().getX();
            current.setPoint(new FunctionPoint(x, values[i]));
            current = current.getNext();
        }
    }

    public double getLeftDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.getNext().getPoint().getX();  // первая точка
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.getPrev().getPoint().getX();  // последняя точка
    }

    public double getFunctionValue(double x) {
        final double EPSILON = 1e-10;

        if (pointsCount == 0) return Double.NaN;

        double leftX = getLeftDomainBorder();
        double rightX = getRightDomainBorder();

        // проверка границ (с учетом EPSILON)
        if (x < leftX - EPSILON || x > rightX + EPSILON) {
            return Double.NaN;
        }

        // поиск точного совпадения x
        FunctionNode current = head.getNext();
        while (current != head) {
            if (Math.abs(current.getPoint().getX() - x) < EPSILON) {
                return current.getPoint().getY();
            }
            current = current.getNext();
        }

        // pobcr интервала для линейной интерполяции
        FunctionNode node1 = head.getNext();
        FunctionNode node2 = node1.getNext();

        while (node2 != head) {
            if (x >= node1.getPoint().getX() - EPSILON &&
                    x <= node2.getPoint().getX() + EPSILON) {

                double x1 = node1.getPoint().getX();
                double y1 = node1.getPoint().getY();
                double x2 = node2.getPoint().getX();
                double y2 = node2.getPoint().getY();

                //  интерполяция
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            node1 = node2;
            node2 = node2.getNext();
        }

        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        // getNodeByIndex() сам проверяет индекс и бросает исключение
        return new FunctionPoint(getNodeByIndex(index).getPoint());
    }

    public double getPointX(int index) {
        return getNodeByIndex(index).getPoint().getX();
    }

    public double getPointY(int index) {
        return getNodeByIndex(index).getPoint().getY();
    }

    public void setPointY(int index, double y) {
        FunctionNode node = getNodeByIndex(index);
        double x = node.getPoint().getX();
        node.setPoint(new FunctionPoint(x, y));
    }

    public void setPoint(int index, FunctionPoint point) throws functions.InappropriateFunctionPointException {
        // проверка индекса (внутри getNodeByIndex)
        FunctionNode node = getNodeByIndex(index);

        // проверка на null
        if (point == null) {
            throw new IllegalArgumentException("Точка не может быть null");
        }

        // проверка порядка x с соседними точками
        if (index > 0) {
            FunctionNode prevNode = node.getPrev();
            if (point.getX() <= prevNode.getPoint().getX()) {
                throw new functions.InappropriateFunctionPointException(
                        "x=" + point.getX() + " должен быть > " + prevNode.getPoint().getX()
                );
            }
        }
        if (index < pointsCount - 1) {
            FunctionNode nextNode = node.getNext();
            if (point.getX() >= nextNode.getPoint().getX()) {
                throw new functions.InappropriateFunctionPointException(
                        "x=" + point.getX() + " должен быть < " + nextNode.getPoint().getX()
                );
            }
        }

        node.setPoint(new FunctionPoint(point));
    }

    public void setPointX(int index, double x) throws functions.InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double oldY = node.getPoint().getY();

        // проверка порядка X (как в setPoint)
        if (index > 0) {
            FunctionNode prevNode = node.getPrev();
            if (x <= prevNode.getPoint().getX()) {
                throw new functions.InappropriateFunctionPointException(
                        "x=" + x + " должен быть > " + prevNode.getPoint().getX()
                );
            }
        }
        if (index < pointsCount - 1) {
            FunctionNode nextNode = node.getNext();
            if (x >= nextNode.getPoint().getX()) {
                throw new functions.InappropriateFunctionPointException(
                        "x=" + x + " должен быть < " + nextNode.getPoint().getX()
                );
            }
        }

        node.setPoint(new FunctionPoint(x, oldY));
    }

    public void deletePoint(int index) {
        // проверка минимального количества точек
        if (pointsCount <= 2) {
            throw new IllegalStateException(
                    "Нельзя удалить точку! Должно остаться минимум 2 точки"
            );
        }

        // удаление узла
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws functions.InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Точка не может быть null");
        }

        double newX = point.getX();

        // если список пустой
        if (head.getNext() == head) { // пустой циклический список
            FunctionNode newNode = new FunctionNode(point);
            newNode.setNext(head);
            newNode.setPrev(head);
            head.setNext(newNode);
            head.setPrev(newNode);
            pointsCount = 1;
            return;
        }

        // поиск позиции для вставки
        FunctionNode current = head.getNext();
        FunctionNode prevNode = head;
        int index = 0;

        while (current != head) {
            double currentX = current.getPoint().getX();

            // проверка на дубликат
            if (Math.abs(currentX - newX) < 1e-10) {
                throw new functions.InappropriateFunctionPointException(
                        "Точка с x=" + newX + " уже существует"
                );
            }

            // нашли позицию
            if (newX < currentX) {
                break;
            }

            prevNode = current;
            current = current.getNext();
            index++;
        }

        FunctionNode newNode = new FunctionNode(point);
        newNode.setPrev(prevNode);
        newNode.setNext(current);
        prevNode.setNext(newNode);
        current.setPrev(newNode);

        pointsCount++;
    }

    private FunctionNode getNodeByIndex(int index) {
        // проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new functions.FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

        if (lastAccessedNode != null && lastAccessedIndex == index) {
            return lastAccessedNode;
        }

        FunctionNode node;
        if (index < pointsCount / 2) {
            // ищем с начала
            node = head.getNext();  // первый значащий элемент
            for (int i = 0; i < index; i++) {
                node = node.getNext();
            }
        } else {
            node = head.getPrev();  // последний значащий элемент
            for (int i = pointsCount - 1; i > index; i--) {
                node = node.getPrev();
            }
        }

        lastAccessedNode = node;
        lastAccessedIndex = index;

        return node;
    }

    private FunctionNode addNodeToTail() {
        // создаем новый узел
        FunctionNode newNode = new FunctionNode(null);
        newNode.setNext(head);
        newNode.setPrev(head.getPrev());

        head.getPrev().setNext(newNode);
        head.setPrev(newNode);

        pointsCount++;  // увеличиваем счетчик

        return newNode;
    }

    private FunctionNode addNodeByIndex(int index) {
        // проверка индекса
        if (index < 0 || index > pointsCount) {
            throw new functions.FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

        // eсли добавляем в конец - используем существующий метод
        if (index == pointsCount) {
            return addNodeToTail();
        }

        FunctionNode currentNode = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(null);
        // устанавливаем связи нового узла
        newNode.setNext(currentNode);
        newNode.setPrev(currentNode.getPrev());
        // обновляем связи соседних узлов
        currentNode.getPrev().setNext(newNode);
        currentNode.setPrev(newNode);

        pointsCount++;  // Увеличиваем счетчик

        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        // проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

        // находим узел для удаления
        FunctionNode nodeToDelete = getNodeByIndex(index);

        nodeToDelete.getPrev().setNext(nodeToDelete.getNext());
        nodeToDelete.getNext().setPrev(nodeToDelete.getPrev());

        pointsCount--;  // уменьшаем счетчик

        if (lastAccessedNode == nodeToDelete) {
            lastAccessedNode = null;
            lastAccessedIndex = -1;
        } else if (lastAccessedIndex > index) {
            lastAccessedIndex--;
        }

        return nodeToDelete;
    }

    // методы для Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount); // количество точек
        // записываем все точки
        FunctionNode current = head.getNext();
        while (current != head) {
            out.writeDouble(current.getPoint().getX());
            out.writeDouble(current.getPoint().getY());
            current = current.getNext();
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // очищаем существующий список
        head = new FunctionNode();
        head.setNext(head);
        head.setPrev(head);
        pointsCount = 0;
        int count = in.readInt();
        // читаем и добавляем точки
        for (int i = 0; i < count; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            try {
                addPoint(new FunctionPoint(x, y));
            } catch (InappropriateFunctionPointException e) {
                throw new IOException("ошибка при десериализации точки: " + e.getMessage());
            }
        }
    }
    // 5 лаболаторная 3 пункт (переделанные для этого класса из Array)
    public String toString() {
        String result = "{";
        // начинаем с первого узла после головы
        FunctionNode current = head.getNext();
        int count = 0;
        // перебираем все узлы списка пока не вернёмся к голове
        while (current != head) {
            result += current.getPoint(); // автоматически вызывается toString()
            count++;
            // если это не последняя точка, добавляем запятую и пробел
            if (count < pointsCount)
                result += ", ";
            // переходим к следующему узлу
            current = current.getNext();
        }

        // записываем последнюю скобку
        result += "}";
        return result; // возвращаем итоговую строку
    }

    public int hashCode() {
        int hash = pointsCount;  // начало  с количества точек
        // ободим все узлы списка
        FunctionNode curent = head.getNext();
        while (curent != head) {
            hash ^= curent.getPoint().hashCode(); // иксор с хэшкодом каждой точки
            curent = curent.getNext();
        }
        return hash;
    }
    public Object clone() throws CloneNotSupportedException {
        // создаём массив для копий точек
        FunctionPoint[] pointsCopy = new FunctionPoint[pointsCount];
        // копируем все точки из списка
        FunctionNode massiv = head.getNext();
        for (int i = 0; i < pointsCount; i++) {
            pointsCopy[i] = new FunctionPoint(massiv.getPoint().getX(), massiv.getPoint().getY());massiv = massiv.getNext();
        }
        // создаём новую функцию через конструктор с массивом
        return new LinkedListTabulatedFunction(pointsCopy);
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        // проверка на табулированную функцию
        if (!(o instanceof TabulatedFunction))
            return false;
        TabulatedFunction o1 = (TabulatedFunction) o;
        // проверяем количество точек
        if (this.getPointsCount() != o1.getPointsCount()) {
            return false;
        }
        if (o instanceof LinkedListTabulatedFunction) {
            // оптимизация: прямой обход двух списков
            LinkedListTabulatedFunction another_sheet = (LinkedListTabulatedFunction) o;
            FunctionNode node1 = this.head.getNext();
            FunctionNode node2 = another_sheet.head.getNext();
            while (node1 != head) {
                if (!node1.getPoint().equals(node2.getPoint())) {//делегирванеи
                    return false;
                }
                node1 = node1.getNext();
                node2 = node2.getNext();
            }
        }
        else {
            // общий случай: через getPoint()
            for (int i = 0; i < pointsCount; i++) {
                if (!this.getPoint(i).equals(o1.getPoint(i))) { // делегирование
                    return false;
                }
            }
        }
        return true; // возвращаем true в случае если ничего не сработает
    }
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode curr_node = head.getNext();  // первый реальный узел
            private int index = 0;
            public boolean hasNext() {
                return index < pointsCount;
            }
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Больше точек нет в табулированной функции");
                }
                // прямой доступ к внутренней структуре!
                FunctionPoint point = new FunctionPoint(curr_node.getPoint());
                curr_node = curr_node.getNext();
                index++;
                return point;
            }
            public void remove() {
                throw new UnsupportedOperationException("Удаление не поддерживается для табулированных функций");
            }
        };
    }
    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {

        // метод для создания по границам и количеству точек
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }
        // метод для создания по границам и массиву значений y
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }
        //метод для создания по массиву точек
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }
    }

}