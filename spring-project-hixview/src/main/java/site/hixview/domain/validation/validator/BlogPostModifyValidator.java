package site.hixview.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;
import site.hixview.domain.service.CompanyService;

import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.ErrorCodeName.NOT_FOUND;
import static site.hixview.util.EnumUtils.inEnumConstants;
import static site.hixview.util.EnumUtils.inEnumValues;

@Component
@RequiredArgsConstructor
public class BlogPostModifyValidator implements Validator {

    private final BlogPostService blogPostService;
    private final CompanyService companyService;
    private final BlogPostEntryDateValidator blogPostEntryDateValidator;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return BlogPostDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        BlogPostDto blogPostDto = (BlogPostDto) target;

        blogPostEntryDateValidator.validate(blogPostDto, errors);

        if (blogPostService.findPostByName(blogPostDto.getName()).isEmpty()) {
            errors.rejectValue(NAME, NOT_FOUND);
        }

        if (blogPostService.findPostByLink(blogPostDto.getLink()).isEmpty()) {
            errors.rejectValue(LINK, NOT_FOUND);
        }

        String targetName = blogPostDto.getTargetName();
        if (companyService.findCompanyByName(targetName).isEmpty() &&
                !inEnumConstants(FirstCategory.class, targetName) &&
                !inEnumValues(FirstCategory.class, targetName) &&
                !inEnumConstants(Country.class, targetName) &&
                !inEnumValues(Country.class, targetName)) {
            errors.rejectValue(TARGET_NAME, NOT_FOUND);
        }
    }
}
