package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.dto.FirstCategoryDto;
import site.hixview.aggregate.dto.FirstCategoryDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Mapper
public interface FirstCategoryMapper {
    FirstCategory toFirstCategory(FirstCategoryDto firstCategoryDto);

    FirstCategoryDto toFirstCategoryDto(FirstCategory firstCategory);

    @Mapping(target = NUMBER, ignore = true)
    FirstCategory toFirstCategory(FirstCategoryDtoNoNumber firstCategoryDto);

    FirstCategoryDtoNoNumber toFirstCategoryDtoNoNumber(FirstCategory firstCategory);
}
