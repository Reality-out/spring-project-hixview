package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.jpa.entity.EconomyContentEntity;

public interface EconomyContentEntityMapperSupport {
    @AfterMapping
    default EconomyContentEntity afterMappingToEntity(
            @MappingTarget EconomyContentEntity economyContentEntity, EconomyContent economyContent) {
        return new EconomyContentEntity(economyContent.getNumber(), economyContent.getName());
    }
}
