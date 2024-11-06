package site.hixview.domain.entity.company;

import lombok.*;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.ListedCountry;
import site.hixview.domain.entity.Scale;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.company.dto.CompanyDto;
import site.hixview.domain.validation.annotation.*;
import site.hixview.domain.validation.annotation.company.CompanyName;

import java.util.HashMap;

import static site.hixview.domain.vo.Word.*;

@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Company {
    @CodeConstraint
    private final String code;

    @ListedCountryConstraint
    private final ListedCountry listedCountry;

    @ScaleConstraint
    private final Scale scale;

    @CompanyName
    private final String name;

    @FirstCategoryConstraint
    private final FirstCategory firstCategory;

    @SecondCategoryConstraint
    private final SecondCategory secondCategory;

    public CompanyDto toDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(code);
        companyDto.setListedCountry(listedCountry.name());
        companyDto.setScale(scale.name());
        companyDto.setName(name);
        companyDto.setFirstCategory(firstCategory.name());
        companyDto.setSecondCategory(secondCategory.name());
        return companyDto;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put(CODE, code);
            put(LISTED_COUNTRY, listedCountry);
            put(SCALE, scale);
            put(NAME, name);
            put(FIRST_CATEGORY, firstCategory);
            put(SECOND_CATEGORY, secondCategory);
        }};
    }

    public static class CompanyBuilder {
        public CompanyBuilder() {}

        public CompanyBuilder company(Company company) {
            code = company.getCode();
            listedCountry = company.getListedCountry();
            scale = company.getScale();
            name = company.getName();
            firstCategory = company.getFirstCategory();
            secondCategory = company.getSecondCategory();
            return this;
        }

        public CompanyBuilder companyDto(CompanyDto companyDto) {
            code = companyDto.getCode();
            listedCountry = ListedCountry.valueOf(companyDto.getListedCountry());
            scale = Scale.valueOf(companyDto.getScale());
            name = companyDto.getName();
            firstCategory = FirstCategory.valueOf(companyDto.getFirstCategory());
            secondCategory = SecondCategory.valueOf(companyDto.getSecondCategory());
            return this;
        }
    }
}
