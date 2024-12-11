package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.aggregate.domain.CompanyArticleCompany.CompanyArticleCompanyBuilder;
import site.hixview.aggregate.dto.CompanyArticleCompanyDto;

public class CompanyArticleCompanyFacade {
    public CompanyArticleCompanyBuilder createBuilder(CompanyArticleCompany mapper) {
        return CompanyArticleCompany.builder()
                .number(mapper.getNumber())
                .articleNumber(mapper.getArticleNumber())
                .companyCode(mapper.getCompanyCode());
    }

    public CompanyArticleCompanyBuilder createBuilder(CompanyArticleCompanyDto mapperDto) {
        return CompanyArticleCompany.builder()
                .number(mapperDto.getNumber())
                .articleNumber(mapperDto.getArticleNumber())
                .companyCode(mapperDto.getCompanyCode());
    }
}
