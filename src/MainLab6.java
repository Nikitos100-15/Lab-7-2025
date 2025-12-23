import functions.*;
import functions.basic.*;
import threads.*;
import java.io.*;
import java.util.concurrent.Semaphore;

public class MainLab6 {

    public static void complicatedThreads() {
        // создаем таск
        Task task = new Task(100); // 100 заданий
        // создаем семафор с 1 разрешением
        Semaphore semaphore = new Semaphore(1);
        // создаем потоки
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);
        //generator.setPriority(Thread.MIN_PRIORITY);
        //integrator.setPriority(Thread.MAX_PRIORITY);
        generator.start();
        integrator.start();
        try {
            Thread.sleep(50); // ждем 50 мс по заданию
            generator.interrupt();
            integrator.interrupt();

            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            System.out.println("Основной поток прерван");
        }
        System.out.println("Оба потока завершились");
    }

    // потоки
    public static void simpleThreads() {
        Task task = new Task(100);
        // создаем потоки
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));
        // generatorThread.setPriority(Thread.MAX_PRIORITY);
        // integratorThread.setPriority(Thread.MIN_PRIORITY);

        // запускаем потоки
        generatorThread.start();
        integratorThread.start();
        // ждем завершения
        try {
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            System.out.println("Потоки прерваны");
        }
        System.out.println("Оба потока завершили работу");
    }

    // последовательная версия программы
    public static void nonThread() {
        //объект Task с 100 заданиями
        Task task = new Task(100);
        for (int i = 0; i < task.getTasksCount(); i++) {
            // логарифм со случайным основанием от 1 до 10
            double log = 1 + Math.random() * 9;
            task.setFunction(new Log(log));
            // левая граница: 0 - 100
            task.setLeftX(Math.random() * 100);
            // правая граница: 100 - 200
            task.setRightX(100 + Math.random() * 100);
            // шаг 0 1
            task.setStep(Math.random());
            // вывод
            System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep());
            try {
                // вычисление интеграла
                double integral = Functions.Integral(task.getFunction(), task.getLeftX(), task.getRightX(), task.getStep());
                System.out.println("Result " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep() + " " + integral);

            } catch (Exception e) {
                System.out.println("ошибка интегрирования: " + e.getMessage());
            }
        }
        System.out.println("Выполнено " + task.getTasksCount() + " заданий.");
    }

    public static void main(String[] args) {
        // 6 лаболаторная 1 задание, тестирование интегрирования функции
        System.out.println("проверка интегрирования");
        // 1. экспонента e^x
        Function expFunc = new Function() {
            public double getLeftDomainBorder() {
                return Double.NEGATIVE_INFINITY;
            }

            public double getRightDomainBorder() {
                return Double.POSITIVE_INFINITY;
            }

            public double getFunctionValue(double x) {
                return Math.exp(x);
            } // e^x
        };

        // теоретическое значение: ∫e^x dx от 0 до 1 = e - 1
        double theoretical = Math.E - 1;
        System.out.println("теоретическое значение ∫e^x dx от 0 до 1 = " + theoretical);
        // разные шаги
        double[] steps = {1.0, 0.1, 0.01, 0.001, 0.0001, 0.00001};
        boolean f = false;
        for (double step : steps) {
            double result = Functions.Integral(expFunc, 0, 1, step);
            double diff = Math.abs(result - theoretical);
            System.out.println("Шаг: " + step + "  Результат: " + result + ", Ошибка: " + diff);

            if (diff < 1e-7) { // 0.0000001
                System.out.println("7 знаков, результат удовлетворяет условию");
                f = true;
                break;
            } else {
                System.out.println("не удовлетворяет условию");
            }
        }
        if (!f) {
            System.out.println("для точности 7 знаков нужен шаг < 0.00001");
        }
        
        nonThread();
        simpleThreads();
        complicatedThreads();
    }
}