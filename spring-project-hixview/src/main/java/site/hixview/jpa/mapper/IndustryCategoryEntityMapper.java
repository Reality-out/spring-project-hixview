package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.jpa.entity.IndustryCategoryEntity;

import static site.hixview.aggregate.vo.WordCamel.INDUSTRY_CATEGORY;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IndustryCategoryEntityMapper {
    IndustryCategoryEntity toIndustryCategoryEntity(IndustryCategory industryCategory);

    @Mapping(target = INDUSTRY_CATEGORY, ignore = true)
    IndustryCategory toIndustryCategory(IndustryCategoryEntity industryCategoryEntity);
}
