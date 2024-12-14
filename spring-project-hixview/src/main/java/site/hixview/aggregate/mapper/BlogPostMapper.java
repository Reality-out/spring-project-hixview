package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.aggregate.dto.BlogPostDtoNoNumber;
import site.hixview.aggregate.mapper.support.BlogPostMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BlogPostMapper extends BlogPostMapperSupport {
    @Mapping(source = MAPPED_ARTICLE_NUMBERS, target = MAPPED_ARTICLE_NUMBERS, qualifiedByName = "mappedArticleNumbersToDomain")
    @Mapping(target = BLOG_POST, ignore = true)
    BlogPost toBlogPost(BlogPostDto blogPostDto);

    @Mapping(source = MAPPED_ARTICLE_NUMBERS, target = MAPPED_ARTICLE_NUMBERS, qualifiedByName = "mappedArticleNumbersToDto")
    BlogPostDto toBlogPostDto(BlogPost blogPost);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(source = MAPPED_ARTICLE_NUMBERS, target = MAPPED_ARTICLE_NUMBERS, qualifiedByName = "mappedArticleNumbersToDomain")
    @Mapping(target = BLOG_POST, ignore = true)
    BlogPost toBlogPost(BlogPostDtoNoNumber blogPostDto);

    @Mapping(source = MAPPED_ARTICLE_NUMBERS, target = MAPPED_ARTICLE_NUMBERS, qualifiedByName = "mappedArticleNumbersToDto")
    BlogPostDtoNoNumber toBlogPostDtoNoNumber(BlogPost blogPost);
}
