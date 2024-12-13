package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.aggregate.dto.BlogPostDtoNoNumber;
import site.hixview.aggregate.enums.Classification;

import static site.hixview.aggregate.vo.WordSnake.MAPPED_ARTICLE_NUMBERS_SNAKE;

public interface BlogPostDtoTestUtils {
    /**
     * Create
     */
    default BlogPostDto createBlogPostDto() {
        BlogPostDto blogPostDto = new BlogPostDto();
        blogPostDto.setNumber(1L);
        blogPostDto.setName("이노와이어리스 투자 포인트 재확인");
        blogPostDto.setLink("https://blog.naver.com/akdnjs0308/223491404394");
        blogPostDto.setDate("2024-06-26");
        blogPostDto.setClassification(Classification.COMPANY.name());
        blogPostDto.setMappedArticleNumbers("{\"" + MAPPED_ARTICLE_NUMBERS_SNAKE + "\":[1,2]}");
        return blogPostDto;
    }

    default BlogPostDto createAnotherBlogPostDto() {
        BlogPostDto blogPostDto = new BlogPostDto();
        blogPostDto.setNumber(2L);
        blogPostDto.setName("중국 경제 진단(중국은 어떤 처지에 놓여 있을까)");
        blogPostDto.setLink("https://blog.naver.com/akdnjs0308/223527440189");
        blogPostDto.setDate("2024-07-27");
        blogPostDto.setClassification(Classification.ECONOMY.name());
        blogPostDto.setMappedArticleNumbers("{\"" + MAPPED_ARTICLE_NUMBERS_SNAKE + "\":[3,4]}");
        return blogPostDto;
    }

    default BlogPostDtoNoNumber createBlogPostDtoNoNumber() {
        BlogPostDtoNoNumber blogPostDto = new BlogPostDtoNoNumber();
        blogPostDto.setName("이노와이어리스 투자 포인트 재확인");
        blogPostDto.setLink("https://blog.naver.com/akdnjs0308/223491404394");
        blogPostDto.setDate("2024-06-26");
        blogPostDto.setClassification(Classification.COMPANY.name());
        blogPostDto.setMappedArticleNumbers("{\"" + MAPPED_ARTICLE_NUMBERS_SNAKE + "\":[1,2]}");
        return blogPostDto;
    }

    default BlogPostDtoNoNumber createAnotherBlogPostDtoNoNumber() {
        BlogPostDtoNoNumber blogPostDto = new BlogPostDtoNoNumber();
        blogPostDto.setName("중국 경제 진단(중국은 어떤 처지에 놓여 있을까)");
        blogPostDto.setLink("https://blog.naver.com/akdnjs0308/223527440189");
        blogPostDto.setDate("2024-07-27");
        blogPostDto.setClassification(Classification.ECONOMY.name());
        blogPostDto.setMappedArticleNumbers("{\"" + MAPPED_ARTICLE_NUMBERS_SNAKE + "\":[3,4]}");
        return blogPostDto;
    }
}
