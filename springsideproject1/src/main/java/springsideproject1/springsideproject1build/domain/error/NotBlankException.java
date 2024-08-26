package springsideproject1.springsideproject1build.domain.error;

public class NotBlankException extends RuntimeException {
    public NotBlankException(String message) {
        super(message);
    }
}