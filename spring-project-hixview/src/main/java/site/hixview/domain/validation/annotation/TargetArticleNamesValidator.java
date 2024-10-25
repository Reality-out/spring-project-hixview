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

import static site.hixview.domain.vo.Word.TARGET_ARTICLE_NAME;
import static site.hixview.util.JsonUtils.deserializeWithOneMapToList;

public class TargetArticleNamesValidator implements ConstraintValidator<TargetArticleNamesConstraint, String> {

    @Autowired
    private MessageSource source;

    private final Logger log = LoggerFactory.getLogger(TargetArticleNamesValidator.class);

    @Override
    public boolean isValid(String targetArticleNames, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        List<String> articleNamesList = deserializeWithOneMapToList(new ObjectMapper(), TARGET_ARTICLE_NAME, targetArticleNames);
        for (String economyContent : articleNamesList) {
            if (economyContent.isBlank()) {
                context.buildConstraintViolationWithTemplate(
                        source.getMessage("NotBlank.post.targetArticleName", null, Locale.getDefault())
                ).addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
