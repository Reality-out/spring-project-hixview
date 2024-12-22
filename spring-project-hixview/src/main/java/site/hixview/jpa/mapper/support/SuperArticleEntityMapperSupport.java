package site.hixview.jpa.mapper.support;

import org.mapstruct.Named;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.PressEntity;

interface SuperArticleEntityMapperSupport {
    @Named("numberToDomain")
    default Long numberToDomain(ArticleEntity articleEntity) {
        return articleEntity.getNumber();
    }

    @Named("pressNumberToDomain")
    default Long pressNumberToDomain(PressEntity pressEntity) {
        return pressEntity.getNumber();
    }
}
