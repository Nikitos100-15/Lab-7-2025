package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;
    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            // synchronized
            synchronized (task) {
                try {
                    if (task.getFunction() == null) {
                        Thread.sleep(30);
                        continue;
                    }
                    // берем текущие данные
                    double leftX = task.getLeftX();
                    double rightX = task.getRightX();
                    double step = task.getStep();

                    // вычисляем интеграл
                    double integral = Functions.Integral(task.getFunction(), leftX, rightX, step);
                    // выводим результат
                    System.out.println("Integrator: Result " + leftX + " " + rightX + " " + step + " " + integral);

                } catch (Exception e) {
                    System.out.println("Integrator error: " + e.getMessage());
                }
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}