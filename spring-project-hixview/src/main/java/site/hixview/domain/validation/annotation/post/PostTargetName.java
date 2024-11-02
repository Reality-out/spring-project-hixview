package site.hixview.domain.validation.annotation.post;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@NotBlank(message = "{NotBlank.post.targetName}")
@Size(max = 80, message = "{Size.post.targetName}")
public @interface PostTargetName {

    String message() default "PostTargetName default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
