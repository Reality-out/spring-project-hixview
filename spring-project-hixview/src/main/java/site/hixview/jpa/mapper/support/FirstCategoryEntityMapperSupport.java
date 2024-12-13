package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;

public abstract class FirstCategoryEntityMapperSupport {
    @Autowired
    private IndustryCategoryEntityRepository industryCategoryRepository;

    @AfterMapping
    public void afterMappingToEntity(
            @MappingTarget FirstCategoryEntity firstCategoryEntity, FirstCategory firstCategory) {
        firstCategoryEntity.updateIndustryCategory(industryCategoryRepository.findByNumber(
                firstCategory.getIndustryCategoryNumber()).orElseThrow(() ->
                new EntityNotFoundWithNumberException(firstCategory.getIndustryCategoryNumber(), IndustryCategory.class)));
    }

    @Named("industryCategoryNumberToDomain")
    public Long industryCategoryNumberToDomain(IndustryCategoryEntity industryCategoryEntity) {
        return industryCategoryEntity.getNumber();
    }
}
