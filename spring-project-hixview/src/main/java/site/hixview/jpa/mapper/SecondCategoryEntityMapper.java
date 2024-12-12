package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.mapper.support.SecondCategoryEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class SecondCategoryEntityMapper extends SecondCategoryEntityMapperSupport {
    @Mapping(target = INDUSTRY_CATEGORY, ignore = true)
    @Mapping(target = FIRST_CATEGORY, ignore = true)
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract SecondCategoryEntity toSecondCategoryEntity(SecondCategory secondCategory);

    @Mapping(source = INDUSTRY_CATEGORY, target = INDUSTRY_CATEGORY_NUMBER, qualifiedByName = "industryCategoryNumberToDomain")
    @Mapping(source = FIRST_CATEGORY, target = FIRST_CATEGORY_NUMBER, qualifiedByName = "firstCategoryNumberToDomain")
    public abstract SecondCategory toSecondCategory(SecondCategoryEntity secondCategoryEntity);
}
