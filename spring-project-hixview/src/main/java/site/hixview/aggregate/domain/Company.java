package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class Company {
    private final String code;
    private final String koreanName;
    private final String englishName;
    private final String nameListed;
    private final Country countryListed;
    private final Scale scale;
    private final Long firstCategoryNumber;
    private final Long secondCategoryNumber;

    public static class CompanyBuilder {
        private String code;
        private String koreanName;
        private String englishName;
        private String nameListed;
        private Country countryListed;
        private Scale scale;
        private Long firstCategoryNumber;
        private Long secondCategoryNumber;

        public CompanyBuilder company(final Company company) {
            this.code = company.getCode();
            this.koreanName = company.getKoreanName();
            this.englishName = company.getEnglishName();
            this.nameListed = company.getNameListed();
            this.countryListed = company.getCountryListed();
            this.scale = company.getScale();
            this.firstCategoryNumber = company.getFirstCategoryNumber();
            this.secondCategoryNumber = company.getSecondCategoryNumber();
            return this;
        }

        public Company build() {
            return new Company(this.code, this.koreanName, this.englishName, this.nameListed, this.countryListed, this.scale, this.firstCategoryNumber, this.secondCategoryNumber);
        }
    }
}
