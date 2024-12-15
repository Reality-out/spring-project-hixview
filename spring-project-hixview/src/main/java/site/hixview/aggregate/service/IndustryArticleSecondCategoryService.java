package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.service.supers.CrudAllowedServiceWithNumberId;

import java.util.List;

public interface IndustryArticleSecondCategoryService extends CrudAllowedServiceWithNumberId<EconomyArticleContent> {
    List<IndustryArticleSecondCategory> getByIndustryArticle(IndustryArticle article);

    List<IndustryArticleSecondCategory> getBySecondCategory(SecondCategory secondCategory);
}
