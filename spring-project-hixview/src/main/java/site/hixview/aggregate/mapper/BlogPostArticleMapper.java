package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.aggregate.dto.BlogPostArticleDto;

@Mapper
public interface BlogPostArticleMapper {
    BlogPostArticle toBlogPostArticle(BlogPostArticleDto blogPostDto);

    BlogPostArticleDto toBlogPostArticleDto(BlogPostArticle blogPost);
}
