package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.BlogPostArticle;

public interface BlogPostArticleTestUtils {
    BlogPostArticle blogPostArticle = BlogPostArticle.builder().number(1L).postNumber(1L).articleNumber(1L).build();
    BlogPostArticle anotherBlogPostArticle = BlogPostArticle.builder().number(2L).postNumber(2L).articleNumber(2L).build();
}
