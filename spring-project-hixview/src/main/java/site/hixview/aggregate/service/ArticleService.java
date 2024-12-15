package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.service.supers.OnlyGetAllowedServiceWithNumberId;

public interface ArticleService extends OnlyGetAllowedServiceWithNumberId<Article> {
}
