package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;

public abstract class BlogPostArticleEntityMapperSupport {
    @Autowired
    private BlogPostEntityRepository blogPostRepository;

    @Autowired
    private ArticleEntityRepository articleEntityRepository;

    @AfterMapping
    public void afterMappingToEntity(@MappingTarget BlogPostArticleEntity entity, Long postNumber, Long articleNumber) {
        entity.updatePost(blogPostRepository.findByNumber(postNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(postNumber, BlogPostEntity.class)));
        entity.updateArticle(articleEntityRepository.findByNumber(articleNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(articleNumber, ArticleEntity.class)));
    }

    @Named("postNumberToDomain")
    public Long postNumberToDomain(BlogPostEntity blogPostEntity) {
        return blogPostEntity.getNumber();
    }

    @Named("articleNumberToDomain")
    public Long articleNumberToDomain(ArticleEntity articleEntity) {
        return articleEntity.getNumber();
    }
}
