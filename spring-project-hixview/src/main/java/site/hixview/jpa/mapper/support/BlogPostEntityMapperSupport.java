package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.domain.BlogPost.BlogPostBuilder;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.PostEntityRepository;

import java.util.List;

import static site.hixview.jpa.entity.BlogPostEntity.BlogPostEntityBuilder;

public interface BlogPostEntityMapperSupport {
    @AfterMapping
    default BlogPostEntityBuilder afterMappingToEntity(
            @MappingTarget BlogPostEntityBuilder blogPostEntityBuilder, BlogPost blogPost,
            @Context PostEntityRepository postEntityRepository) {
        return blogPostEntityBuilder.post(postEntityRepository.findByNumber(blogPost.getNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(blogPost.getNumber(), PostEntity.class)));
    }

    @AfterMapping
    default BlogPostBuilder afterMappingToDomain(
            @MappingTarget BlogPostBuilder blogPostBuilder, BlogPostEntity blogPostEntity,
            @Context BlogPostArticleEntityRepository blogPostArticleRepository) {
        List<BlogPostArticleEntity> blogPostArticles = blogPostArticleRepository.findByBlogPost(blogPostEntity);
        return blogPostBuilder.mappedArticleNumbers(blogPostArticles.stream()
                .map(data -> data.getArticle().getNumber()).toList());
    }
}
