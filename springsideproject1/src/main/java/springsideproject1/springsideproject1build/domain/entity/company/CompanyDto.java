package springsideproject1.springsideproject1build.domain.entity.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import springsideproject1.springsideproject1build.domain.validation.annotation.Country;

import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.NUMBER_REGEX;

@Getter
@Setter
public class CompanyDto {
    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = NUMBER_REGEX)
    private String code;

    @Country
    private String country;

    @NotBlank
    private String scale;

    @NotBlank
    private String name;

    @NotBlank
    private String firstCategory;

    @NotBlank
    private String secondCategory;
}
