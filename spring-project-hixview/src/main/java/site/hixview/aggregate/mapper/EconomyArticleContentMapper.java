package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.dto.EconomyArticleContentDto;

@Mapper
public interface EconomyArticleContentMapper {
    EconomyArticleContent toEconomyArticleContent(EconomyArticleContentDto economyArticleContentDto);

    EconomyArticleContentDto toEconomyArticleContentDto(EconomyArticleContent economyArticleContent);
}
