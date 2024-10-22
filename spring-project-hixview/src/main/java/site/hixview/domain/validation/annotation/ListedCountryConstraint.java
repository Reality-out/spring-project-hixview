package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ListedCountryValidator.class)
@Documented
public @interface ListedCountryConstraint {

    String message() default "ListedCountry default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
