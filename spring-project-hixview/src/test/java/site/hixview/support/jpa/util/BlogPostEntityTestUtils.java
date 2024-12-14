package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.support.spring.util.BlogPostTestUtils;

public interface BlogPostEntityTestUtils extends PostEntityTestUtils, BlogPostTestUtils {
    /**
     * Create
     */
    default BlogPostEntity createBlogPostEntity() {
        return BlogPostEntity.builder()
                .post(createPostEntity())
                .name(blogPost.getName())
                .link(blogPost.getLink())
                .date(blogPost.getDate())
                .classification(blogPost.getClassification().name())
                .build();
    }

    default BlogPostEntity createAnotherBlogPostEntity() {
        return BlogPostEntity.builder()
                .post(createPostEntity())
                .name(anotherBlogPost.getName())
                .link(anotherBlogPost.getLink())
                .date(anotherBlogPost.getDate())
                .classification(anotherBlogPost.getClassification().name())
                .build();
    }
}
