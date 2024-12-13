package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.BlogPostArticleEntity;

public interface BlogPostArticleEntityTestUtils extends BlogPostEntityTestUtils, ArticleEntityTestUtils {
    default BlogPostArticleEntity createBlogPostArticle() {
        return new BlogPostArticleEntity(createBlogPost(), createArticle());
    }

    default BlogPostArticleEntity createAnotherBlogPostArticle() {
        return new BlogPostArticleEntity(createAnotherBlogPost(), createAnotherArticle());
    }
}
