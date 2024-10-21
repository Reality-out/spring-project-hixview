package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.service.EconomyArticleService;

import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.name.EntityName.Article.LINK;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_COUNTRY;
import static site.hixview.util.EnumUtils.inEnumConstants;

@Component
@RequiredArgsConstructor
public class EconomyArticleAddComplexValidator implements Validator {

    private final EconomyArticleEntryDateValidator entryDateValidator;
    private final EconomyArticleService articleService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return EconomyArticleDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        EconomyArticleDto articleDto = (EconomyArticleDto) target;

        entryDateValidator.validate(articleDto, errors);

        if (articleService.findArticleByName(articleDto.getName()).isPresent()) {
            errors.rejectValue(NAME, "Exist");
        }
        if (articleService.findArticleByLink(articleDto.getLink()).isPresent()) {
            errors.rejectValue(LINK, "Exist");
        }
        if (!inEnumConstants(Country.class, articleDto.getSubjectCountry())) {
            errors.rejectValue(SUBJECT_COUNTRY, "NotFound");
        }
    }
}
