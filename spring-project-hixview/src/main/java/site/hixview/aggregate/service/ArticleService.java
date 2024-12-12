package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.service.supers.OnlyAllowedToSearch;

public interface ArticleService extends OnlyAllowedToSearch<Article> {

}
