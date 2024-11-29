package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.CompanyEntity;

public interface CompanyTestUtils extends FirstCategoryTestUtils, SecondCategoryTestUtils {
    /**
     * Create
     */
    default CompanyEntity createCompany() {
        return CompanyEntity.builder().code("000270").koreanName("기아").englishName("KIA").nameListed("기아")
                .countryListed("SOUTH_KOREA").scale("BIG").firstCategory(createFirstCategory())
                .secondCategory(createSecondCategory()).build();
    }

    default CompanyEntity createAnotherCompany() {
        return CompanyEntity.builder().code("000660").koreanName("SK하이닉스").englishName("SK_HYNIX")
                .nameListed("SK하이닉스").countryListed("SOUTH_KOREA").scale("BIG").firstCategory(createAnotherFirstCategory())
                .secondCategory(createAnotherSecondCategory()).build();
    }
}