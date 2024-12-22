package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.Press;
import site.hixview.jpa.entity.PressEntity;

public interface PressEntityMapperSupport {
    @AfterMapping
    default PressEntity afterMappingToEntity(
            @MappingTarget PressEntity ignoredEntity, Press press) {
        return new PressEntity(press.getNumber(), press.getKoreanName(), press.getEnglishName());
    }
}
