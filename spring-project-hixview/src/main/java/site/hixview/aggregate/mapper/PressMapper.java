package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.dto.PressDto;
import site.hixview.aggregate.dto.PressDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Mapper
public interface PressMapper {
    Press toPress(PressDto pressDto);

    PressDto toPressDto(Press press);

    @Mapping(target = NUMBER, ignore = true)
    Press toPress(PressDtoNoNumber pressDto);

    PressDtoNoNumber toPressDtoNoNumber(Press press);
}
