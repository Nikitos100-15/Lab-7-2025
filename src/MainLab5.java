import functions.*;
import functions.basic.*;
import java.io.*;

public class MainLab5 {

    public static void printAllPoints(TabulatedFunction func) {
        for (int i = 0; i < func.getPointsCount(); i++) {
            double x = func.getPointX(i);
            double y = func.getPointY(i);
            System.out.println("Точка " + i + ":" + "(" + x + "," + y + ")");
        }
    }

    public static void main(String[] args) {
        System.out.println("5 лаболаторная тест");
        double[] array = {3.0, 4.0, 5.0};
        // toString()
        ArrayTabulatedFunction test = new ArrayTabulatedFunction(0, 2, array);
        LinkedListTabulatedFunction test1 = new LinkedListTabulatedFunction(0, 2, array);
        System.out.println("Array: " + test);
        System.out.println("LinkedList: " + test1);

        // equals()
        ArrayTabulatedFunction test3 = new ArrayTabulatedFunction(0, 2, array);
        System.out.println("Array==Array: " + test.equals(test3));
        System.out.println("Array==List: " + test.equals(test1));

        // hashCode()
        System.out.println("Hash Array: " + test.hashCode());
        System.out.println("Hash Array2: " + test3.hashCode());
        System.out.println("Hash List: " + test1.hashCode());

        // проверка контракта equals/hashCode
        System.out.println("equals/hashCode contract: " + (test.equals(test3) == (test.hashCode() == test3.hashCode())));

        // изменение и проверка изменения хеша
        int hash_before = test.hashCode();
        test.setPointY(1, test.getPointY(1) + 0.003);
        int hash_after = test.hashCode();
        System.out.println("Hash before: " + hash_before);
        System.out.println("Hash after: " + hash_after);
        System.out.println("Hash changed: " + (hash_before != hash_after));

        // clone()
        try {
            ArrayTabulatedFunction arrClone = (ArrayTabulatedFunction) test.clone();
            LinkedListTabulatedFunction listClone = (LinkedListTabulatedFunction) test1.clone();

            // Изменяем оригиналы
            double originalArrY = test.getPointY(0);
            double originalListY = test1.getPointY(0);
            test.setPointY(0, 100);
            test1.setPointY(0, 200);
            System.out.println("Array clone unchanged: " +
                    (Math.abs(arrClone.getPointY(0) - originalArrY) < 0.0001));
            System.out.println("List clone unchanged: " +
                    (Math.abs(listClone.getPointY(0) - originalListY) < 0.0001));

        } catch (CloneNotSupportedException e) {
            System.err.println("клонирование не поддерживается: " + e.getMessage());
        }

        // тесты Externalizable  для класса  LinkedListTabulatedFunction
        System.out.println("тесты сериализации LinkedListTabulatedFunction (Externalizable)");
        LinkedListTabulatedFunction original = new LinkedListTabulatedFunction(0, 3, new double[]{0, 1, 4, 9});
        System.out.println("все точки фунции"); // попросили в  исправлении
        printAllPoints(original); // используем готовый метод
        try {
            // ввыполняем  Externalizable
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            original.writeExternal(oos);
            oos.flush();
            oos.close();
            System.out.println("функция cериализована " + baos.size() + " байт");

            // десериализуем через Externalizable
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            LinkedListTabulatedFunction restored = new LinkedListTabulatedFunction();  // ← создаём пустой
            restored.readExternal(ois);  // ← заполняем через readExternal
            ois.close();

            System.out.println("все точки восстановленной функции");
            printAllPoints(restored);

            // переделал проверку через эпсилон
            final double EPSILON = 1e-10;
            boolean f = true;
            for (int i = 0; i < original.getPointsCount(); i++) {
                double originalX = original.getPointX(i);
                double originalY = original.getPointY(i);
                double restY = restored.getFunctionValue(originalX);
                if (Math.abs(originalY - restY) > EPSILON) {
                    System.out.println("ошибка в точке x=" + originalX + " разница=" + Math.abs(originalY - restY));
                    f = false;
                }
            }

            if (f) {
                System.out.println("функции одинаковы");
            } else {
                System.out.println("функции различаются");
            }

        } catch (Exception e) {
            System.out.println("ошибка сериализации: " + e);
        }


        // 2) тест Serializable для ArrayTabulatedFunction
        System.out.println("тест сериализации ArrayTabulatedFunction");
        ArrayTabulatedFunction arrayOriginal = new ArrayTabulatedFunction(-2, 2, new double[]{3, 1, 0, 1, 4});
        System.out.println("все точки  функции");
        printAllPoints(arrayOriginal);//также выводим через метод
        try {
            // сериализуем
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
            oos2.writeObject(arrayOriginal);
            oos2.close();
            System.out.println("функция сериализована (" + baos2.size() + " байт)");

            // десериализуем
            ByteArrayInputStream bais2 = new ByteArrayInputStream(baos2.toByteArray());
            ObjectInputStream ois2 = new ObjectInputStream(bais2);
            ArrayTabulatedFunction arrayRestored = (ArrayTabulatedFunction) ois2.readObject();
            ois2.close();
            System.out.println("все точки восстановленной функции");
            printAllPoints(arrayRestored);
            final double EPSILON = 1e-10;
            boolean f = true;

            for (int i = 0; i < arrayOriginal.getPointsCount(); i++) {
                double origX = arrayOriginal.getPointX(i);
                double origY = arrayOriginal.getPointY(i);
                double restY = arrayRestored.getFunctionValue(origX);

                if (Math.abs(origY - restY) > EPSILON) {
                    System.out.println("ошибка в точке x=" + origX);
                    f = false;
                }
            }
            if (f) {
                System.out.println("функции одинаковы");
            } else {
                System.out.println("функции различаются");
            }

        } catch (Exception e) {
            System.out.println("ошибка сериализации" + e);
        }
    }
}