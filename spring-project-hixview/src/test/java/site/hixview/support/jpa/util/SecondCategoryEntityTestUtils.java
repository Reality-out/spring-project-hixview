package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.SecondCategoryEntity;

public interface SecondCategoryEntityTestUtils extends FirstCategoryEntityTestUtils {
    /**
     * Create
     */
    default SecondCategoryEntity createSecondCategoryEntity() {
        return new SecondCategoryEntity("은행", "BANK", createSecondIndustryCategoryEntity(), createFirstCategoryEntity());
    }

    default SecondCategoryEntity createAnotherSecondCategoryEntity() {
        return new SecondCategoryEntity("배터리 제조", "BATTERY_MANUFACTURING", createSecondIndustryCategoryEntity(), createAnotherFirstCategoryEntity());
    }
}