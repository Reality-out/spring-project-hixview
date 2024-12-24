package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.support.spring.util.IndustryArticleSecondCategoryTestUtils;

public interface IndustryArticleSecondCategoryEntityTestUtils extends IndustryArticleSecondCategoryTestUtils, IndustryArticleEntityTestUtils, SecondCategoryEntityTestUtils {
    default IndustryArticleSecondCategoryEntity createIndustryArticleSecondCategoryEntity() {
        return new IndustryArticleSecondCategoryEntity(createIndustryArticleEntity(), createSecondCategoryEntity());
    }

    default IndustryArticleSecondCategoryEntity createAnotherIndustryArticleSecondCategoryEntity() {
        return new IndustryArticleSecondCategoryEntity(createAnotherIndustryArticleEntity(), createAnotherSecondCategoryEntity());
    }

    default IndustryArticleSecondCategoryEntity createNumberedIndustryArticleSecondCategoryEntity() {
        return new IndustryArticleSecondCategoryEntity(industryArticleSecondCategory.getNumber(), createIndustryArticleEntity(), createSecondCategoryEntity());
    }
}
