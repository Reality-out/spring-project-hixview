package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.Article;
import site.hixview.jpa.entity.ArticleEntity;

public abstract class ArticleEntityMapperSupport {
    @AfterMapping
    public void afterMappingToEntity(
            @MappingTarget ArticleEntity articleEntity, Article article) {
        articleEntity.updateNumber(article.getNumber());
    }
}
