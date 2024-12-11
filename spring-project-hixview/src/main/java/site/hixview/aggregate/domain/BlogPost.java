package site.hixview.aggregate.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.aggregate.dto.BlogPostDtoNoNumber;
import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.error.RuntimeJsonParsingException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_MAP_TO_JSON;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_LIST;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_ARTICLE_NUMBERS_SNAKE;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class BlogPost implements ConvertibleToWholeDto<BlogPostDto, BlogPostDtoNoNumber> {

    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Classification classification;
    private final List<Long> mappedArticleNumbers;

    @Override
    public BlogPostDto toDto() {
        BlogPostDto blogPostDto = new BlogPostDto();
        blogPostDto.setNumber(number);
        blogPostDto.setName(name);
        blogPostDto.setLink(link);
        blogPostDto.setDate(String.valueOf(date));
        blogPostDto.setClassification(classification.name());
        try {
            blogPostDto.setMappedArticleNumbers(new ObjectMapper().writeValueAsString(new HashMap<>() {{
                put(MAPPED_ARTICLE_NUMBERS_SNAKE, mappedArticleNumbers);
            }}));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(CANNOT_MAP_TO_JSON + mappedArticleNumbers);
        }
        return blogPostDto;
    }

    @Override
    public BlogPostDtoNoNumber toDtoNoNumber() {
        BlogPostDtoNoNumber blogPostDto = new BlogPostDtoNoNumber();
        blogPostDto.setName(name);
        blogPostDto.setLink(link);
        blogPostDto.setDate(String.valueOf(date));
        blogPostDto.setClassification(classification.name());
        try {
            blogPostDto.setMappedArticleNumbers(new ObjectMapper().writeValueAsString(new HashMap<>() {{
                put(MAPPED_ARTICLE_NUMBERS_SNAKE, mappedArticleNumbers);
            }}));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(CANNOT_MAP_TO_JSON + mappedArticleNumbers);
        }
        return blogPostDto;
    }

    public static final class BlogPostBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Classification classification;
        private List<Long> mappedArticleNumbers;

        public BlogPostBuilder post(final BlogPost blogPost) {
            this.number = blogPost.getNumber();
            this.name = blogPost.getName();
            this.link = blogPost.getLink();
            this.date = blogPost.getDate();
            this.classification = blogPost.getClassification();
            this.mappedArticleNumbers = blogPost.getMappedArticleNumbers();
            return this;
        }

        public BlogPostBuilder blogPostDto(final BlogPostDto blogPostDto) {
            this.number = blogPostDto.getNumber();
            this.name = blogPostDto.getName();
            this.link = blogPostDto.getLink();
            this.date = convertFromStringToLocalDate(blogPostDto.getDate());
            this.classification = Classification.valueOf(blogPostDto.getClassification());
            try {
                Map<String, List<Long>> relatedArticleNumbersMap = new ObjectMapper().
                        readValue(blogPostDto.getMappedArticleNumbers(), new TypeReference<>() {
                        });
                this.mappedArticleNumbers = relatedArticleNumbersMap.get(MAPPED_ARTICLE_NUMBERS_SNAKE);
            } catch (JsonProcessingException e) {
                throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + mappedArticleNumbers);
            }
            return this;
        }

        public BlogPostBuilder blogPostDto(final BlogPostDtoNoNumber blogPostDto) {
            this.name = blogPostDto.getName();
            this.link = blogPostDto.getLink();
            this.date = convertFromStringToLocalDate(blogPostDto.getDate());
            this.classification = Classification.valueOf(blogPostDto.getClassification());
            try {
                Map<String, List<Long>> relatedArticleNumbersMap = new ObjectMapper().
                        readValue(blogPostDto.getMappedArticleNumbers(), new TypeReference<>() {
                        });
                this.mappedArticleNumbers = relatedArticleNumbersMap.get(MAPPED_ARTICLE_NUMBERS_SNAKE);
            } catch (JsonProcessingException e) {
                throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + mappedArticleNumbers);
            }
            return this;
        }
    }
}
