package springsideproject1.springsideproject1build.domain.entity.company;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.CODE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NAME;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Company {
    @Size(min = 6, max = 6)
    private final String code;

    private final Country country;
    private final Scale scale;
    private final String name;
    private final FirstCategory category1st;
    private final SecondCategory category2nd;

    public CompanyDto toCompanyDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(code);
        companyDto.setCountry(country.name());
        companyDto.setScale(scale.name());
        companyDto.setName(name);
        companyDto.setCategory1st(category1st.name());
        companyDto.setCategory2nd(category2nd.name());
        return companyDto;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put(CODE, code);
            put("country", country);
            put("scale", scale);
            put(NAME, name);
            put("category1st", category1st);
            put("category2nd", category2nd);
        }};
    }

    public static class CompanyBuilder {
        public CompanyBuilder() {
        }

        public CompanyBuilder company(Company company) {
            code = company.getCode();
            country = company.getCountry();
            scale = company.getScale();
            name = company.getName();
            category1st = company.getCategory1st();
            category2nd = company.getCategory2nd();
            return this;
        }

        public CompanyBuilder companyDto(CompanyDto companyDto) {
            code = companyDto.getCode();
            country = Country.valueOf(companyDto.getCountry());
            scale = Scale.valueOf(companyDto.getScale());
            name = companyDto.getName();
            category1st = FirstCategory.valueOf(companyDto.getCategory1st());
            category2nd = SecondCategory.valueOf(companyDto.getCategory2nd());
            return this;
        }
    }
}
