package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.CompanyDto;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class Company implements ConvertibleToDto<CompanyDto> {

    private final String code;
    private final String koreanName;
    private final String englishName;
    private final String nameListed;
    private final Country countryListed;
    private final Scale scale;
    private final Long firstCategoryNumber;
    private final Long secondCategoryNumber;

    @Override
    public CompanyDto toDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(code);
        companyDto.setKoreanName(koreanName);
        companyDto.setEnglishName(englishName);
        companyDto.setNameListed(nameListed);
        companyDto.setCountryListed(countryListed.name());
        companyDto.setScale(scale.name());
        companyDto.setFirstCategoryNumber(firstCategoryNumber);
        companyDto.setSecondCategoryNumber(secondCategoryNumber);
        return companyDto;
    }

    public static final class CompanyBuilder {
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

        public CompanyBuilder companyDto(final CompanyDto companyDto) {
            this.code = companyDto.getCode();
            this.koreanName = companyDto.getKoreanName();
            this.englishName = companyDto.getEnglishName();
            this.nameListed = companyDto.getNameListed();
            this.countryListed = Country.valueOf(companyDto.getCountryListed());
            this.scale = Scale.valueOf(companyDto.getScale());
            this.firstCategoryNumber = companyDto.getFirstCategoryNumber();
            this.secondCategoryNumber = companyDto.getSecondCategoryNumber();
            return this;
        }
    }
}
