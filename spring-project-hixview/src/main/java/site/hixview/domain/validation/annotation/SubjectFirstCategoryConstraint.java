package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SubjectFirstCategoryValidator.class)
@Documented
public @interface SubjectFirstCategoryConstraint {

    String message() default "SubjectFirstCategory default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
