package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.Article;
import site.hixview.jpa.entity.ArticleEntity;

public interface ArticleEntityMapperSupport {
    @AfterMapping
    default void afterMappingToEntity(
            @MappingTarget ArticleEntity articleEntity, Article article) {
        articleEntity.updateNumber(article.getNumber());
    }
}
