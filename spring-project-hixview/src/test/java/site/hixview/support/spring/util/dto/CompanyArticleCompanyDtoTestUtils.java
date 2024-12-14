package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.CompanyArticleCompanyDto;
import site.hixview.support.spring.util.CompanyArticleCompanyTestUtils;

public interface CompanyArticleCompanyDtoTestUtils extends CompanyArticleCompanyTestUtils {
    /**
     * Create
     */
    default CompanyArticleCompanyDto createCompanyArticleCompanyDto() {
        CompanyArticleCompanyDto companyArticleCompanyDto = new CompanyArticleCompanyDto();
        companyArticleCompanyDto.setNumber(companyArticleCompany.getNumber());
        companyArticleCompanyDto.setArticleNumber(companyArticleCompany.getArticleNumber());
        companyArticleCompanyDto.setCompanyCode(companyArticleCompany.getCompanyCode());
        return companyArticleCompanyDto;
    }

    default CompanyArticleCompanyDto createAnotherCompanyArticleCompanyDto() {
        CompanyArticleCompanyDto companyArticleCompanyDto = new CompanyArticleCompanyDto();
        companyArticleCompanyDto.setNumber(anotherCompanyArticleCompany.getNumber());
        companyArticleCompanyDto.setArticleNumber(anotherCompanyArticleCompany.getArticleNumber());
        companyArticleCompanyDto.setCompanyCode(anotherCompanyArticleCompany.getCompanyCode());
        return companyArticleCompanyDto;
    }
}
