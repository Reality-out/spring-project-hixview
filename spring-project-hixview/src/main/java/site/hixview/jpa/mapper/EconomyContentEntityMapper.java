package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.jpa.entity.EconomyContentEntity;

import static site.hixview.aggregate.vo.WordCamel.VERSION_NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class EconomyContentEntityMapper {
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract EconomyContentEntity toEconomyContentEntity(EconomyContent economyContent);

    public abstract EconomyContent toEconomyContent(EconomyContentEntity economyContentEntity);
}
