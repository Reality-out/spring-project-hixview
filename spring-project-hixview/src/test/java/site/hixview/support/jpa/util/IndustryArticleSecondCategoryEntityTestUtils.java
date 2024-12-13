package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;

public interface IndustryArticleSecondCategoryEntityTestUtils extends IndustryArticleEntityTestUtils, SecondCategoryEntityTestUtils {
    default IndustryArticleSecondCategoryEntity createIndustryArticleSecondCategory() {
        return new IndustryArticleSecondCategoryEntity(createIndustryArticle(), createSecondCategory());
    }

    default IndustryArticleSecondCategoryEntity createAnotherIndustryArticleSecondCategory() {
        return new IndustryArticleSecondCategoryEntity(createAnotherIndustryArticle(), createAnotherSecondCategory());
    }
}
