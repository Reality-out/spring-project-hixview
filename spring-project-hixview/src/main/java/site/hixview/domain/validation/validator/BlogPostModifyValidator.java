package site.hixview.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;

import static site.hixview.domain.vo.Word.*;

@Component
@RequiredArgsConstructor
public class BlogPostModifyValidator implements Validator {

    private final BlogPostService blogPostService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return BlogPostDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        BlogPostDto blogPostDto = (BlogPostDto) target;

        if (blogPostService.findPostByName(blogPostDto.getName()).isEmpty()) {
            errors.rejectValue(NAME, "NotFound");
        }

        if (blogPostService.findPostByLink(blogPostDto.getLink()).isEmpty()) {
            errors.rejectValue(LINK, "NotFound");
        }
    }
}
