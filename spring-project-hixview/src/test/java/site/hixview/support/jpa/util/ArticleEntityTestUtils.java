package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.support.spring.util.ArticleTestUtils;

public interface ArticleEntityTestUtils extends ArticleTestUtils {
    /**
     * Create
     */
    default ArticleEntity createArticleEntity() {
        return new ArticleEntity();
    }

    default ArticleEntity createAnotherArticleEntity() {
        return new ArticleEntity();
    }

    default ArticleEntity createNumberedArticleEntity() {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.updateNumber(article.getNumber());
        return articleEntity;
    }
}
