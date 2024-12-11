package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.dto.EconomyContentDto;

@Mapper
public interface EconomyContentMapper {
    EconomyContent toEconomyContent(EconomyContentDto economyContentDto);

    EconomyContentDto toEconomyContentDto(EconomyContent economyContent);
}
