package site.hixview.domain.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TargetEconomyContentsValidator.class)
@Documented
public @interface TargetEconomyContentsConstraint {

    String message() default "EconomyContents default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
