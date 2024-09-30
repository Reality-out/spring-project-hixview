package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImportanceValidator.class)
@Documented
public @interface ImportanceConstraint {

    String message() default "Importance default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

