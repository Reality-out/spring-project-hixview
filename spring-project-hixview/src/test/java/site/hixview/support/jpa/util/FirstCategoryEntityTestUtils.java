package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.FirstCategoryEntity;

public interface FirstCategoryEntityTestUtils extends IndustryCategoryEntityTestUtils {
    /**
     * Create
     */
    default FirstCategoryEntity createFirstCategoryEntity() {
        return new FirstCategoryEntity("건설", "CONSTRUCTION", createFirstIndustryCategoryEntity());
    }

    default FirstCategoryEntity createAnotherFirstCategoryEntity() {
        return new FirstCategoryEntity("방산", "DEFENSE", createFirstIndustryCategoryEntity());
    }
}
