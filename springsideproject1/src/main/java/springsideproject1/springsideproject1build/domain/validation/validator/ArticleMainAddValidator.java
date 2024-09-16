package springsideproject1.springsideproject1build.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMainDto;
import springsideproject1.springsideproject1build.domain.service.ArticleMainService;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Article.IMAGE_PATH;
import static springsideproject1.springsideproject1build.domain.vo.Word.NAME;

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
