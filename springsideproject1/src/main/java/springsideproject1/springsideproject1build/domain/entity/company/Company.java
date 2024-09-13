package springsideproject1.springsideproject1build.domain.entity.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import springsideproject1.springsideproject1build.domain.entity.Country;
import springsideproject1.springsideproject1build.domain.entity.FirstCategory;
import springsideproject1.springsideproject1build.domain.entity.Scale;
import springsideproject1.springsideproject1build.domain.entity.SecondCategory;
import springsideproject1.springsideproject1build.domain.validation.annotation.CodeConstraint;

import java.util.HashMap;

import static springsideproject1.springsideproject1build.domain.vo.CLASS.*;
import static springsideproject1.springsideproject1build.domain.vo.WORD.NAME;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Company {
    @CodeConstraint
    private final String code;

    @NotNull
    private final Country country;

    @NotNull
    private final Scale scale;

    @NotBlank
    @Size(max = 12)
    private final String name;

    @NotNull
    private final FirstCategory firstCategory;

    @NotNull
    private final SecondCategory secondCategory;

    public CompanyDto toDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(code);
        companyDto.setCountry(country.name());
        companyDto.setScale(scale.name());
        companyDto.setName(name);
        companyDto.setFirstCategory(firstCategory.name());
        companyDto.setSecondCategory(secondCategory.name());
        return companyDto;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put(CODE, code);
            put(COUNTRY, country);
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
            country = company.getCountry();
            scale = company.getScale();
            name = company.getName();
            firstCategory = company.getFirstCategory();
            secondCategory = company.getSecondCategory();
            return this;
        }

        public CompanyBuilder companyDto(CompanyDto companyDto) {
            code = companyDto.getCode();
            country = Country.valueOf(companyDto.getCountry());
            scale = Scale.valueOf(companyDto.getScale());
            name = companyDto.getName();
            firstCategory = FirstCategory.valueOf(companyDto.getFirstCategory());
            secondCategory = SecondCategory.valueOf(companyDto.getSecondCategory());
            return this;
        }
    }
}
