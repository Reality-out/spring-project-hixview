package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.dto.FirstCategoryDto;
import site.hixview.aggregate.dto.FirstCategoryDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.FIRST_CATEGORY;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FirstCategoryMapper {
    @Mapping(target = FIRST_CATEGORY, ignore = true)
    FirstCategory toFirstCategory(FirstCategoryDto firstCategoryDto);

    FirstCategoryDto toFirstCategoryDto(FirstCategory firstCategory);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(target = FIRST_CATEGORY, ignore = true)
    FirstCategory toFirstCategory(FirstCategoryDtoNoNumber firstCategoryDto);

    FirstCategoryDtoNoNumber toFirstCategoryDtoNoNumber(FirstCategory firstCategory);
}
