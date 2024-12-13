package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.BlogPostArticleDto;

public interface BlogPostArticleDtoTestUtils {
    /**
     * Create
     */
    default BlogPostArticleDto createBlogPostArticleDto() {
        BlogPostArticleDto blogPostArticleDto = new BlogPostArticleDto();
        blogPostArticleDto.setNumber(1L);
        blogPostArticleDto.setPostNumber(1L);
        blogPostArticleDto.setArticleNumber(1L);
        return blogPostArticleDto;
    }

    default BlogPostArticleDto createAnotherBlogPostArticleDto() {
        BlogPostArticleDto blogPostArticleDto = new BlogPostArticleDto();
        blogPostArticleDto.setNumber(2L);
        blogPostArticleDto.setPostNumber(2L);
        blogPostArticleDto.setArticleNumber(2L);
        return blogPostArticleDto;
    }
}
