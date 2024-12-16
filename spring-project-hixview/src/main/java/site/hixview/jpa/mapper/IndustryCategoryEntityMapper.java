package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.mapper.support.IndustryCategoryEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.INDUSTRY_CATEGORY;

@Mapper
public interface IndustryCategoryEntityMapper extends IndustryCategoryEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    IndustryCategoryEntity toIndustryCategoryEntity(IndustryCategory industryCategory);

    @Mapping(target = INDUSTRY_CATEGORY, ignore = true)
    IndustryCategory toIndustryCategory(IndustryCategoryEntity industryCategoryEntity);
}
