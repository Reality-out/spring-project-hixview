package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FirstCategoryValidator.class)
@Documented
public @interface FirstCategoryConstraint {

    String message() default "FirstCategory default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
