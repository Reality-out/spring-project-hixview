package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.support.spring.util.IndustryCategoryTestUtils;

public interface IndustryCategoryEntityTestUtils extends IndustryCategoryTestUtils {
    /**
     * Create
     */
    default IndustryCategoryEntity createFirstIndustryCategoryEntity() {
        return new IndustryCategoryEntity(industryCategory.getKoreanName(), industryCategory.getEnglishName());
    }

    default IndustryCategoryEntity createSecondIndustryCategoryEntity() {
        return new IndustryCategoryEntity(anotherIndustryCategory.getKoreanName(), anotherIndustryCategory.getEnglishName());
    }

    default IndustryCategoryEntity createNumberedIndustryCategoryEntity() {
        return new IndustryCategoryEntity(
                industryCategory.getNumber(), industryCategory.getKoreanName(), industryCategory.getEnglishName());
    }
}