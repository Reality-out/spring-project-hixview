package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ArticleClassNameValidator.class)
@Documented
public @interface ArticleClassNameConstraint {

    String message() default "ArticleClassName default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}