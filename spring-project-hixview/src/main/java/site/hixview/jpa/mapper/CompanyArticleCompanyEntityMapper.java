package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.mapper.support.CompanyArticleCompanyEntityMapperSupport;
import site.hixview.jpa.repository.CompanyArticleEntityRepository;
import site.hixview.jpa.repository.CompanyEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper
public interface CompanyArticleCompanyEntityMapper extends CompanyArticleCompanyEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    CompanyArticleCompanyEntity toCompanyArticleCompanyEntity(CompanyArticleCompany companyArticleCompany,
                                                              @Context CompanyArticleEntityRepository companyArticleRepository,
                                                              @Context CompanyEntityRepository companyEntityRepository);

    @Mapping(source = COMPANY_ARTICLE, target = ARTICLE_NUMBER, qualifiedByName = "articleNumberToDomain")
    @Mapping(source = COMPANY, target = COMPANY_CODE, qualifiedByName = "companyCodeToDomain")
    @Mapping(target = COMPANY_ARTICLE_COMPANY, ignore = true)
    CompanyArticleCompany toCompanyArticleCompany(CompanyArticleCompanyEntity companyArticleCompanyEntity);
}
