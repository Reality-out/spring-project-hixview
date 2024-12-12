package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Press;
import site.hixview.jpa.entity.PressEntity;

import static site.hixview.aggregate.vo.WordCamel.VERSION_NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class PressEntityMapper {
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract PressEntity toPressEntity(Press press);

    public abstract Press toPress(PressEntity pressEntity);
}
