package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.support.spring.util.CompanyArticleTestUtils;

public interface CompanyArticleEntityTestUtils extends ArticleEntityTestUtils, PressEntityTestUtils, CompanyArticleTestUtils {
    /**
     * Create
     */
    default CompanyArticleEntity createCompanyArticleEntity() {
        return CompanyArticleEntity.builder()
                .article(createArticleEntity())
                .name(companyArticle.getName())
                .link(companyArticle.getLink())
                .date(companyArticle.getDate())
                .subjectCountry(companyArticle.getSubjectCountry().name())
                .importance(companyArticle.getImportance().name())
                .summary(companyArticle.getSummary())
                .press(createPressEntity())
                .build();
    }

    default CompanyArticleEntity createAnotherCompanyArticleEntity() {
        return CompanyArticleEntity.builder()
                .article(createAnotherArticleEntity())
                .name(anotherCompanyArticle.getName())
                .link(anotherCompanyArticle.getLink())
                .date(anotherCompanyArticle.getDate())
                .subjectCountry(anotherCompanyArticle.getSubjectCountry().name())
                .importance(anotherCompanyArticle.getImportance().name())
                .summary(anotherCompanyArticle.getSummary())
                .press(createAnotherPressEntity())
                .build();
    }
}
