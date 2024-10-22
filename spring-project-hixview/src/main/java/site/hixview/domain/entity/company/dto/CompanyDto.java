package site.hixview.domain.entity.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import site.hixview.domain.validation.annotation.*;

@Getter
@Setter
public class CompanyDto {
    @CodeConstraint
    private String code;

    @ListedCountryConstraint
    private String listedCountry;

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
