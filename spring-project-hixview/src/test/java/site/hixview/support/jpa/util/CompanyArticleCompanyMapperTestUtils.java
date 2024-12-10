package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.CompanyArticleCompanyMapperEntity;

public interface CompanyArticleCompanyMapperTestUtils extends CompanyArticleTestUtils, CompanyTestUtils{
    default CompanyArticleCompanyMapperEntity createCompanyArticleCompanyMapper() {
        return new CompanyArticleCompanyMapperEntity(createCompanyArticle(), createCompany());
    }

    default CompanyArticleCompanyMapperEntity createAnotherCompanyArticleCompanyMapper() {
        return new CompanyArticleCompanyMapperEntity(createAnotherCompanyArticle(), createAnotherCompany());
    }
}
