package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.dto.SecondCategoryDto;
import site.hixview.aggregate.dto.SecondCategoryDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.NUMBER;
import static site.hixview.aggregate.vo.WordCamel.SECOND_CATEGORY;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SecondCategoryMapper {
    @Mapping(target = SECOND_CATEGORY, ignore = true)
    SecondCategory toSecondCategory(SecondCategoryDto secondCategoryDto);

    SecondCategoryDto toSecondCategoryDto(SecondCategory secondCategory);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(target = SECOND_CATEGORY, ignore = true)
    SecondCategory toSecondCategory(SecondCategoryDtoNoNumber secondCategoryDto);

    SecondCategoryDtoNoNumber toSecondCategoryDtoNoNumber(SecondCategory secondCategory);
}
