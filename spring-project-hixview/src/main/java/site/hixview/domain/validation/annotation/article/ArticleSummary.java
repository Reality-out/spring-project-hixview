package site.hixview.domain.validation.annotation.article;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@NotBlank(message = "{NotBlank.article.summary}")
@Size(max = 36, message = "{Size.article.summary}")
public @interface ArticleSummary {

    String message() default "ArticleSummary default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
