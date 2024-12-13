package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.FirstCategory;

public interface FirstCategoryTestUtils {
    FirstCategory firstCategory = FirstCategory.builder()
            .number(1L)
            .koreanName("건설")
            .englishName("CONSTRUCTION")
            .industryCategoryNumber(1L).build();

    FirstCategory anotherFirstCategory = FirstCategory.builder()
            .number(2L)
            .koreanName("방산")
            .englishName("DEFENSE")
            .industryCategoryNumber(1L).build();
}
