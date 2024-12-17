package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;

public interface SecondCategoryEntityMapperSupport {
    @AfterMapping
    default SecondCategoryEntity afterMappingToEntity(
            @MappingTarget SecondCategoryEntity ignoredSecondCategoryEntity, SecondCategory secondCategory,
            @Context IndustryCategoryEntityRepository industryCategoryRepository,
            @Context FirstCategoryEntityRepository firstCategoryRepository) {
        return new SecondCategoryEntity(secondCategory.getNumber(),
                secondCategory.getKoreanName(),
                secondCategory.getEnglishName(),
                industryCategoryRepository.findByNumber(
                        secondCategory.getIndustryCategoryNumber()).orElseThrow(() -> new EntityNotFoundWithNumberException(
                                secondCategory.getIndustryCategoryNumber(), IndustryCategoryEntity.class)),
                firstCategoryRepository.findByNumber(
                        secondCategory.getFirstCategoryNumber()).orElseThrow(() -> new EntityNotFoundWithNumberException(
                                secondCategory.getFirstCategoryNumber(), FirstCategoryEntity.class)));
    }

    @Named("industryCategoryNumberToDomain")
    default Long industryCategoryNumberToDomain(IndustryCategoryEntity industryCategoryEntity) {
        return industryCategoryEntity.getNumber();
    }

    @Named("firstCategoryNumberToDomain")
    default Long firstCategoryNumberToDomain(FirstCategoryEntity firstCategoryEntity) {
        return firstCategoryEntity.getNumber();
    }
}
