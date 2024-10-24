package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SubjectSecondCategoriesValidator.class)
@Documented
public @interface SubjectSecondCategoriesConstraint {

    String message() default "SecondCategory default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
