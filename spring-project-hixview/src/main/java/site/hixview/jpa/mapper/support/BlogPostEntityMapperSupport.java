package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.domain.BlogPost.BlogPostBuilder;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.PostEntityRepository;

import java.util.List;

public interface BlogPostEntityMapperSupport {
    @AfterMapping
    default BlogPostEntity afterMappingToEntity(
            @MappingTarget BlogPostEntity blogPostEntity, BlogPost blogPost,
            @Context PostEntityRepository postEntityRepository) {
        Logger log = LoggerFactory.getLogger(BlogPostEntityMapperSupport.class);
        return BlogPostEntity.builder()
                .blogPost(blogPostEntity)
                .post(postEntityRepository.findByNumber(blogPost.getNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(blogPost.getNumber(), PostEntity.class))).build();
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
