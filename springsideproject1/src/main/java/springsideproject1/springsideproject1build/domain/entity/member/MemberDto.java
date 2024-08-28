package springsideproject1.springsideproject1build.domain.entity.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.*;

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
    private String name;

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;

    @NotNull
    private Integer days;

    @NotBlank
    @Pattern(regexp = PHONE_NUMBER_REGEX_DASHED)
    private String phoneNumber;
}
