package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.domain.BlogPost.BlogPostBuilder;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.BlogPostArticleRepository;
import site.hixview.jpa.repository.PostRepository;

import java.util.List;

import static site.hixview.jpa.entity.BlogPostEntity.BlogPostEntityBuilder;

public abstract class BlogPostEntityMapperSupport {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BlogPostArticleRepository blogPostArticleRepository;

    @AfterMapping
    public BlogPostEntityBuilder afterMappingToEntity(
            @MappingTarget BlogPostEntityBuilder blogPostEntityBuilder, BlogPost blogPost) {
        return blogPostEntityBuilder.post(postRepository.findByNumber(blogPost.getNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(blogPost.getNumber(), PostEntity.class)));
    }

    @AfterMapping
    public BlogPostBuilder afterMappingToDomain(
            @MappingTarget BlogPostBuilder blogPostBuilder, BlogPostEntity blogPostEntity) {
        List<BlogPostArticleEntity> blogPostArticles = blogPostArticleRepository.findByBlogPost(blogPostEntity);
        return blogPostBuilder.mappedArticleNumbers(blogPostArticles.stream()
                .map(data -> data.getArticle().getNumber()).toList());
    }
}
