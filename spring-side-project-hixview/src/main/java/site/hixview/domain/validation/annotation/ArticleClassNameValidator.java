package site.hixview.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import site.hixview.domain.entity.ArticleClassName;

import java.util.Locale;

import static site.hixview.util.EnumUtils.inEnumConstants;

public class ArticleClassNameValidator implements ConstraintValidator<ArticleClassNameConstraint, String> {

    @Autowired
    private MessageSource source;

    @Override
    public boolean isValid(String articleClassName, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (articleClassName == null || articleClassName.isEmpty()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.article.articleClassName", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!inEnumConstants(ArticleClassName.class, articleClassName)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("typeMismatch.enum.article.articleClassName", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
