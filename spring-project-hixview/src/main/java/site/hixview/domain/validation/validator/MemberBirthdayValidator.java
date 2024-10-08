package site.hixview.domain.validation.validator;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.member.dto.MemberDto;

import java.time.DateTimeException;
import java.time.LocalDate;

import static site.hixview.domain.vo.Word.DAYS;

@Component
public class MemberBirthdayValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return MemberDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        MemberDto memberDto = (MemberDto) target;
        LocalDate maxDate = LocalDate.now();

        // date
        try {
            LocalDate inputDate = LocalDate.of(memberDto.getYear(), memberDto.getMonth(), memberDto.getDays());

            if (inputDate.isAfter(maxDate)) {
                errors.rejectValue(DAYS, "Restrict");
            }
        } catch (DateTimeException e) {
            errors.rejectValue(DAYS, "TypeButInvalid");
        }
    }
}
