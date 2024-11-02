package site.hixview.domain.validation.annotation.company;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@NotBlank(message = "{NotBlank.company.name}")
@Size(max = 12, message = "{Size.company.name}")
public @interface CompanyName {

    String message() default "CompanyName default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
