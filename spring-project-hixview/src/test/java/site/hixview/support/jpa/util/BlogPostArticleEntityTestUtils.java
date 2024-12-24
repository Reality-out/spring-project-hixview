package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.support.spring.util.BlogPostArticleTestUtils;

public interface BlogPostArticleEntityTestUtils extends BlogPostArticleTestUtils, BlogPostEntityTestUtils, ArticleEntityTestUtils {
    default BlogPostArticleEntity createBlogPostArticleEntity() {
        return new BlogPostArticleEntity(createBlogPostEntity(), createArticleEntity());
    }

    default BlogPostArticleEntity createAnotherBlogPostArticleEntity() {
        return new BlogPostArticleEntity(createAnotherBlogPostEntity(), createAnotherArticleEntity());
    }

    default BlogPostArticleEntity createNumberedBlogPostArticleEntity() {
        return new BlogPostArticleEntity(blogPostArticle.getNumber(), createNumberedBlogPostEntity(), createNumberedArticleEntity());
    }
}
