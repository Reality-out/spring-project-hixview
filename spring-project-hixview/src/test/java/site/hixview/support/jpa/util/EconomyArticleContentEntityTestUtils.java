package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.EconomyArticleContentEntity;

public interface EconomyArticleContentEntityTestUtils extends EconomyArticleEntityTestUtils, EconomyContentEntityTestUtils {
    default EconomyArticleContentEntity createEconomyArticleContentEntity() {
        return new EconomyArticleContentEntity(createEconomyArticleEntity(), createEconomyContentEntity());
    }

    default EconomyArticleContentEntity createAnotherEconomyArticleContentEntity() {
        return new EconomyArticleContentEntity(createAnotherEconomyArticleEntity(), createAnotherEconomyContentEntity());
    }

    default EconomyArticleContentEntity createNumberedEconomyArticleContentEntity() {
        return new EconomyArticleContentEntity(createNumberedEconomyArticleEntity(), createNumberedEconomyContentEntity());
    }
}
