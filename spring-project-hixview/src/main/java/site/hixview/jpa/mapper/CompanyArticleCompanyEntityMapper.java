package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.mapper.support.CompanyArticleCompanyEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class CompanyArticleCompanyEntityMapper extends CompanyArticleCompanyEntityMapperSupport {
    @Mapping(target = COMPANY_ARTICLE, ignore = true)
    @Mapping(target = COMPANY, ignore = true)
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract CompanyArticleCompanyEntity toCompanyArticleCompanyEntity(CompanyArticleCompany companyArticleCompany);

    @Mapping(source = COMPANY_ARTICLE, target = ARTICLE_NUMBER, qualifiedByName = "articleNumberToDomain")
    @Mapping(source = COMPANY, target = COMPANY_CODE, qualifiedByName = "companyCodeToDomain")
    public abstract CompanyArticleCompany toCompanyArticleCompany(CompanyArticleCompanyEntity companyArticleCompanyEntity);
}
