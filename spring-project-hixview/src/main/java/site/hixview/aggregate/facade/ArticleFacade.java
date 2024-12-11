package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.domain.Article.ArticleBuilder;
import site.hixview.aggregate.dto.ArticleDto;

public class ArticleFacade {
    public ArticleBuilder createBuilder(final Article article) {
        return Article.builder()
                .number(article.getNumber());
    }

    public ArticleBuilder createBuilder(final ArticleDto article) {
        return Article.builder()
                .number(article.getNumber());
    }
}