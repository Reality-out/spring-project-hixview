package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.CompanyArticleCompanyEntity;

public interface CompanyArticleCompanyEntityTestUtils extends CompanyArticleEntityTestUtils, CompanyEntityTestUtils {
    default CompanyArticleCompanyEntity createCompanyArticleCompany() {
        return new CompanyArticleCompanyEntity(createCompanyArticle(), createCompany());
    }

    default CompanyArticleCompanyEntity createAnotherCompanyArticleCompany() {
        return new CompanyArticleCompanyEntity(createAnotherCompanyArticle(), createAnotherCompany());
    }
}
