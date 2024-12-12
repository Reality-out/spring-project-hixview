package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.mapper.support.BlogPostArticleEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class BlogPostArticleEntityMapper extends BlogPostArticleEntityMapperSupport {
    @Mapping(target = BLOG_POST, ignore = true)
    @Mapping(target = ARTICLE, ignore = true)
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract BlogPostArticleEntity toBlogPostArticleEntity(BlogPostArticle blogPostArticle);

    @Mapping(source = BLOG_POST, target = POST_NUMBER, qualifiedByName = "postNumberToDomain")
    @Mapping(source = ARTICLE, target = ARTICLE_NUMBER, qualifiedByName = "articleNumberToDomain")
    public abstract BlogPostArticle toBlogPostArticle(BlogPostArticleEntity blogPostArticleEntity);
}
