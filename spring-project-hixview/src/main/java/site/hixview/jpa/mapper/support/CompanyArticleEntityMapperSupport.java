package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
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

public abstract class CompanyArticleEntityMapperSupport extends SuperArticleEntityMapperSupport {
    @Autowired
    private ArticleEntityRepository articleEntityRepository;

    @Autowired
    private PressEntityRepository pressEntityRepository;

    @Autowired
    private CompanyArticleCompanyEntityRepository companyArticleCompanyRepository;

    @AfterMapping
    public CompanyArticleEntityBuilder afterMappingToEntity(
            @MappingTarget CompanyArticleEntityBuilder builder, CompanyArticle companyArticle) {
        return builder.article(articleEntityRepository.findByNumber(companyArticle.getNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(companyArticle.getNumber(), ArticleEntity.class)))
                .press(pressEntityRepository.findByNumber(companyArticle.getPressNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(companyArticle.getPressNumber(), PressEntity.class)));
    }

    @AfterMapping
    public CompanyArticleBuilder afterMappingToDomain(
            @MappingTarget CompanyArticleBuilder builder, CompanyArticleEntity entity) {
        return builder.mappedCompanyCodes(companyArticleCompanyRepository.findByCompanyArticle(entity)
                .stream().map(data -> data.getCompany().getCode()).toList());
    }
}
