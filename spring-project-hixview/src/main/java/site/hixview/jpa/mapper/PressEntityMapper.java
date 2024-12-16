package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.Press;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.mapper.support.PressEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.PRESS;

@Mapper
public interface PressEntityMapper extends PressEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    PressEntity toPressEntity(Press press);

    @Mapping(target = PRESS, ignore = true)
    Press toPress(PressEntity pressEntity);
}
