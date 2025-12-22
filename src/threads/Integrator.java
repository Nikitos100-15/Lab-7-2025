package threads;
import functions.Functions;
import java.util.concurrent.Semaphore;

// Integrator - поток для вычисления интегралов
public class Integrator extends Thread {
    private final Task task;
    private final Semaphore semaphore; //семафор для синхронизации доступа
    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    // основной метод потока
    public void run() {
        try {
            // работает пока поток не прервали
            while (!isInterrupted() && task.getTasksCount() > 0) {
                try {
                    // захватываем семафор
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Integrator прерван при ожидании семафора");
                    break;
                }
                try {
                    // чтение параметров задачи, сгенерированных Generator
                    double leftX = task.getLeftX();
                    double rightX = task.getRightX();
                    double step = task.getStep();
                    // вычисление интеграла функции на заданном интервале
                    double integral = Functions.Integral(task.getFunction(), leftX, rightX, step);
                    // вывод результата вычислений
                    System.out.println("Result: " + leftX + " " + rightX + " " + step + " " + integral);
                    // уменьшаем счетчик оставшихся задач
                    task.setTasksCount(task.getTasksCount() - 1);

                } finally {
                    // всегда освобождаем семафор
                    semaphore.release();
                }

                //пауза между вычислениями
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Integrator прерван во время паузы");
                    break;
                }
            }
        } catch (Exception e) {
            //  ловим любые другие исключения
            System.out.println("Integrator error: " + e.getMessage());
        }
        System.out.println("Integrator завершил работу");
    }
}