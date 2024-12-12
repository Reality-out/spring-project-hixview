package site.hixview.jpa.mapper.support;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.repository.CompanyArticleRepository;
import site.hixview.jpa.repository.CompanyRepository;

import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY_WITH_CODE;

public abstract class CompanyArticleCompanyEntityMapperSupport {
    @Autowired
    private CompanyArticleRepository companyArticleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @AfterMapping
    public void afterMappingToEntity(
            @MappingTarget CompanyArticleCompanyEntity entity, Long articleNumber, String companyCode) {
        entity.updateArticle(companyArticleRepository.findByNumber(articleNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(articleNumber, CompanyArticleEntity.class)));
        entity.updateCompany(companyRepository.findByCode(companyCode).orElseThrow(() ->
                new EntityNotFoundException(CANNOT_FOUND_ENTITY_WITH_CODE + companyCode +
                        " , for the class named: " + CompanyEntity.class.getSimpleName())));
    }

    @Named("articleNumberToDomain")
    public Long articleNumberToDomain(CompanyArticleEntity companyArticleEntity) {
        return companyArticleEntity.getNumber();
    }

    @Named("companyCodeToDomain")
    public String companyCodeToDomain(CompanyEntity companyEntity) {
        return companyEntity.getCode();
    }
}
