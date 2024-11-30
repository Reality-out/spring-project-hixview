package site.hixview.aggregate.error;

public class UnexpectedClassTypeException extends UnexpectedObjectTypeException {
    public UnexpectedClassTypeException(String message) {
        super(message);
    }
}
