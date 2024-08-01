package springsideproject1.springsideproject1build.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Company {
    private final String code;
    private final String country;
    private final String scale;
    private final String name;
    private final String category1st;
    private final String category2nd;

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
    }
}
