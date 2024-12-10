package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.EconomyArticleContentMapperEntity;

public interface EconomyArticleContentMapperTestUtils extends EconomyArticleTestUtils, EconomyContentTestUtils {
    default EconomyArticleContentMapperEntity createEconomyArticleContentMapper() {
        return new EconomyArticleContentMapperEntity(createEconomyArticle(), createEconomyContent());
    }

    default EconomyArticleContentMapperEntity createAnotherEconomyArticleContentMapper() {
        return new EconomyArticleContentMapperEntity(createAnotherEconomyArticle(), createAnotherEconomyContent());
    }
}
