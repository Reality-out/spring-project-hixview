package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.EconomyArticleContentEntity;

public interface EconomyArticleContentMapperTestUtils extends EconomyArticleTestUtils, EconomyContentTestUtils {
    default EconomyArticleContentEntity createEconomyArticleContentMapper() {
        return new EconomyArticleContentEntity(createEconomyArticle(), createEconomyContent());
    }

    default EconomyArticleContentEntity createAnotherEconomyArticleContentMapper() {
        return new EconomyArticleContentEntity(createAnotherEconomyArticle(), createAnotherEconomyContent());
    }
}
