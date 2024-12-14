package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.aggregate.dto.BlogPostArticleDto;

import static site.hixview.aggregate.vo.WordCamel.BLOG_POST_ARTICLE;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BlogPostArticleMapper {
    @Mapping(target = BLOG_POST_ARTICLE, ignore = true)
    BlogPostArticle toBlogPostArticle(BlogPostArticleDto blogPostDto);

    BlogPostArticleDto toBlogPostArticleDto(BlogPostArticle blogPost);
}
