package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Article;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.mapper.support.ArticleEntityMapperSupport;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ArticleEntityMapper extends ArticleEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    ArticleEntity toArticleEntity(Article article);

    Article toArticle(ArticleEntity articleEntity);
}
