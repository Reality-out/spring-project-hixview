package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.dto.EconomyArticleContentDto;

import static site.hixview.aggregate.vo.WordCamel.ECONOMY_ARTICLE_CONTENT;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EconomyArticleContentMapper {
    @Mapping(target = ECONOMY_ARTICLE_CONTENT, ignore = true)
    EconomyArticleContent toEconomyArticleContent(EconomyArticleContentDto economyArticleContentDto);

    EconomyArticleContentDto toEconomyArticleContentDto(EconomyArticleContent economyArticleContent);
}
