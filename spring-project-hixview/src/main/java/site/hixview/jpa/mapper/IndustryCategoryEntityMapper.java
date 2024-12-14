package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.jpa.entity.IndustryCategoryEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IndustryCategoryEntityMapper {
    IndustryCategoryEntity toIndustryCategoryEntity(IndustryCategory industryCategory);

    IndustryCategory toIndustryCategory(IndustryCategoryEntity industryCategoryEntity);
}
