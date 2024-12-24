package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.support.spring.util.EconomyArticleContentTestUtils;

public interface EconomyArticleContentEntityTestUtils extends EconomyArticleContentTestUtils, EconomyArticleEntityTestUtils, EconomyContentEntityTestUtils {
    default EconomyArticleContentEntity createEconomyArticleContentEntity() {
        return new EconomyArticleContentEntity(createEconomyArticleEntity(), createEconomyContentEntity());
    }

    default EconomyArticleContentEntity createAnotherEconomyArticleContentEntity() {
        return new EconomyArticleContentEntity(createAnotherEconomyArticleEntity(), createAnotherEconomyContentEntity());
    }

    default EconomyArticleContentEntity createNumberedEconomyArticleContentEntity() {
        return new EconomyArticleContentEntity(economyArticleContent.getNumber(), createNumberedEconomyArticleEntity(), createNumberedEconomyContentEntity());
    }
}
