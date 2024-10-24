package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TargetArticleNamesValidator.class)
@Documented
public @interface TargetArticleNamesConstraint {

    String message() default "TargetArticleNames default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
