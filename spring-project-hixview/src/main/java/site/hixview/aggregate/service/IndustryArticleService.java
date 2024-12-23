package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.service.supers.ArticleSuperService;

import java.util.List;

public interface IndustryArticleService extends ArticleSuperService<IndustryArticle> {
    List<IndustryArticle> getByFirstCategory(FirstCategory firstCategory);
}
