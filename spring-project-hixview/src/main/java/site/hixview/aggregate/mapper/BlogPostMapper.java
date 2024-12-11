package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.aggregate.dto.BlogPostDtoNoNumber;
import site.hixview.aggregate.mapper.support.BlogPostMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper
public interface BlogPostMapper extends BlogPostMapperSupport {
    @Mapping(source = CLASSIFICATION, target = CLASSIFICATION, qualifiedByName = "classificationToDomain")
    @Mapping(source = MAPPED_ARTICLE_NUMBERS, target = MAPPED_ARTICLE_NUMBERS, qualifiedByName = "mappedArticleNumbersToDomain")
    BlogPost toBlogPost(BlogPostDto blogPostDto);

    @Mapping(source = CLASSIFICATION, target = CLASSIFICATION, qualifiedByName = "classificationToDto")
    @Mapping(source = MAPPED_ARTICLE_NUMBERS, target = MAPPED_ARTICLE_NUMBERS, qualifiedByName = "mappedArticleNumbersToDto")
    BlogPostDto toBlogPostDto(BlogPost blogPost);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(source = CLASSIFICATION, target = CLASSIFICATION, qualifiedByName = "classificationToDomain")
    @Mapping(source = MAPPED_ARTICLE_NUMBERS, target = MAPPED_ARTICLE_NUMBERS, qualifiedByName = "mappedArticleNumbersToDomain")
    BlogPost toBlogPost(BlogPostDtoNoNumber blogPostDto);

    @Mapping(source = CLASSIFICATION, target = CLASSIFICATION, qualifiedByName = "classificationToDto")
    @Mapping(source = MAPPED_ARTICLE_NUMBERS, target = MAPPED_ARTICLE_NUMBERS, qualifiedByName = "mappedArticleNumbersToDto")
    BlogPostDtoNoNumber toBlogPostDtoNoNumber(BlogPost blogPost);
}
