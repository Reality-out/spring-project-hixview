package site.hixview.domain.error;

import lombok.Getter;
import org.springframework.validation.AbstractErrors;

@Getter
public class ConstraintValidationException extends RuntimeException {
    private final AbstractErrors error;
    private final boolean beanValidationViolated;

    public ConstraintValidationException(String message, AbstractErrors error) {
        super(message);
        this.error = error;
        this.beanValidationViolated = false;
    }

    public ConstraintValidationException(String message, AbstractErrors error, boolean beanValidationViolated) {
        super(message);
        this.error = error;
        this.beanValidationViolated = beanValidationViolated;
    }
}