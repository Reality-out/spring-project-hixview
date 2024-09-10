package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ArticleClassNameValidator.class)
@Documented
public @interface ArticleClassName {

    String message() default "ArticleClassName default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
