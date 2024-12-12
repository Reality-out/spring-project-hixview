package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.BlogPostArticleEntity;

public interface BlogPostArticleMapperTestUtils extends BlogPostTestUtils, ArticleTestUtils {
    default BlogPostArticleEntity createBlogPostArticleMapper() {
        return new BlogPostArticleEntity(createBlogPost(), createArticle());
    }

    default BlogPostArticleEntity createAnotherBlogPostArticleMapper() {
        return new BlogPostArticleEntity(createAnotherBlogPost(), createAnotherArticle());
    }
}
