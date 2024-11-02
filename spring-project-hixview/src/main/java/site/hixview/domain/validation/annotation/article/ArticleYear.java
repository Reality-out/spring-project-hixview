package site.hixview.domain.validation.annotation.article;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@NotNull(message = "{NotNull.article.year}")
@Range(min = 1960, max = 2099, message = "{Range.article.year}")
public @interface ArticleYear {

    String message() default "ArticleYear default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
