package threads;

import functions.Function;

public class Task {
    private Function function;      // функция для интегрирования
    private double leftX;           // левая граница
    private double rightX;          // правая граница
    private double step;            // шаг дискретизации
    private int tasksCount;         // количество заданий

    // конструктор
    public Task(int tasksCount) {
        this.tasksCount = tasksCount;
    }
    // геттеры
    public Function getFunction() {
        return function;
    }
    public double getLeftX() {
        return leftX;
    }
    public double getRightX() {
        return rightX;
    }
    public double getStep() {
        return step;
    }
    public int getTasksCount() {
        return tasksCount;
    }
    // сеттеры
    public void setFunction(Function function) {
        this.function = function;
    }
    public void setLeftX(double leftX) {
        this.leftX = leftX;
    }
    public void setRightX(double rightX) {
        this.rightX = rightX;
    }
    public void setStep(double step) {
        this.step = step;
    }
    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }
}