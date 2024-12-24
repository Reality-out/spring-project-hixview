package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.support.spring.util.CompanyArticleCompanyTestUtils;

public interface CompanyArticleCompanyEntityTestUtils extends CompanyArticleCompanyTestUtils, CompanyArticleEntityTestUtils, CompanyEntityTestUtils {
    default CompanyArticleCompanyEntity createCompanyArticleCompanyEntity() {
        return new CompanyArticleCompanyEntity(createCompanyArticleEntity(), createCompanyEntity());
    }

    default CompanyArticleCompanyEntity createAnotherCompanyArticleCompanyEntity() {
        return new CompanyArticleCompanyEntity(createAnotherCompanyArticleEntity(), createAnotherCompanyEntity());
    }

    default CompanyArticleCompanyEntity createNumberedCompanyArticleCompanyEntity() {
        return new CompanyArticleCompanyEntity(companyArticleCompany.getNumber(), createCompanyArticleEntity(), createCompanyEntity());
    }
}
