package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS_NAME.COMPANY_ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS_NAME.INDUSTRY_ARTICLE;

public class ArticleClassNameValidator implements ConstraintValidator<ArticleClassName, String> {

    @Autowired
    private MessageSource source;

    private final List<String> allowedNames = List.of(COMPANY_ARTICLE, INDUSTRY_ARTICLE);

    @Override
    public boolean isValid(String articleClassName, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (articleClassName == null || articleClassName.isEmpty()) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("NotBlank.article.articleClassName", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        if (!allowedNames.contains(articleClassName)) {
            context.buildConstraintViolationWithTemplate(
                    source.getMessage("Restrict.article.articleClassName", null, Locale.getDefault())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
