package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SubjectCountryValidator.class)
@Documented
public @interface SubjectCountryConstraint {

    String message() default "SubjectCountry default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
