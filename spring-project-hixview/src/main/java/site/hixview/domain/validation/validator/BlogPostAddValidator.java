package site.hixview.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.home.dto.ArticleMainDto;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.*;

import static site.hixview.domain.vo.Word.*;
import static site.hixview.util.EnumUtils.inEnumConstants;

@Component
@RequiredArgsConstructor
public class BlogPostAddValidator implements Validator {

    private final CompanyService companyService;
    private final BlogPostService blogPostService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return ArticleMainDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        BlogPostDto postDto = (BlogPostDto) target;

        if (blogPostService.findPostByName(postDto.getName()).isPresent()) {
            errors.rejectValue(NAME, "Exist");
        }

        if (blogPostService.findPostByLink(postDto.getLink()).isPresent()) {
            errors.rejectValue(LINK, "Exist");
        }

        String targetName = postDto.getTargetName();
        if (companyService.findCompanyByName(targetName).isEmpty() &&
                !inEnumConstants(FirstCategory.class, targetName) &&
                !inEnumConstants(Country.class, targetName)) {
            errors.rejectValue(TARGET_NAME, "NotFound");
        }
    }
}
