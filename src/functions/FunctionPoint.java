package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    // поле нашего класса
    private double x;
    private double y;
    // конструктор, который создаёт объект с двумя точками координат
    public  FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    // конструктор, который создаёт объект с двумя нулевыми точками
    public FunctionPoint() {
        this.x = 0;
        this.y = 0;
    }
    // конструктор, который создает объект с двумя точно такими же точками(т.е. копии какой-либо другой точкой)
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;

    }
    // геттер получение переменной X
    public double getX(){
        return x;
    }
    // геттер полечения переменной Y
    public  double getY(){
        return y;
    }
    // сеттеры для переменных x и y
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    // 5 лаболаторная
    // 1 задание переопределение методов
    public String toString() {
        return "(" + x +"," + y +")"; // возвращает текстовое описание точки
    }
    // equals
    public boolean equals(Object o) {
        // проверка на тот же объект
        if (this == o) return true;
        // проверка на null
        if (o == null ) return false;
        FunctionPoint that = (FunctionPoint) o;

        // Double.compare()
        return (Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0);
    }
    // hashCode
    public int hashCode() {
        int result = 17;
        //Double.hashCode()
        result = 31 * result + Double.hashCode(x);
        result = 31 * result + Double.hashCode(y);
        return result;
    }
    // метод clone():
    public Object clone() throws CloneNotSupportedException {
        // возвращаем объект-копию для объекта точки
        return super.clone();
    }
    }

