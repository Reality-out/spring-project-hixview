package site.hixview.domain.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.service.EconomyArticleService;

import java.time.DateTimeException;
import java.time.LocalDate;

import static site.hixview.domain.vo.Word.DAYS;

@Component
@RequiredArgsConstructor
public class EconomyArticleEntryDateValidator implements Validator {

    private final EconomyArticleService articleService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return EconomyArticleDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        EconomyArticleDto articleDto = (EconomyArticleDto) target;

        try {
            LocalDate inputDate = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDays());
            if (inputDate.isAfter(LocalDate.now())) {
                errors.rejectValue(DAYS, "Restrict");
            }
        } catch (DateTimeException e) {
            errors.rejectValue(DAYS, "TypeButInvalid.java.lang.LocalDate");
        }
    }
}