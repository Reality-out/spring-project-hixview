package springsideproject1.springsideproject1build.domain.entity.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import springsideproject1.springsideproject1build.domain.validation.annotation.*;

@Getter
@Setter
public class CompanyDto {
    @CodeConstraint
    private String code;

    @CountryConstraint
    private String country;

    @ScaleConstraint
    private String scale;

    @NotBlank
    @Size(max = 12)
    private String name;

    @FirstCategoryConstraint
    private String firstCategory;

    @SecondCategoryConstraint
    private String secondCategory;
}
