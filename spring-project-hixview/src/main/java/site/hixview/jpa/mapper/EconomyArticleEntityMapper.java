package site.hixview.jpa.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.mapper.support.EconomyArticleEntityMapperSupport;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.EconomyArticleContentEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper
public interface EconomyArticleEntityMapper extends EconomyArticleEntityMapperSupport {
    @Mapping(target = ARTICLE, ignore = true)
    @Mapping(target = PRESS, ignore = true)
    @Mapping(target = ECONOMY_ARTICLE, ignore = true)
    EconomyArticleEntity toEconomyArticleEntity(EconomyArticle economyArticle,
                                                @Context ArticleEntityRepository articleEntityRepository,
                                                @Context PressEntityRepository pressEntityRepository);

    @Mapping(source = ARTICLE, target = NUMBER, qualifiedByName = "numberToDomain")
    @Mapping(source = PRESS, target = PRESS_NUMBER, qualifiedByName = "pressNumberToDomain")
    @Mapping(target = MAPPED_ECONOMY_CONTENT_NUMBERS, ignore = true)
    @Mapping(target = ECONOMY_ARTICLE, ignore = true)
    EconomyArticle toEconomyArticle(EconomyArticleEntity economyArticleEntity,
                                    @Context EconomyArticleContentEntityRepository economyArticleContentRepository);
}
