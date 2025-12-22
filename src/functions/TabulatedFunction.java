package functions;

import java.util.Iterator;
import functions.Function;
import functions.FunctionPoint;
import functions.InappropriateFunctionPointException;

public interface TabulatedFunction extends Function, Iterable<FunctionPoint>, Cloneable {

// все объекты типа клонируемыми с точки зрения JVM

    int getPointsCount();

    FunctionPoint getPoint(int index);

    double getPointX(int index);

    double getPointY(int index);

    void setPointY(int index, double y);

    void setPoint(int index, FunctionPoint point) throws functions.InappropriateFunctionPointException;

    void setPointX(int index, double x) throws functions.InappropriateFunctionPointException;

    void deletePoint(int index);

    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    Object clone() throws CloneNotSupportedException;
}