package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public FunctionPointIndexOutOfBoundsException() {
    }

    public FunctionPointIndexOutOfBoundsException(String message) {
        super(message);
    }

    public FunctionPointIndexOutOfBoundsException(int index) {
        super("значение точки вне границ: " + index);
    }

    public FunctionPointIndexOutOfBoundsException(int index, int size) {
        super("значение точки вне границ: " + index);
    }
}