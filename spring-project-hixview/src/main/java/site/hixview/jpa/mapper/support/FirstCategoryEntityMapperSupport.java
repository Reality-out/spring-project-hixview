package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;

public interface FirstCategoryEntityMapperSupport {
    @AfterMapping
    default void afterMappingToEntity(
            @MappingTarget FirstCategoryEntity firstCategoryEntity, FirstCategory firstCategory,
            @Context IndustryCategoryEntityRepository industryCategoryRepository) {
        firstCategoryEntity.updateIndustryCategory(industryCategoryRepository.findByNumber(
                firstCategory.getIndustryCategoryNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(firstCategory.getIndustryCategoryNumber(), IndustryCategory.class)));
    }

    @Named("industryCategoryNumberToDomain")
    default Long industryCategoryNumberToDomain(IndustryCategoryEntity industryCategoryEntity) {
        return industryCategoryEntity.getNumber();
    }
}
