package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.mapper.support.FirstCategoryEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FirstCategoryEntityMapper extends FirstCategoryEntityMapperSupport {
    @Mapping(target = INDUSTRY_CATEGORY, ignore = true)
    @Mapping(target = VERSION_NUMBER, ignore = true)
    FirstCategoryEntity toFirstCategoryEntity(FirstCategory firstCategory);

    @Mapping(source = INDUSTRY_CATEGORY, target = INDUSTRY_CATEGORY_NUMBER, qualifiedByName = "industryCategoryNumberToDomain")
    FirstCategory toFirstCategory(FirstCategoryEntity firstCategoryEntity);
}
