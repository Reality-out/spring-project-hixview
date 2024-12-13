package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.EconomyArticleContentEntity;

public interface EconomyArticleContentEntityTestUtils extends EconomyArticleEntityTestUtils, EconomyContentEntityTestUtils {
    default EconomyArticleContentEntity createEconomyArticleContent() {
        return new EconomyArticleContentEntity(createEconomyArticle(), createEconomyContent());
    }

    default EconomyArticleContentEntity createAnotherEconomyArticleContent() {
        return new EconomyArticleContentEntity(createAnotherEconomyArticle(), createAnotherEconomyContent());
    }
}
