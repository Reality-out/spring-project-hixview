package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import springsideproject1.springsideproject1build.domain.validation.annotation.validator.ScaleValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ScaleValidator.class)
@Documented
public @interface Scale {

    String message() default "Scale default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
