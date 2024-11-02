package site.hixview.domain.validation.annotation.post;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

import static site.hixview.domain.vo.Regex.URL_REGEX;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@NotBlank(message = "{NotBlank.post.link}")
@Size(max = 400, message = "{Size.post.link}")
@Pattern(regexp = URL_REGEX, message = "{Pattern.post.link}")
public @interface PostLink {

    String message() default "PostLink default message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
