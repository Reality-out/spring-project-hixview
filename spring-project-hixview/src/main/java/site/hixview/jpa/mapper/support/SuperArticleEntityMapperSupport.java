package site.hixview.jpa.mapper.support;

import org.mapstruct.Named;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.PressEntity;

interface SuperArticleEntityMapperSupport {
    @Named("numberToDomain")
    default Long numberToDomain(ArticleEntity article) {
        return article.getNumber();
    }

    @Named("pressNumberToDomain")
    default Long pressNumberToDomain(PressEntity press) {
        return press.getNumber();
    }
}
