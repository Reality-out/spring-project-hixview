package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.SecondCategory;

public interface SecondCategoryTestUtils {
    SecondCategory secondCategory = SecondCategory.builder()
            .number(1L)
            .koreanName("은행")
            .englishName("BANK")
            .industryCategoryNumber(2L)
            .firstCategoryNumber(1L).build();

    SecondCategory anotherSecondCategory = SecondCategory.builder()
            .number(2L)
            .koreanName("배터리 제조")
            .englishName("BATTERY_MANUFACTURING")
            .industryCategoryNumber(2L)
            .firstCategoryNumber(2L).build();
}
