package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import springsideproject1.springsideproject1build.domain.validation.annotation.validator.ImportanceValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImportanceValidator.class)
@Documented
public @interface Importance {

    String message() default "Importance default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

