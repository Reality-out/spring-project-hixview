package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.mapper.support.EconomyContentEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.ECONOMY_CONTENT;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EconomyContentEntityMapper extends EconomyContentEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    EconomyContentEntity toEconomyContentEntity(EconomyContent economyContent);

    @Mapping(target = ECONOMY_CONTENT, ignore = true)
    EconomyContent toEconomyContent(EconomyContentEntity economyContentEntity);
}
