package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.domain.Company.CompanyBuilder;
import site.hixview.aggregate.dto.CompanyDto;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;

public class CompanyFacade {
    public CompanyBuilder createBuilder(Company company) {
        return Company.builder()
                .code(company.getCode())
                .koreanName(company.getKoreanName())
                .englishName(company.getEnglishName())
                .nameListed(company.getNameListed())
                .countryListed(company.getCountryListed())
                .scale(company.getScale())
                .firstCategoryNumber(company.getFirstCategoryNumber())
                .secondCategoryNumber(company.getSecondCategoryNumber());
    }

    public CompanyBuilder createBuilder(CompanyDto companyDto) {
        return Company.builder()
                .code(companyDto.getCode())
                .koreanName(companyDto.getKoreanName())
                .englishName(companyDto.getEnglishName())
                .nameListed(companyDto.getNameListed())
                .countryListed(Country.valueOf(companyDto.getCountryListed()))
                .scale(Scale.valueOf(companyDto.getScale()))
                .firstCategoryNumber(companyDto.getFirstCategoryNumber())
                .secondCategoryNumber(companyDto.getSecondCategoryNumber());
    }
}
