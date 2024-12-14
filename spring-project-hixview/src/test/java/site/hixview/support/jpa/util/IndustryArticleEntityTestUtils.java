package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.support.spring.util.IndustryArticleTestUtils;

public interface IndustryArticleEntityTestUtils extends ArticleEntityTestUtils, PressEntityTestUtils, FirstCategoryEntityTestUtils, IndustryArticleTestUtils {
    /**
     * Create
     */
    default IndustryArticleEntity createIndustryArticleEntity() {
        return IndustryArticleEntity.builder()
                .article(createArticleEntity())
                .name(industryArticle.getName())
                .link(industryArticle.getLink())
                .date(industryArticle.getDate())
                .subjectCountry(industryArticle.getSubjectCountry().name())
                .importance(industryArticle.getImportance().name())
                .summary(industryArticle.getSummary())
                .press(createPressEntity())
                .firstCategory(createFirstCategoryEntity())
                .build();
    }

    default IndustryArticleEntity createAnotherIndustryArticleEntity() {
        return IndustryArticleEntity.builder()
                .article(createAnotherArticleEntity())
                .name(anotherIndustryArticle.getName())
                .link(anotherIndustryArticle.getLink())
                .date(anotherIndustryArticle.getDate())
                .subjectCountry(anotherIndustryArticle.getSubjectCountry().name())
                .importance(anotherIndustryArticle.getImportance().name())
                .summary(anotherIndustryArticle.getSummary())
                .press(createAnotherPressEntity())
                .firstCategory(createAnotherFirstCategoryEntity())
                .build();
    }
}
