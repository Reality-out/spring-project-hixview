package site.hixview.domain.entity.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static site.hixview.domain.vo.Regex.*;

@Getter
@Setter
public class MemberDto {
    @NotBlank
    @Pattern(regexp = ID_REGEX)
    private String id;

    @NotBlank
    @Pattern(regexp = PW_REGEX)
    private String password;

    @NotBlank
    @Pattern(regexp = NAME_REGEX)
    private String name;

    @NotBlank
    @Pattern(regexp = EMAIL_REGEX)
    private String email;
}
