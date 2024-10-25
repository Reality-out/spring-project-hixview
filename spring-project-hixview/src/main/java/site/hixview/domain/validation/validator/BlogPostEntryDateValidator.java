package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;

import java.time.DateTimeException;
import java.time.LocalDate;

import static site.hixview.domain.vo.Word.DAYS;

@Component
@RequiredArgsConstructor
public class BlogPostEntryDateValidator implements Validator {

    private final BlogPostService blogPostService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return BlogPostDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        BlogPostDto blogPostDto = (BlogPostDto) target;

        try {
            LocalDate inputDate = LocalDate.of(blogPostDto.getYear(), blogPostDto.getMonth(), blogPostDto.getDays());
            if (inputDate.isAfter(LocalDate.now())) {
                errors.rejectValue(DAYS, "Restrict");
            }
        } catch (DateTimeException e) {
            errors.rejectValue(DAYS, "TypeButInvalid.java.lang.LocalDate");
        }
    }
}