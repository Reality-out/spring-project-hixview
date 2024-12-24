package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.spring.util.SecondCategoryTestUtils;

public interface SecondCategoryEntityTestUtils extends FirstCategoryEntityTestUtils, SecondCategoryTestUtils {
    /**
     * Create
     */
    default SecondCategoryEntity createSecondCategoryEntity() {
        return new SecondCategoryEntity(secondCategory.getKoreanName(), secondCategory.getEnglishName(), createSecondIndustryCategoryEntity(), createFirstCategoryEntity());
    }

    default SecondCategoryEntity createAnotherSecondCategoryEntity() {
        return new SecondCategoryEntity(anotherIndustryCategory.getKoreanName(), anotherFirstCategory.getEnglishName(), createSecondIndustryCategoryEntity(), createAnotherFirstCategoryEntity());
    }

    default SecondCategoryEntity createNumberedSecondCategoryEntity() {
        return new SecondCategoryEntity(secondCategory.getNumber(), secondCategory.getKoreanName(), secondCategory.getEnglishName(), createNumberedSecondIndustryCategoryEntity(), createNumberedFirstCategoryEntity());
    }
}