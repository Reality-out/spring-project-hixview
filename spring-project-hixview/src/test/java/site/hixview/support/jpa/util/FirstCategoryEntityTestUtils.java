package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.support.spring.util.FirstCategoryTestUtils;

public interface FirstCategoryEntityTestUtils extends IndustryCategoryEntityTestUtils, FirstCategoryTestUtils {
    /**
     * Create
     */
    default FirstCategoryEntity createFirstCategoryEntity() {
        return new FirstCategoryEntity(firstCategory.getKoreanName(), firstCategory.getEnglishName(),
                createFirstIndustryCategoryEntity());
    }

    default FirstCategoryEntity createAnotherFirstCategoryEntity() {
        return new FirstCategoryEntity(anotherFirstCategory.getKoreanName(), anotherFirstCategory.getEnglishName(),
                createFirstIndustryCategoryEntity());
    }

    default FirstCategoryEntity createNumberedFirstCategoryEntity() {
        return new FirstCategoryEntity(firstCategory.getNumber(), firstCategory.getKoreanName(),
                firstCategory.getEnglishName(), createNumberedFirstIndustryCategoryEntity());
    }
}
