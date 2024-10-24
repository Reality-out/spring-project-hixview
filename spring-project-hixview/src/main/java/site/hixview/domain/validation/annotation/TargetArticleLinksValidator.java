package site.hixview.domain.validation.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

import static site.hixview.domain.vo.Word.TARGET_ARTICLE_LINK;
import static site.hixview.util.JsonUtils.deserializeWithOneMapToList;

public class TargetArticleLinksValidator implements ConstraintValidator<TargetArticleLinksConstraint, String> {

    @Autowired
    private MessageSource source;

    private final Logger log = LoggerFactory.getLogger(TargetArticleLinksValidator.class);

    @Override
    public boolean isValid(String targetArticleLinks, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        List<String> articleLinksList = deserializeWithOneMapToList(new ObjectMapper(), TARGET_ARTICLE_LINK, targetArticleLinks);
        for (String economyContent : articleLinksList) {
            if (economyContent.isBlank()) {
                context.buildConstraintViolationWithTemplate(
                        source.getMessage("NotBlank.article.targetArticleLink", null, Locale.getDefault())
                ).addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
