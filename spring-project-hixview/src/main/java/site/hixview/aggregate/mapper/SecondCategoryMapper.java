package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.dto.SecondCategoryDto;
import site.hixview.aggregate.dto.SecondCategoryDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Mapper
public interface SecondCategoryMapper {
    SecondCategory toSecondCategory(SecondCategoryDto secondCategoryDto);

    SecondCategoryDto toSecondCategoryDto(SecondCategory secondCategory);

    @Mapping(target = NUMBER, ignore = true)
    SecondCategory toSecondCategory(SecondCategoryDtoNoNumber secondCategoryDto);

    SecondCategoryDtoNoNumber toSecondCategoryDtoNoNumber(SecondCategory secondCategory);
}
