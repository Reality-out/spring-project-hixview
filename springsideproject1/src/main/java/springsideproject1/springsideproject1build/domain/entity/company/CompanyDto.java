package springsideproject1.springsideproject1build.domain.entity.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDto {
    @Size(min = 6, max = 6)
    private String code;

    @NotBlank
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
