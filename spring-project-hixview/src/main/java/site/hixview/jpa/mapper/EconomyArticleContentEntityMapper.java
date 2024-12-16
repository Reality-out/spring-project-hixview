package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.mapper.support.EconomyArticleContentEntityMapperSupport;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.EconomyContentEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper
public interface EconomyArticleContentEntityMapper extends EconomyArticleContentEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    EconomyArticleContentEntity toEconomyArticleContentEntity(EconomyArticleContent economyArticleContent,
                                                              @Context EconomyArticleEntityRepository economyArticleRepository,
                                                              @Context EconomyContentEntityRepository economyContentEntityRepository);

    @Mapping(source = ECONOMY_ARTICLE, target = ARTICLE_NUMBER, qualifiedByName = "articleNumberToDomain")
    @Mapping(source = ECONOMY_CONTENT, target = CONTENT_NUMBER, qualifiedByName = "contentNumberToDomain")
    @Mapping(target = ECONOMY_ARTICLE_CONTENT, ignore = true)
    EconomyArticleContent toEconomyArticleContent(EconomyArticleContentEntity economyArticleContentEntity);
}
