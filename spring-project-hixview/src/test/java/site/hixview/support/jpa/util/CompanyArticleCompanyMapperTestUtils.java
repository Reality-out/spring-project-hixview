package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.CompanyArticleCompanyEntity;

public interface CompanyArticleCompanyMapperTestUtils extends CompanyArticleTestUtils, CompanyTestUtils{
    default CompanyArticleCompanyEntity createCompanyArticleCompanyMapper() {
        return new CompanyArticleCompanyEntity(createCompanyArticle(), createCompany());
    }

    default CompanyArticleCompanyEntity createAnotherCompanyArticleCompanyMapper() {
        return new CompanyArticleCompanyEntity(createAnotherCompanyArticle(), createAnotherCompany());
    }
}
