package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;

public interface ArticleEntityMapperSupport {
    @AfterMapping
    default ArticleEntity afterMappingToEntity(
            @MappingTarget ArticleEntity articleEntity, Article article,
            @Context ArticleEntityRepository articleEntityRepository) {
        return articleEntityRepository.findByNumber(article.getNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(article.getNumber(), ArticleEntity.class));
    }
}
