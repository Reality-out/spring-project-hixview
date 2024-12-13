package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.IndustryArticleSecondCategory;

public interface IndustryArticleSecondCategoryTestUtils {
    IndustryArticleSecondCategory industryArticleSecondCategory = IndustryArticleSecondCategory.builder()
            .number(1L).articleNumber(1L).secondCategoryNumber(1L).build();

    IndustryArticleSecondCategory anotherIndustryArticleSecondCategory = IndustryArticleSecondCategory.builder()
            .number(2L).articleNumber(2L).secondCategoryNumber(2L).build();
}
