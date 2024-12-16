package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.mapper.support.SecondCategoryEntityMapperSupport;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper
public interface SecondCategoryEntityMapper extends SecondCategoryEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    SecondCategoryEntity toSecondCategoryEntity(SecondCategory secondCategory,
                                                @Context IndustryCategoryEntityRepository industryCategoryRepository,
                                                @Context FirstCategoryEntityRepository firstCategoryRepository);

    @Mapping(source = INDUSTRY_CATEGORY, target = INDUSTRY_CATEGORY_NUMBER, qualifiedByName = "industryCategoryNumberToDomain")
    @Mapping(source = FIRST_CATEGORY, target = FIRST_CATEGORY_NUMBER, qualifiedByName = "firstCategoryNumberToDomain")
    @Mapping(target = SECOND_CATEGORY, ignore = true)
    SecondCategory toSecondCategory(SecondCategoryEntity secondCategoryEntity);
}
