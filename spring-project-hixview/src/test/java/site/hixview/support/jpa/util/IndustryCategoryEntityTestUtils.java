package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.IndustryCategoryEntity;

public interface IndustryCategoryEntityTestUtils {
    /**
     * Create
     */
    default IndustryCategoryEntity createFirstIndustryCategoryEntity() {
        return new IndustryCategoryEntity("1차 업종", "FIRST_CATEGORY");
    }

    default IndustryCategoryEntity createSecondIndustryCategoryEntity() {
        return new IndustryCategoryEntity("2차 업종", "SECOND_CATEGORY");
    }
}