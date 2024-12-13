package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.FirstCategoryEntity;

public interface FirstCategoryEntityTestUtils extends IndustryCategoryEntityTestUtils {
    /**
     * Create
     */
    default FirstCategoryEntity createFirstCategory() {
        return new FirstCategoryEntity("건설", "CONSTRUCTION", createFirstIndustryCategory());
    }

    default FirstCategoryEntity createAnotherFirstCategory() {
        return new FirstCategoryEntity("방산", "DEFENSE", createFirstIndustryCategory());
    }
}
