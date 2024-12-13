package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.CompanyArticleCompany;

public interface CompanyArticleCompanyTestUtils {
    CompanyArticleCompany companyArticleCompany = CompanyArticleCompany.builder()
            .number(1L).articleNumber(1L).companyCode("000270").build();

    CompanyArticleCompany anotherCompanyArticleCompany = CompanyArticleCompany.builder()
            .number(2L).articleNumber(2L).companyCode("000660").build();
}
