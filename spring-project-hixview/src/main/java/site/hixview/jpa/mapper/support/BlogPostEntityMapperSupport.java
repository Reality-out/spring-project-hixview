package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.domain.BlogPost.BlogPostBuilder;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.entity.BlogPostEntity.BlogPostEntityBuilder;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.PostEntityRepository;

import java.util.List;

public interface BlogPostEntityMapperSupport {
    @AfterMapping
    default void afterMappingToEntity(
            @MappingTarget BlogPostEntityBuilder blogPostEntityBuilder, BlogPost blogPost,
            @Context PostEntityRepository postEntityRepository) {
        blogPostEntityBuilder
                .post(postEntityRepository.findByNumber(blogPost.getNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(blogPost.getNumber(), PostEntity.class)));
    }

    @AfterMapping
    default void afterMappingToDomain(
            @MappingTarget BlogPostBuilder blogPostBuilder, BlogPostEntity blogPostEntity,
            @Context BlogPostArticleEntityRepository blogPostArticleRepository) {
        List<BlogPostArticleEntity> blogPostArticles = blogPostArticleRepository.findByBlogPost(blogPostEntity);
        blogPostBuilder.mappedArticleNumbers(blogPostArticles.stream()
                .map(data -> data.getArticle().getNumber()).toList());
    }
}
