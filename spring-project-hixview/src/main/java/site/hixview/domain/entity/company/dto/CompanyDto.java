package site.hixview.domain.entity.company.dto;

import lombok.Getter;
import lombok.Setter;
import site.hixview.domain.validation.annotation.*;
import site.hixview.domain.validation.annotation.company.CompanyName;

@Getter
@Setter
public class CompanyDto {
    @CodeConstraint
    private String code;

    @ListedCountryConstraint
    private String listedCountry;

    @ScaleConstraint
    private String scale;

    @CompanyName
    private String name;

    @FirstCategoryConstraint
    private String firstCategory;

    @SecondCategoryConstraint
    private String secondCategory;
}
