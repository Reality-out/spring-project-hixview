package springsideproject1.springsideproject1build.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}