package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;

public interface BlogPostArticleEntityMapperSupport {
    @AfterMapping
    default void afterMappingToEntity(@MappingTarget BlogPostArticleEntity entity, BlogPostArticle blogPostArticle,
                                      @Context BlogPostEntityRepository blogPostEntityRepository,
                                      @Context ArticleEntityRepository articleEntityRepository) {
        Long postNumber = blogPostArticle.getPostNumber();
        Long articleNumber = blogPostArticle.getArticleNumber();
        entity.updateBlogPost(blogPostEntityRepository.findByNumber(postNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(postNumber, BlogPostEntity.class)));
        entity.updateArticle(articleEntityRepository.findByNumber(articleNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(articleNumber, ArticleEntity.class)));
    }

    @Named("postNumberToDomain")
    default Long postNumberToDomain(BlogPostEntity blogPostEntity) {
        return blogPostEntity.getNumber();
    }

    @Named("articleNumberToDomain")
    default Long articleNumberToDomain(ArticleEntity articleEntity) {
        return articleEntity.getNumber();
    }
}
