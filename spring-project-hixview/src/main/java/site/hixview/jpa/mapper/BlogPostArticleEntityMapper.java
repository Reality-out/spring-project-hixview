package site.hixview.jpa.mapper;

import org.mapstruct.*;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.mapper.support.BlogPostArticleEntityMapperSupport;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BlogPostArticleEntityMapper extends BlogPostArticleEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    BlogPostArticleEntity toBlogPostArticleEntity(BlogPostArticle blogPostArticle,
                                                  @Context BlogPostEntityRepository blogPostEntityRepository,
                                                  @Context ArticleEntityRepository articleEntityRepository);

    @Mapping(source = BLOG_POST, target = POST_NUMBER, qualifiedByName = "postNumberToDomain")
    @Mapping(source = ARTICLE, target = ARTICLE_NUMBER, qualifiedByName = "articleNumberToDomain")
    @Mapping(target = BLOG_POST_ARTICLE, ignore = true)
    BlogPostArticle toBlogPostArticle(BlogPostArticleEntity blogPostArticleEntity);
}
