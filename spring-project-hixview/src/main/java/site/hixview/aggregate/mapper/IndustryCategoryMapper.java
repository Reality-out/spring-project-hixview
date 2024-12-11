package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.aggregate.dto.IndustryCategoryDto;
import site.hixview.aggregate.dto.IndustryCategoryDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Mapper
public interface IndustryCategoryMapper {
    IndustryCategory toIndustryCategory(IndustryCategoryDto industryCategoryDto);

    IndustryCategoryDto toIndustryCategoryDto(IndustryCategory industryCategory);

    @Mapping(target = NUMBER, ignore = true)
    IndustryCategory toIndustryCategory(IndustryCategoryDtoNoNumber industryCategoryDto);

    IndustryCategoryDtoNoNumber toIndustryCategoryDtoNoNumber(IndustryCategory industryCategory);
}
