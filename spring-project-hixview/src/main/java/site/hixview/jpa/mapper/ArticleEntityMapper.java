package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Article;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.mapper.support.ArticleEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.NUMBER;
import static site.hixview.aggregate.vo.WordCamel.VERSION_NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class ArticleEntityMapper extends ArticleEntityMapperSupport {
    @Mapping(target = NUMBER, ignore = true)
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract ArticleEntity toArticleEntity(Article article);

    @Mapping(source = NUMBER, target = NUMBER)
    public abstract Article toArticle(ArticleEntity articleEntity);
}
