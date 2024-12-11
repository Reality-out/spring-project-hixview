package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.aggregate.dto.CompanyArticleCompanyDto;

@Mapper
public interface CompanyArticleCompanyMapper {
    CompanyArticleCompany toCompanyArticleCompany(CompanyArticleCompanyDto companyArticleCompanyDto);

    CompanyArticleCompanyDto toCompanyArticleCompanyDto(CompanyArticleCompany companyArticleCompany);
}
