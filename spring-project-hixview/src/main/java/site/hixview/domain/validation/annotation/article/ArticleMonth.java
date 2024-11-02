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
@NotNull(message = "{NotNull.article.month}")
@Range(min = 1, max = 12, message = "{Range.article.month}")
public @interface ArticleMonth {

    String message() default "ArticleMonth default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
