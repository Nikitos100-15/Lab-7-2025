package threads;

import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;

// Generator - поток для генерации задач интегрирования
public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore; // семафор для синхронизации доступа

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    // основной метод потока
    public void run() {
        Random random = new Random(); // генератор случайных чисел

        try {
            // Работаем пока поток не прервали и есть задачи для обработки
            while (!isInterrupted() && task.getTasksCount() > 0) {
                try {
                    // захватываем семафор
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Generator прерван при ожидании семафора");
                    break;
                }
                try {
                    // генерация случайных параметров для задачи:
                    double a = random.nextDouble() * 9 + 1;
                    task.setFunction(new Log(a));
                    task.setLeftX(random.nextDouble() * 100);
                    task.setRightX(100 + random.nextDouble() * 100);
                    task.setStep(random.nextDouble());

                    System.out.println("Source: " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep());

                } finally {
                    // всегда освобождаем семафор
                    semaphore.release();
                }

                // пауза между генерациями
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Generator прерван во время паузы"); break;
                }
            }
        } catch (Exception e) {
            // ловим любые другие исключения
            System.out.println("Generator: " + e.getMessage());
        }

        System.out.println("Generator завершил работу");
    }
}