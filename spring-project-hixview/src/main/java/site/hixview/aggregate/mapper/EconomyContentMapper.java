package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.dto.EconomyContentDto;

import static site.hixview.aggregate.vo.WordCamel.ECONOMY_CONTENT;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EconomyContentMapper {
    @Mapping(target = ECONOMY_CONTENT, ignore = true)
    EconomyContent toEconomyContent(EconomyContentDto economyContentDto);

    EconomyContentDto toEconomyContentDto(EconomyContent economyContent);
}
