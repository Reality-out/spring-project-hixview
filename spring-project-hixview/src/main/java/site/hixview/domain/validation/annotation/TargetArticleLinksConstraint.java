package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TargetArticleLinksValidator.class)
@Documented
public @interface TargetArticleLinksConstraint {

    String message() default "TargetArticleLinks default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
