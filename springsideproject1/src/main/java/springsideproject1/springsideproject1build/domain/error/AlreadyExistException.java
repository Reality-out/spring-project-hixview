package springsideproject1.springsideproject1build.domain.error;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
    }
}