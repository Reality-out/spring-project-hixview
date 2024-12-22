package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.Article;
import site.hixview.jpa.entity.ArticleEntity;

public interface ArticleEntityMapperSupport {
    @AfterMapping
    default ArticleEntity afterMappingToEntity(
            @MappingTarget ArticleEntity ignoredEntity, Article article) {
        ArticleEntity newArticleEntity = new ArticleEntity();
        newArticleEntity.updateNumber(article.getNumber());
        return newArticleEntity;
    }
}
