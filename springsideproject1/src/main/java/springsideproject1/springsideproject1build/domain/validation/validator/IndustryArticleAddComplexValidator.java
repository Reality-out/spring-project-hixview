package springsideproject1.springsideproject1build.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.FirstCategory;
import springsideproject1.springsideproject1build.domain.entity.SecondCategory;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleDto;
import springsideproject1.springsideproject1build.domain.service.IndustryArticleService;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Article.*;
import static springsideproject1.springsideproject1build.domain.vo.Word.NAME;
import static springsideproject1.springsideproject1build.util.EnumUtils.inEnumConstants;

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
