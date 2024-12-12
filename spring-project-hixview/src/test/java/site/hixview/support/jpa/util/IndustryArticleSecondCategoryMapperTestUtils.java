package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;

public interface IndustryArticleSecondCategoryMapperTestUtils extends IndustryArticleTestUtils, SecondCategoryTestUtils {
    default IndustryArticleSecondCategoryEntity createIndustryArticleSecondCategoryMapper() {
        return new IndustryArticleSecondCategoryEntity(createIndustryArticle(), createSecondCategory());
    }

    default IndustryArticleSecondCategoryEntity createAnotherIndustryArticleSecondCategoryMapper() {
        return new IndustryArticleSecondCategoryEntity(createAnotherIndustryArticle(), createAnotherSecondCategory());
    }
}
