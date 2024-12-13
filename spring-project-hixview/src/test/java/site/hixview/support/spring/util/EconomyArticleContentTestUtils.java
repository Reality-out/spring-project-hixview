package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.EconomyArticleContent;

public interface EconomyArticleContentTestUtils {
    EconomyArticleContent economyArticleContent = EconomyArticleContent.builder().number(1L).articleNumber(1L).contentNumber(1L).build();
    EconomyArticleContent anotherEconomyArticleContent = EconomyArticleContent.builder().number(2L).articleNumber(2L).contentNumber(2L).build();
}
