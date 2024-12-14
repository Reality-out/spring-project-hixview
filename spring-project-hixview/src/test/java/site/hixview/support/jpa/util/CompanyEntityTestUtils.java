package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.support.spring.util.CompanyTestUtils;

public interface CompanyEntityTestUtils extends FirstCategoryEntityTestUtils, SecondCategoryEntityTestUtils, CompanyTestUtils {
    /**
     * Create
     */
    default CompanyEntity createCompanyEntity() {
        return CompanyEntity.builder()
                .code(company.getCode())
                .koreanName(company.getKoreanName())
                .englishName(company.getEnglishName())
                .nameListed(company.getNameListed())
                .countryListed(company.getCountryListed().name())
                .scale(company.getScale().name())
                .firstCategory(createFirstCategoryEntity())
                .secondCategory(createSecondCategoryEntity()).build();
    }

    default CompanyEntity createAnotherCompanyEntity() {
        return CompanyEntity.builder()
                .code(anotherCompany.getCode())
                .koreanName(anotherCompany.getKoreanName())
                .englishName(anotherCompany.getEnglishName())
                .nameListed(anotherCompany.getNameListed())
                .countryListed(anotherCompany.getCountryListed().name())
                .scale(anotherCompany.getScale().name())
                .firstCategory(createAnotherFirstCategoryEntity())
                .secondCategory(createAnotherSecondCategoryEntity()).build();
    }
}