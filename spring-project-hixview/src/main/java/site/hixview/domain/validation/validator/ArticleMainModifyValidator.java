package site.hixview.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.home.dto.ArticleMainDto;
import site.hixview.domain.service.ArticleMainService;

import static site.hixview.domain.vo.Word.IMAGE_PATH;
import static site.hixview.domain.vo.Word.NAME;

@Component
@RequiredArgsConstructor
public class ArticleMainModifyValidator implements Validator {

    private final ArticleMainService articleMainService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return ArticleMainDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        ArticleMainDto articleMainDto = (ArticleMainDto) target;

        if (articleMainService.findArticleByName(articleMainDto.getName()).isEmpty()) {
            errors.rejectValue(NAME, "NotFound");
        }

        if (articleMainService.findArticleByImagePath(articleMainDto.getImagePath()).isEmpty()) {
            errors.rejectValue(IMAGE_PATH, "NotFound");
        }
    }
}
