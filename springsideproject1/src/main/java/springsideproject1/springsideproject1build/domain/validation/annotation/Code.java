package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CodeValidator.class)
@Documented
public @interface Code {

    String message() default "Code default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}