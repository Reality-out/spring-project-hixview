package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.Article;

public interface ArticleTestUtils {
    /**
     * Create
     */
    Article article = Article.builder().number(1L).build();
    Article anotherArticle = Article.builder().number(2L).build();
}
