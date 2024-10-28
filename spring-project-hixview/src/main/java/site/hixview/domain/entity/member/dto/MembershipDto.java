package site.hixview.domain.entity.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static site.hixview.domain.vo.Regex.*;

@Getter
@Setter
public class MembershipDto {
    @NotBlank(message = "ID가 비어 있습니다.")
    @Pattern(regexp = ID_REGEX, message = "ID 형식이 올바르지 않습니다.")
    private String id;

    @NotBlank(message = "비밀번호가 비어 있습니다.")
    @Pattern(regexp = PW_REGEX, message = "비밀번호 형식이 올바르지 않습니다.")
    private String password;

    @NotBlank(message = "이름이 비어 있습니다.")
    @Pattern(regexp = NAME_REGEX, message = "이름 형식이 올바르지 않습니다.")
    private String name;

    @NotBlank(message = "이메일 값이 비어 있습니다.")
    @Pattern(regexp = EMAIL_REGEX, message = "이메일 형식이 올바르지 않습니다.")
    private String email;
}
