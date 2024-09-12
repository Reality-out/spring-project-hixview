package springsideproject1.springsideproject1build.domain.validation.validator.article.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleDto;
import springsideproject1.springsideproject1build.domain.service.IndustryArticleService;

import static springsideproject1.springsideproject1build.domain.entity.FirstCategory.containedWithFirstCategory;
import static springsideproject1.springsideproject1build.domain.entity.SecondCategory.containedWithSecondCategory;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.*;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.SUBJECT_SECOND_CATEGORY;
import static springsideproject1.springsideproject1build.domain.vo.WORD.NAME;

@Component
@RequiredArgsConstructor
public class IndustryArticleModifyValidator implements Validator {

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

        if (articleService.findArticleByName(articleDto.getName()).isEmpty()) {
            errors.rejectValue(NAME, "NotFound");
        }
        if (articleService.findArticleByLink(articleDto.getLink()).isEmpty()) {
            errors.rejectValue(LINK, "NotFound");
        }
        if (!containedWithFirstCategory(articleDto.getSubjectFirstCategory())) {
            errors.rejectValue(SUBJECT_FIRST_CATEGORY, "NotFound");
        }
        if (!containedWithSecondCategory(articleDto.getSubjectSecondCategory())) {
            errors.rejectValue(SUBJECT_SECOND_CATEGORY, "NotFound");
        }
    }
}
