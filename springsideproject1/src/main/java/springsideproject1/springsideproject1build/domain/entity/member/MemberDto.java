package springsideproject1.springsideproject1build.domain.entity.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

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
    @Pattern(regexp = NAME_REGEX)
    private String name;

    @NotNull
    @Range(max = 2099)
    private Integer year;

    @NotNull
    @Range(min = 1, max = 12)
    private Integer month;

    @NotNull
    @Range(min = 1, max = 31)
    private Integer days;

    @NotBlank
    @Pattern(regexp = PHONE_NUMBER_REGEX_DASHED)
    private String phoneNumber;
}
