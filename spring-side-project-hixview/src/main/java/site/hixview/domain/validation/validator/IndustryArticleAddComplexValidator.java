package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.article.IndustryArticleDto;
import site.hixview.domain.service.IndustryArticleService;

import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.util.EnumUtils.inEnumConstants;

@Component
@RequiredArgsConstructor
public class IndustryArticleAddComplexValidator implements Validator {

    private final IndustryArticleEntryDateValidator entryDateValidator;
    private final IndustryArticleService articleService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return IndustryArticleDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        IndustryArticleDto articleDto = (IndustryArticleDto) target;

        entryDateValidator.validate(articleDto, errors);

        if (articleService.findArticleByName(articleDto.getName()).isPresent()) {
            errors.rejectValue(NAME, "Exist");
        }
        if (articleService.findArticleByLink(articleDto.getLink()).isPresent()) {
            errors.rejectValue(LINK, "Exist");
        }
        if (!inEnumConstants(FirstCategory.class, articleDto.getSubjectFirstCategory())) {
            errors.rejectValue(SUBJECT_FIRST_CATEGORY, "NotFound");
        }
        if (!inEnumConstants(SecondCategory.class, articleDto.getSubjectSecondCategory())) {
            errors.rejectValue(SUBJECT_SECOND_CATEGORY, "NotFound");
        }
    }
}
