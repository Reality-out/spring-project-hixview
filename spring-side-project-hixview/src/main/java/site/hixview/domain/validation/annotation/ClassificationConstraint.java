package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClassificationValidator.class)
@Documented
public @interface ClassificationConstraint {

    String message() default "Classification default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
