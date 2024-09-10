package springsideproject1.springsideproject1build.domain.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS_NAME.COMPANY_ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS_NAME.INDUSTRY_ARTICLE;

public class ArticleClassNameValidator implements ConstraintValidator<ArticleClassName, String> {

    private final List<String> allowedNames = List.of(COMPANY_ARTICLE, INDUSTRY_ARTICLE);

    @Override
    public boolean isValid(String articleClassName, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (articleClassName == null || articleClassName.isEmpty()) {
            context.buildConstraintViolationWithTemplate("{NotBlank.article.articleClassName}").addConstraintViolation();
            return false;
        }
        if (!allowedNames.contains(articleClassName)) {
            context.buildConstraintViolationWithTemplate("{Restrict.article.articleClassName}").addConstraintViolation();
            return false;
        }
        return true;
    }
}
