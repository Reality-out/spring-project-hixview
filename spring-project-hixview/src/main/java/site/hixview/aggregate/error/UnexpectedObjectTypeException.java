package site.hixview.aggregate.error;

public class UnexpectedObjectTypeException extends IllegalArgumentException {
    public UnexpectedObjectTypeException(String message) {
        super(message);
    }
}
