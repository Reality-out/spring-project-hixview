package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.mapper.support.EconomyArticleEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class EconomyArticleEntityMapper extends EconomyArticleEntityMapperSupport {
    @Mapping(target = ARTICLE, ignore = true)
    @Mapping(target = PRESS, ignore = true)
    @Mapping(target = ECONOMY_ARTICLE, ignore = true)
    public abstract EconomyArticleEntity toEconomyArticleEntity(EconomyArticle economyArticle);

    @Mapping(source = ARTICLE, target = NUMBER, qualifiedByName = "numberToDomain")
    @Mapping(source = PRESS, target = PRESS_NUMBER, qualifiedByName = "pressNumberToDomain")
    @Mapping(target = MAPPED_ECONOMY_CONTENT_NUMBERS, ignore = true)
    public abstract EconomyArticle toEconomyArticle(EconomyArticleEntity economyArticleEntity);
}
