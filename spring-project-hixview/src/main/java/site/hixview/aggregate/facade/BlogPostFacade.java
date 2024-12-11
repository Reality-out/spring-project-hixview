package site.hixview.aggregate.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.domain.BlogPost.BlogPostBuilder;
import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.aggregate.dto.BlogPostDtoNoNumber;
import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.error.RuntimeJsonParsingException;

import java.util.List;
import java.util.Map;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_LIST;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_ARTICLE_NUMBERS_SNAKE;

public class BlogPostFacade {
    public BlogPostBuilder createBuilder(BlogPost blogPost) {
        return BlogPost.builder()
                .number(blogPost.getNumber())
                .name(blogPost.getName())
                .link(blogPost.getLink())
                .date(blogPost.getDate())
                .classification(blogPost.getClassification())
                .mappedArticleNumbers(blogPost.getMappedArticleNumbers());
    }

    public BlogPostBuilder createBuilder(BlogPostDto blogPostDto) {
        List<Long> mappedArticleNumbers;
        try {
            Map<String, List<Long>> mappedArticleNumbersMap = new ObjectMapper().
                    readValue(blogPostDto.getMappedArticleNumbers(), new TypeReference<>() {
                    });
            mappedArticleNumbers = mappedArticleNumbersMap.get(MAPPED_ARTICLE_NUMBERS_SNAKE);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + blogPostDto.getMappedArticleNumbers());
        }
        return BlogPost.builder()
                .number(blogPostDto.getNumber())
                .name(blogPostDto.getName())
                .link(blogPostDto.getLink())
                .date(convertFromStringToLocalDate(blogPostDto.getDate()))
                .classification(Classification.valueOf(blogPostDto.getClassification()))
                .mappedArticleNumbers(mappedArticleNumbers);
    }

    public BlogPostBuilder createBuilder(BlogPostDtoNoNumber blogPostDto) {
        List<Long> mappedArticleNumbers;
        try {
            Map<String, List<Long>> mappedArticleNumbersMap = new ObjectMapper().
                    readValue(blogPostDto.getMappedArticleNumbers(), new TypeReference<>() {
                    });
            mappedArticleNumbers = mappedArticleNumbersMap.get(MAPPED_ARTICLE_NUMBERS_SNAKE);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + blogPostDto.getMappedArticleNumbers());
        }
        return BlogPost.builder()
                .name(blogPostDto.getName())
                .link(blogPostDto.getLink())
                .date(convertFromStringToLocalDate(blogPostDto.getDate()))
                .classification(Classification.valueOf(blogPostDto.getClassification()))
                .mappedArticleNumbers(mappedArticleNumbers);
    }
}
