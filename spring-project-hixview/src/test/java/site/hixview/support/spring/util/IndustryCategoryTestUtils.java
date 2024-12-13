package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.IndustryCategory;

public interface IndustryCategoryTestUtils {
    IndustryCategory industryCategory = IndustryCategory.builder().number(1L).koreanName("1차 업종").englishName("FIRST_CATEGORY").build();
    IndustryCategory anotherIndustryCategory = IndustryCategory.builder().number(2L).koreanName("2차 업종").englishName("SECOND_CATEGORY").build();
}
