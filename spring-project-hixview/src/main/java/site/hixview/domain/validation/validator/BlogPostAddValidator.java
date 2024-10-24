package site.hixview.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.home.dto.ArticleMainDto;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.EconomyArticleService;
import site.hixview.domain.service.IndustryArticleService;

import static site.hixview.domain.vo.Word.*;

@Component
@RequiredArgsConstructor
public class BlogPostAddValidator implements Validator {

    private final CompanyArticleService companyArticleService;
    private final IndustryArticleService industryArticleService;
    private final EconomyArticleService economyArticleService;
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

        if (companyArticleService.findArticleByName(postDto.getTargetName()).isEmpty() &&
                industryArticleService.findArticleByName(postDto.getTargetName()).isEmpty() &&
                economyArticleService.findArticleByName(postDto.getTargetName()).isEmpty()) {
            errors.rejectValue(TARGET_NAME, "NotFound");
        }
    }
}
