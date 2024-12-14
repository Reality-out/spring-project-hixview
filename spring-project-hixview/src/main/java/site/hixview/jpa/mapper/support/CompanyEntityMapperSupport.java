package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.CompanyEntity.CompanyEntityBuilder;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;

public interface CompanyEntityMapperSupport {
    @AfterMapping
    default CompanyEntityBuilder afterMappingToEntity(
            @MappingTarget CompanyEntityBuilder companyEntityBuilder, Company company,
            @Context FirstCategoryEntityRepository firstCategoryRepository,
            @Context SecondCategoryEntityRepository secondCategoryRepository) {
        return companyEntityBuilder.firstCategory(firstCategoryRepository.findByNumber(company.getFirstCategoryNumber())
                        .orElseThrow(() -> new EntityNotFoundWithNumberException(company.getFirstCategoryNumber(),
                                FirstCategoryEntity.class)))
                .secondCategory(secondCategoryRepository.findByNumber(company.getSecondCategoryNumber())
                        .orElseThrow(() -> new EntityNotFoundWithNumberException(company.getSecondCategoryNumber(),
                                SecondCategoryEntity.class)));
    }

    @Named("firstCategoryNumberToDomain")
    default Long firstCategoryNumberToDomain(FirstCategoryEntity firstCategory) {
        return firstCategory.getNumber();
    }

    @Named("secondCategoryNumberToDomain")
    default Long secondCategoryNumberToDomain(SecondCategoryEntity secondCategory) {
        return secondCategory.getNumber();
    }
}
