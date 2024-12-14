package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.aggregate.dto.BlogPostDtoNoNumber;
import site.hixview.support.spring.util.BlogPostTestUtils;

import static site.hixview.aggregate.vo.WordSnake.MAPPED_ARTICLE_NUMBERS_SNAKE;

public interface BlogPostDtoTestUtils extends BlogPostTestUtils {
    String blogPostArticleNumbers = "{\"" + MAPPED_ARTICLE_NUMBERS_SNAKE + "\":[1,2]}";
    String anotherBlogPostArticleNumbers = "{\"" + MAPPED_ARTICLE_NUMBERS_SNAKE + "\":[3,4]}";

    /**
     * Create
     */
    default BlogPostDto createBlogPostDto() {
        BlogPostDto blogPostDto = new BlogPostDto();
        blogPostDto.setNumber(blogPost.getNumber());
        blogPostDto.setName(blogPost.getName());
        blogPostDto.setLink(blogPost.getLink());
        blogPostDto.setDate(String.valueOf(blogPost.getDate()));
        blogPostDto.setClassification(blogPost.getClassification().name());
        blogPostDto.setMappedArticleNumbers(blogPostArticleNumbers);
        return blogPostDto;
    }

    default BlogPostDto createAnotherBlogPostDto() {
        BlogPostDto blogPostDto = new BlogPostDto();
        blogPostDto.setNumber(anotherBlogPost.getNumber());
        blogPostDto.setName(anotherBlogPost.getName());
        blogPostDto.setLink(anotherBlogPost.getLink());
        blogPostDto.setDate(String.valueOf(anotherBlogPost.getDate()));
        blogPostDto.setClassification(blogPost.getClassification().name());
        blogPostDto.setMappedArticleNumbers(anotherBlogPostArticleNumbers);
        return blogPostDto;
    }

    default BlogPostDtoNoNumber createBlogPostDtoNoNumber() {
        BlogPostDtoNoNumber blogPostDto = new BlogPostDtoNoNumber();
        blogPostDto.setName(blogPost.getName());
        blogPostDto.setLink(blogPost.getLink());
        blogPostDto.setDate(String.valueOf(blogPost.getDate()));
        blogPostDto.setClassification(blogPost.getClassification().name());
        blogPostDto.setMappedArticleNumbers(blogPostArticleNumbers);
        return blogPostDto;
    }

    default BlogPostDtoNoNumber createAnotherBlogPostDtoNoNumber() {
        BlogPostDtoNoNumber blogPostDto = new BlogPostDtoNoNumber();
        blogPostDto.setName(anotherBlogPost.getName());
        blogPostDto.setLink(anotherBlogPost.getLink());
        blogPostDto.setDate(String.valueOf(anotherBlogPost.getDate()));
        blogPostDto.setClassification(blogPost.getClassification().name());
        blogPostDto.setMappedArticleNumbers(anotherBlogPostArticleNumbers);
        return blogPostDto;
    }
}
