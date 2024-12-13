package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.EconomyContent;

public interface EconomyContentTestUtils {
    EconomyContent economyContent = EconomyContent.builder().number(1L).name("합병").build();
    EconomyContent anotherEconomyContent = EconomyContent.builder().number(2L).name("대주주").build();
}
