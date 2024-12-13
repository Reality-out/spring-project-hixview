package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.CompanyArticleCompanyEntity;

public interface CompanyArticleCompanyEntityTestUtils extends CompanyArticleEntityTestUtils, CompanyEntityTestUtils {
    default CompanyArticleCompanyEntity createCompanyArticleCompanyEntity() {
        return new CompanyArticleCompanyEntity(createCompanyArticleEntity(), createCompanyEntity());
    }

    default CompanyArticleCompanyEntity createAnotherCompanyArticleCompanyEntity() {
        return new CompanyArticleCompanyEntity(createAnotherCompanyArticleEntity(), createAnotherCompanyEntity());
    }
}
