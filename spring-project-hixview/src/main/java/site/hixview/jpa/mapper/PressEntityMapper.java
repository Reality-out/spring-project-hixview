package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Press;
import site.hixview.jpa.entity.PressEntity;

import static site.hixview.aggregate.vo.WordCamel.PRESS;
import static site.hixview.aggregate.vo.WordCamel.VERSION_NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PressEntityMapper {
    @Mapping(target = VERSION_NUMBER, ignore = true)
    PressEntity toPressEntity(Press press);

    @Mapping(target = PRESS, ignore = true)
    Press toPress(PressEntity pressEntity);
}
