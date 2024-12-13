package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.ArticleEntity;

public interface ArticleEntityTestUtils {
    /**
     * Create
     */
    default ArticleEntity createArticleEntity() {
        return new ArticleEntity();
    }

    default ArticleEntity createAnotherArticleEntity() {
        return new ArticleEntity();
    }
}
