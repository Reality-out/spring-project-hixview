package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.Press;

public interface PressTestUtils {
    Press press = Press.builder().number(1L).koreanName("아주경제").englishName("AJU_ECONOMY").build();
    Press anotherPress = Press.builder().number(2L).koreanName("아시아경제").englishName("ASIA_ECONOMY").build();
}
