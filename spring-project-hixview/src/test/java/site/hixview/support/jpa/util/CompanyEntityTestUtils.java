package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.CompanyEntity;

public interface CompanyEntityTestUtils extends FirstCategoryEntityTestUtils, SecondCategoryEntityTestUtils {
    /**
     * Create
     */
    default CompanyEntity createCompanyEntity() {
        return CompanyEntity.builder().code("000270").koreanName("기아").englishName("KIA").nameListed("기아")
                .countryListed("SOUTH_KOREA").scale("BIG").firstCategory(createFirstCategoryEntity())
                .secondCategory(createSecondCategoryEntity()).build();
    }

    default CompanyEntity createAnotherCompanyEntity() {
        return CompanyEntity.builder().code("000660").koreanName("SK하이닉스").englishName("SK_HYNIX")
                .nameListed("SK하이닉스").countryListed("SOUTH_KOREA").scale("BIG").firstCategory(createAnotherFirstCategoryEntity())
                .secondCategory(createAnotherSecondCategoryEntity()).build();
    }
}