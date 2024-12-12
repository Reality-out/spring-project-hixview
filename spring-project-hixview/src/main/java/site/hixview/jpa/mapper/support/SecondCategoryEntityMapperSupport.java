package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.repository.FirstCategoryRepository;
import site.hixview.jpa.repository.IndustryCategoryRepository;

public abstract class SecondCategoryEntityMapperSupport {
    @Autowired
    private IndustryCategoryRepository industryCategoryRepository;

    @Autowired
    private FirstCategoryRepository firstCategoryRepository;

    @AfterMapping
    public void afterMappingToEntity(
            @MappingTarget SecondCategoryEntity secondCategoryEntity, SecondCategory secondCategory) {
        secondCategoryEntity.updateIndustryCategory(industryCategoryRepository.findByNumber(
                secondCategory.getIndustryCategoryNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(secondCategory.getIndustryCategoryNumber(), IndustryCategory.class)));

        secondCategoryEntity.updateFirstCategory(firstCategoryRepository.findByNumber(
                secondCategory.getFirstCategoryNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(secondCategory.getFirstCategoryNumber(), FirstCategory.class)));
    }

    @Named("industryCategoryNumberToDomain")
    public Long industryCategoryNumberToDomain(IndustryCategoryEntity industryCategoryEntity) {
        return industryCategoryEntity.getNumber();
    }

    @Named("firstCategoryNumberToDomain")
    public Long firstCategoryNumberToDomain(FirstCategoryEntity firstCategoryEntity) {
        return firstCategoryEntity.getNumber();
    }
}
