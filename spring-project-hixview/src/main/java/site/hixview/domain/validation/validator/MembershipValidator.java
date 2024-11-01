package site.hixview.domain.validation.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import site.hixview.domain.entity.member.dto.MembershipDto;
import site.hixview.domain.service.MemberService;

import static site.hixview.domain.vo.Word.EMAIL;
import static site.hixview.domain.vo.Word.ID;
import static site.hixview.domain.vo.name.ErrorCodeName.EXIST;

@Component
@RequiredArgsConstructor
public class MembershipValidator implements Validator {

    private final MemberService memberService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return MembershipDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        MembershipDto membershipDto = (MembershipDto) target;
        String id = membershipDto.getId();
        String email = membershipDto.getEmail();
        if (id != null && memberService.findMemberByID(id).isPresent()) {
            errors.rejectValue(ID, EXIST, "이미 가입된 ID입니다.");
        }
        if (email != null && memberService.findMemberByEmail(email).isPresent()) {
            errors.rejectValue(EMAIL, EXIST, "이미 가입된 이메일입니다.");
        }
    }
}
