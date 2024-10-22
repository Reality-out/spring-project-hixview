package site.hixview.domain.validation.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import site.hixview.domain.entity.SecondCategory;

import java.util.List;
import java.util.Locale;

import static site.hixview.domain.vo.Word.SUBJECT_SECOND_CATEGORY;
import static site.hixview.util.EnumUtils.inEnumConstants;
import static site.hixview.util.JsonUtils.deserializeWithOneMapToList;

public class SecondCategoriesValidator implements ConstraintValidator<SecondCategoriesConstraint, String> {

    @Autowired
    private MessageSource source;

    private final Logger log = LoggerFactory.getLogger(SecondCategoriesValidator.class);

    @Override
    public boolean isValid(String secondCategories, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        List<String> secondCategoriesList = deserializeWithOneMapToList(new ObjectMapper(), SUBJECT_SECOND_CATEGORY, secondCategories);
        for (String secondCategory : secondCategoriesList) {
            if (secondCategory.isBlank()) {
                context.buildConstraintViolationWithTemplate(
                        source.getMessage("NotBlank.company.secondCategory", null, Locale.getDefault())
                ).addConstraintViolation();
                return false;
            }
        }
        for (String secondCategory : secondCategoriesList) {
            if (!inEnumConstants(SecondCategory.class, secondCategory)) {
                context.buildConstraintViolationWithTemplate(
                        source.getMessage("typeMismatch.enum.company.secondCategory", null, Locale.getDefault())
                ).addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
