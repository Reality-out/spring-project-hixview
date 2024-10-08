package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;

import java.time.DateTimeException;
import java.time.LocalDate;

import static site.hixview.domain.vo.Word.DAYS;

@Component
@RequiredArgsConstructor
public class CompanyArticleEntryDateValidator implements Validator {

    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CompanyArticleDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        CompanyArticleDto articleDto = (CompanyArticleDto) target;

        try {
            LocalDate inputDate = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDays());
            if (inputDate.isAfter(LocalDate.now())) {
                errors.rejectValue(DAYS, "Range");
            }
        } catch (DateTimeException e) {
            errors.rejectValue(DAYS, "TypeButInvalid.java.lang.LocalDate");
        }
    }
}