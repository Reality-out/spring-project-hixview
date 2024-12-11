package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.BlogPostArticleMapperEntity;

public interface BlogPostArticleMapperTestUtils extends BlogPostTestUtils, ArticleTestUtils {
    default BlogPostArticleMapperEntity createBlogPostArticleMapper() {
        return new BlogPostArticleMapperEntity(createBlogPost(), createArticle());
    }

    default BlogPostArticleMapperEntity createAnotherBlogPostArticleMapper() {
        return new BlogPostArticleMapperEntity(createAnotherBlogPost(), createAnotherArticle());
    }
}
