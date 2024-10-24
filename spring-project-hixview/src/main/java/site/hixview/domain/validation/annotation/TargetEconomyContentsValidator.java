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

import static site.hixview.domain.vo.Word.TARGET_ECONOMY_CONTENT;
import static site.hixview.util.JsonUtils.deserializeWithOneMapToList;

public class TargetEconomyContentsValidator implements ConstraintValidator<TargetEconomyContentsConstraint, String> {

    @Autowired
    private MessageSource source;

    private final Logger log = LoggerFactory.getLogger(TargetEconomyContentsValidator.class);

    @Override
    public boolean isValid(String economyContents, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        List<String> economyContentsList = deserializeWithOneMapToList(new ObjectMapper(), TARGET_ECONOMY_CONTENT, economyContents);
        for (String economyContent : economyContentsList) {
            if (economyContent.isBlank()) {
                context.buildConstraintViolationWithTemplate(
                        source.getMessage("NotBlank.article.targetEconomyContent", null, Locale.getDefault())
                ).addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
