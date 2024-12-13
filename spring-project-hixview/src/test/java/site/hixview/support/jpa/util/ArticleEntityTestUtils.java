package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.ArticleEntity;

public interface ArticleEntityTestUtils {
    /**
     * Create
     */
    default ArticleEntity createArticle() {
        return new ArticleEntity();
    }

    default ArticleEntity createAnotherArticle() {
        return new ArticleEntity();
    }
}
