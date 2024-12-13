package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;

public interface IndustryArticleSecondCategoryEntityTestUtils extends IndustryArticleEntityTestUtils, SecondCategoryEntityTestUtils {
    default IndustryArticleSecondCategoryEntity createIndustryArticleSecondCategoryEntity() {
        return new IndustryArticleSecondCategoryEntity(createIndustryArticleEntity(), createSecondCategoryEntity());
    }

    default IndustryArticleSecondCategoryEntity createAnotherIndustryArticleSecondCategoryEntity() {
        return new IndustryArticleSecondCategoryEntity(createAnotherIndustryArticleEntity(), createAnotherSecondCategoryEntity());
    }
}
