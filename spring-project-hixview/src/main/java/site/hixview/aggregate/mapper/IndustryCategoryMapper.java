package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.aggregate.dto.IndustryCategoryDto;
import site.hixview.aggregate.dto.IndustryCategoryDtoNoNumber;

import static site.hixview.aggregate.vo.WordCamel.INDUSTRY_CATEGORY;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IndustryCategoryMapper {
    @Mapping(target = INDUSTRY_CATEGORY, ignore = true)
    IndustryCategory toIndustryCategory(IndustryCategoryDto industryCategoryDto);

    IndustryCategoryDto toIndustryCategoryDto(IndustryCategory industryCategory);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(target = INDUSTRY_CATEGORY, ignore = true)
    IndustryCategory toIndustryCategory(IndustryCategoryDtoNoNumber industryCategoryDto);

    IndustryCategoryDtoNoNumber toIndustryCategoryDtoNoNumber(IndustryCategory industryCategory);
}
