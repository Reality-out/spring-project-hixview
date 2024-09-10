package springsideproject1.springsideproject1build.domain.validation.validator.article.company.main;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springsideproject1.springsideproject1build.domain.entity.article.company.CompanyArticleMainDto;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleMainService;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.IMAGE_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Component
@RequiredArgsConstructor
public class CompanyArticleMainAddValidator implements Validator {

    private final CompanyArticleMainService articleMainService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticleMainDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CompanyArticleMainDto articleDto = (CompanyArticleMainDto) target;

        if (articleMainService.findArticleByName(articleDto.getName()).isPresent()) {
            errors.rejectValue(NAME, "Exist");
        }

        if (articleMainService.findArticleByImagePath(articleDto.getImagePath()).isPresent()) {
            errors.rejectValue(IMAGE_PATH, "Exist");
        }
    }
}
