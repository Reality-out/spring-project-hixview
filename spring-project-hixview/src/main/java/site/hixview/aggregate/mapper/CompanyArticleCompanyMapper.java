package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.aggregate.dto.CompanyArticleCompanyDto;

import static site.hixview.aggregate.vo.WordCamel.COMPANY_ARTICLE_COMPANY;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompanyArticleCompanyMapper {
    @Mapping(target = COMPANY_ARTICLE_COMPANY, ignore = true)
    CompanyArticleCompany toCompanyArticleCompany(CompanyArticleCompanyDto companyArticleCompanyDto);

    CompanyArticleCompanyDto toCompanyArticleCompanyDto(CompanyArticleCompany companyArticleCompany);
}
