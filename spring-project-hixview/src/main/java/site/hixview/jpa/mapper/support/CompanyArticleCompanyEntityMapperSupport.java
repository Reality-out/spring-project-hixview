package site.hixview.jpa.mapper.support;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.repository.CompanyArticleEntityRepository;
import site.hixview.jpa.repository.CompanyEntityRepository;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.CODE;

public interface CompanyArticleCompanyEntityMapperSupport {
    @AfterMapping
    default CompanyArticleCompanyEntity afterMappingToEntity(
            @MappingTarget CompanyArticleCompanyEntity ignoredEntity, CompanyArticleCompany companyArticleCompany,
            @Context CompanyArticleEntityRepository companyArticleEntityRepository,
            @Context CompanyEntityRepository companyEntityRepository) {
        Long articleNumber = companyArticleCompany.getArticleNumber();
        String companyCode = companyArticleCompany.getCompanyCode();
        return new CompanyArticleCompanyEntity(companyArticleCompany.getNumber(),
                companyArticleEntityRepository.findByNumber(articleNumber).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(articleNumber, CompanyArticleEntity.class)),
                companyEntityRepository.findByCode(companyCode).orElseThrow(() ->
                        new EntityNotFoundException(getFormattedExceptionMessage(
                                CANNOT_FOUND_ENTITY, CODE, companyCode, CompanyEntity.class))));
    }

    @Named("articleNumberToDomain")
    default Long articleNumberToDomain(CompanyArticleEntity companyArticleEntity) {
        return companyArticleEntity.getNumber();
    }

    @Named("companyCodeToDomain")
    default String companyCodeToDomain(CompanyEntity companyEntity) {
        return companyEntity.getCode();
    }
}
