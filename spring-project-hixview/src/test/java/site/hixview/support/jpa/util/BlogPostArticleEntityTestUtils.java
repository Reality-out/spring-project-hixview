package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.BlogPostArticleEntity;

public interface BlogPostArticleEntityTestUtils extends BlogPostEntityTestUtils, ArticleEntityTestUtils {
    default BlogPostArticleEntity createBlogPostArticleEntity() {
        return new BlogPostArticleEntity(createBlogPostEntity(), createArticleEntity());
    }

    default BlogPostArticleEntity createAnotherBlogPostArticleEntity() {
        return new BlogPostArticleEntity(createAnotherBlogPostEntity(), createAnotherArticleEntity());
    }
}
