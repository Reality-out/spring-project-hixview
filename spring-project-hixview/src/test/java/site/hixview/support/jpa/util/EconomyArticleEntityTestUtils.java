package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.support.spring.util.EconomyArticleTestUtils;

public interface EconomyArticleEntityTestUtils extends ArticleEntityTestUtils, PressEntityTestUtils, EconomyArticleTestUtils {
    /**
     * Create
     */
    default EconomyArticleEntity createEconomyArticleEntity() {
        return EconomyArticleEntity.builder()
                .article(createArticleEntity())
                .name(economyArticle.getName())
                .link(economyArticle.getLink())
                .date(economyArticle.getDate())
                .subjectCountry(economyArticle.getSubjectCountry().name())
                .importance(economyArticle.getImportance().name())
                .summary(economyArticle.getSummary())
                .press(createPressEntity())
                .build();
    }

    default EconomyArticleEntity createAnotherEconomyArticleEntity() {
        return EconomyArticleEntity.builder()
                .article(createAnotherArticleEntity())
                .name(anotherEconomyArticle.getName())
                .link(anotherEconomyArticle.getLink())
                .date(anotherEconomyArticle.getDate())
                .subjectCountry(anotherEconomyArticle.getSubjectCountry().name())
                .importance(anotherEconomyArticle.getImportance().name())
                .summary(anotherEconomyArticle.getSummary())
                .press(createAnotherPressEntity())
                .build();
    }

    default EconomyArticleEntity createNumberedEconomyArticleEntity() {
        return EconomyArticleEntity.builder()
                .article(createNumberedArticleEntity())
                .name(anotherEconomyArticle.getName())
                .link(anotherEconomyArticle.getLink())
                .date(anotherEconomyArticle.getDate())
                .subjectCountry(anotherEconomyArticle.getSubjectCountry().name())
                .importance(anotherEconomyArticle.getImportance().name())
                .summary(anotherEconomyArticle.getSummary())
                .press(createAnotherPressEntity())
                .build();
    }
}
