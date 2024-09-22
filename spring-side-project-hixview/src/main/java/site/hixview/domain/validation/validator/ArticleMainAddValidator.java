package site.hixview.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.article.ArticleMainDto;
import site.hixview.domain.service.ArticleMainService;

import static site.hixview.domain.vo.name.EntityName.Article.IMAGE_PATH;
import static site.hixview.domain.vo.Word.NAME;

@Component
@RequiredArgsConstructor
public class ArticleMainAddValidator implements Validator {

    private final ArticleMainService articleMainService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return ArticleMainDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        ArticleMainDto articleDto = (ArticleMainDto) target;

        if (articleMainService.findArticleByName(articleDto.getName()).isPresent()) {
            errors.rejectValue(NAME, "Exist");
        }

        if (articleMainService.findArticleByImagePath(articleDto.getImagePath()).isPresent()) {
            errors.rejectValue(IMAGE_PATH, "Exist");
        }
    }
}