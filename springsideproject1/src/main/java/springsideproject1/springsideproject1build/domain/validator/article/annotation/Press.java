package springsideproject1.springsideproject1build.domain.validator.article.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import springsideproject1.springsideproject1build.domain.validator.article.validator.PressValidator;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PressValidator.class)
@Documented
public @interface Press {

    String message() default "{typeMismatch.enum.article.press}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
