package springsideproject1.springsideproject1build.domain.entity.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import springsideproject1.springsideproject1build.domain.validation.annotation.Country;
import springsideproject1.springsideproject1build.domain.validation.annotation.FirstCategory;
import springsideproject1.springsideproject1build.domain.validation.annotation.Scale;
import springsideproject1.springsideproject1build.domain.validation.annotation.SecondCategory;
import springsideproject1.springsideproject1build.domain.validation.annotation.*;

@Getter
@Setter
public class CompanyDto {
    @Code
    private String code;

    @Country
    private String country;

    @Scale
    private String scale;

    @NotBlank
    @Size(max = 12)
    private String name;

    @FirstCategory
    private String firstCategory;

    @SecondCategory
    private String secondCategory;
}
