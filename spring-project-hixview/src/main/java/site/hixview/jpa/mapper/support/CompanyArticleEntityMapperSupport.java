package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.domain.CompanyArticle.CompanyArticleBuilder;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.CompanyArticleEntity.CompanyArticleEntityBuilder;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.CompanyArticleCompanyEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

public interface CompanyArticleEntityMapperSupport extends SuperArticleEntityMapperSupport {
    @AfterMapping
    default void afterMappingToEntity(
            @MappingTarget CompanyArticleEntityBuilder builder, CompanyArticle companyArticle,
            @Context ArticleEntityRepository articleEntityRepository,
            @Context PressEntityRepository pressEntityRepository) {
        builder.article(articleEntityRepository.findByNumber(companyArticle.getNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(companyArticle.getNumber(), ArticleEntity.class)))
                .press(pressEntityRepository.findByNumber(companyArticle.getPressNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(companyArticle.getPressNumber(), PressEntity.class)));
    }

    @AfterMapping
    default void afterMappingToDomain(
            @MappingTarget CompanyArticleBuilder builder, CompanyArticleEntity entity,
            @Context CompanyArticleCompanyEntityRepository companyArticleCompanyRepository) {
        builder.mappedCompanyCodes(companyArticleCompanyRepository.findByCompanyArticle(entity)
                .stream().map(data -> data.getCompany().getCode()).toList());
    }
}
