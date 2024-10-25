package site.hixview.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.SubjectCountry;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;
import site.hixview.domain.service.CompanyService;

import static site.hixview.domain.vo.Word.*;
import static site.hixview.util.EnumUtils.inEnumConstants;
import static site.hixview.util.EnumUtils.inEnumValues;

@Component
@RequiredArgsConstructor
public class BlogPostAddValidator implements Validator {

    private final CompanyService companyService;
    private final BlogPostService blogPostService;
    private final BlogPostEntryDateValidator blogPostEntryDateValidator;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return BlogPostDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        BlogPostDto blogPostDto = (BlogPostDto) target;

        blogPostEntryDateValidator.validate(blogPostDto, errors);

        if (blogPostService.findPostByName(blogPostDto.getName()).isPresent()) {
            errors.rejectValue(NAME, "Exist");
        }

        if (blogPostService.findPostByLink(blogPostDto.getLink()).isPresent()) {
            errors.rejectValue(LINK, "Exist");
        }

        String targetName = blogPostDto.getTargetName();
        if (companyService.findCompanyByName(targetName).isEmpty() &&
                !inEnumConstants(FirstCategory.class, targetName) &&
                !inEnumValues(FirstCategory.class, targetName) &&
                !inEnumConstants(SubjectCountry.class, targetName) &&
                !inEnumValues(SubjectCountry.class, targetName)) {
            errors.rejectValue(TARGET_NAME, "NotFound");
        }
    }
}
