package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.BlogPostArticleDto;
import site.hixview.support.spring.util.BlogPostArticleTestUtils;

public interface BlogPostArticleDtoTestUtils extends BlogPostArticleTestUtils {
    /**
     * Create
     */
    default BlogPostArticleDto createBlogPostArticleDto() {
        BlogPostArticleDto blogPostArticleDto = new BlogPostArticleDto();
        blogPostArticleDto.setNumber(blogPostArticle.getNumber());
        blogPostArticleDto.setPostNumber(blogPostArticle.getPostNumber());
        blogPostArticleDto.setArticleNumber(blogPostArticle.getArticleNumber());
        return blogPostArticleDto;
    }

    default BlogPostArticleDto createAnotherBlogPostArticleDto() {
        BlogPostArticleDto blogPostArticleDto = new BlogPostArticleDto();
        blogPostArticleDto.setNumber(anotherBlogPostArticle.getNumber());
        blogPostArticleDto.setPostNumber(anotherBlogPostArticle.getPostNumber());
        blogPostArticleDto.setArticleNumber(anotherBlogPostArticle.getArticleNumber());
        return blogPostArticleDto;
    }
}
