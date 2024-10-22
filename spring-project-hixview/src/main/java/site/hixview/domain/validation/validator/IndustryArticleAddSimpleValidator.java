package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.service.IndustryArticleService;

import static site.hixview.domain.vo.Word.LINK;
import static site.hixview.domain.vo.Word.NAME;

@Component
@RequiredArgsConstructor
public class IndustryArticleAddSimpleValidator implements Validator {

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
    }
}
