package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import springsideproject1.springsideproject1build.domain.validation.annotation.validator.EntryDateValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EntryDateValidator.class)
@Documented
public @interface EntryDate {

    String message() default "EntryDate default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

