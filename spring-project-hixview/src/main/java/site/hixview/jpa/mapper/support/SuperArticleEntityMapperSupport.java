package site.hixview.jpa.mapper.support;

import org.mapstruct.Named;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.PressEntity;

abstract class SuperArticleEntityMapperSupport {
    @Named("numberToDomain")
    public Long numberToDomain(ArticleEntity article) {
        return article.getNumber();
    }

    @Named("pressNumberToDomain")
    public Long pressNumberToDomain(PressEntity press) {
        return press.getNumber();
    }
}
