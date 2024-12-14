package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.dto.PressDto;
import site.hixview.aggregate.dto.PressDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.NUMBER;
import static site.hixview.aggregate.vo.WordCamel.PRESS;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PressMapper {
    @Mapping(target = PRESS, ignore = true)
    Press toPress(PressDto pressDto);

    PressDto toPressDto(Press press);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(target = PRESS, ignore = true)
    Press toPress(PressDtoNoNumber pressDto);

    PressDtoNoNumber toPressDtoNoNumber(Press press);
}
