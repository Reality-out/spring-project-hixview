package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CodeValidator.class)
@Documented
public @interface CodeConstraint {

    String message() default "Code default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}