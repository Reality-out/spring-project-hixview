package site.hixview.jpa.mapper;

import org.mapstruct.*;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.mapper.support.FirstCategoryEntityMapperSupport;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FirstCategoryEntityMapper extends FirstCategoryEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    FirstCategoryEntity toFirstCategoryEntity(FirstCategory firstCategory,
                                              @Context IndustryCategoryEntityRepository industryCategoryRepository);

    @Mapping(source = INDUSTRY_CATEGORY, target = INDUSTRY_CATEGORY_NUMBER, qualifiedByName = "industryCategoryNumberToDomain")
    @Mapping(target = FIRST_CATEGORY, ignore = true)
    FirstCategory toFirstCategory(FirstCategoryEntity firstCategoryEntity);
}
