package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;

public interface FirstCategoryEntityMapperSupport {
    @AfterMapping
    default FirstCategoryEntity afterMappingToEntity(
            @MappingTarget FirstCategoryEntity ignoredEntity, FirstCategory firstCategory,
            @Context IndustryCategoryEntityRepository industryCategoryEntityRepository) {
        return new FirstCategoryEntity(firstCategory.getNumber(),
                firstCategory.getKoreanName(),
                firstCategory.getEnglishName(),
                industryCategoryEntityRepository.findByNumber(
                        firstCategory.getIndustryCategoryNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(firstCategory.getIndustryCategoryNumber(), IndustryCategoryEntity.class)));
    }

    @Named("industryCategoryNumberToDomain")
    default Long industryCategoryNumberToDomain(IndustryCategoryEntity industryCategoryEntity) {
        return industryCategoryEntity.getNumber();
    }
}
