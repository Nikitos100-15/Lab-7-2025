package threads;

import functions.basic.Log;

public class SimpleGenerator implements Runnable {
    private Task task;
    public SimpleGenerator(Task task) {
        this.task = task;
    }
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            // synchronized
            synchronized (task) {
                // генерируем задание
                double base = 1 + Math.random() * 9;
                task.setFunction(new Log(base));
                task.setLeftX(Math.random() * 100);
                task.setRightX(100 + Math.random() * 100);
                task.setStep(Math.random());
                System.out.println("Generator: Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}