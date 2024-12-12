package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.mapper.support.EconomyArticleContentEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class EconomyArticleContentEntityMapper extends EconomyArticleContentEntityMapperSupport {
    @Mapping(target = ECONOMY_ARTICLE, ignore = true)
    @Mapping(target = ECONOMY_CONTENT, ignore = true)
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract EconomyArticleContentEntity toEconomyArticleContentEntity(EconomyArticleContent economyArticleContent);

    @Mapping(source = ECONOMY_ARTICLE, target = ARTICLE_NUMBER, qualifiedByName = "articleNumberToDomain")
    @Mapping(source = ECONOMY_CONTENT, target = CONTENT_NUMBER, qualifiedByName = "contentNumberToDomain")
    public abstract EconomyArticleContent toEconomyArticleContent(EconomyArticleContentEntity economyArticleContentEntity);
}
