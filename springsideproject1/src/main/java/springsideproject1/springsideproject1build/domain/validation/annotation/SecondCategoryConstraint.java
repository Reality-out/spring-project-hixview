package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SecondCategoryValidator.class)
@Documented
public @interface SecondCategoryConstraint {

    String message() default "SecondCategory default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
