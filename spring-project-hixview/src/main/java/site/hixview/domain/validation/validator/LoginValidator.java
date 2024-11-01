package site.hixview.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.member.dto.LoginDto;
import site.hixview.domain.service.MemberService;

import static site.hixview.domain.vo.Word.ID;
import static site.hixview.domain.vo.Word.PASSWORD;
import static site.hixview.domain.vo.name.ErrorCodeName.NOT_FOUND;

@Component
@RequiredArgsConstructor
public class LoginValidator implements Validator {

    private final MemberService memberService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return LoginDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        LoginDto loginDto = (LoginDto) target;
        String id = loginDto.getId();
        String pw = loginDto.getPassword();
        if (id != null && memberService.findMemberByID(id).isEmpty()) {
            errors.rejectValue(ID, NOT_FOUND, "해당 ID가 존재하지 않습니다.");
        }
        if (id != null && pw != null && memberService.findMemberByIDAndPassword(id, pw).isEmpty()) {
            errors.rejectValue(PASSWORD, NOT_FOUND, "해당 정보에 맞는 회원이 존재하지 않습니다.");
        }
    }
}
