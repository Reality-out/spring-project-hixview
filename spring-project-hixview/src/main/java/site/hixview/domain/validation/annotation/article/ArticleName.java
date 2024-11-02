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
@NotBlank(message = "{NotBlank.article.name}")
@Size(max = 80, message = "{Size.article.name}")
public @interface ArticleName {

    String message() default "ArticleName default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
