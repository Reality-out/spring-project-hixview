package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Article;
import site.hixview.jpa.entity.ArticleEntity;

import static site.hixview.aggregate.vo.WordCamel.VERSION_NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class ArticleEntityMapper {
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract ArticleEntity toArticleEntity(Article article);

    public abstract Article toArticle(ArticleEntity articleEntity);
}
