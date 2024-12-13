package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.SecondCategoryEntity;

public interface SecondCategoryEntityTestUtils extends FirstCategoryEntityTestUtils {
    /**
     * Create
     */
    default SecondCategoryEntity createSecondCategory() {
        return new SecondCategoryEntity("은행", "BANK", createSecondIndustryCategory(), createFirstCategory());
    }

    default SecondCategoryEntity createAnotherSecondCategory() {
        return new SecondCategoryEntity("배터리 제조", "BATTERY_MANUFACTURING", createSecondIndustryCategory(), createAnotherFirstCategory());
    }
}