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
@NotNull(message = "{NotNull.article.days}")
@Range(min = 1, max = 31, message = "{Range.article.days}")
public @interface ArticleDays {

    String message() default "ArticleDays default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
