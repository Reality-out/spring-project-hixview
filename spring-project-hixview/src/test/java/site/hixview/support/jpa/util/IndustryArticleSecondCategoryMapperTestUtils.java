package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.IndustryArticleSecondCategoryMapperEntity;

public interface IndustryArticleSecondCategoryMapperTestUtils extends IndustryArticleTestUtils, SecondCategoryTestUtils {
    default IndustryArticleSecondCategoryMapperEntity createIndustryArticleSecondCategoryMapper() {
        return new IndustryArticleSecondCategoryMapperEntity(createIndustryArticle(), createSecondCategory());
    }

    default IndustryArticleSecondCategoryMapperEntity createAnotherIndustryArticleSecondCategoryMapper() {
        return new IndustryArticleSecondCategoryMapperEntity(createAnotherIndustryArticle(), createAnotherSecondCategory());
    }
}
