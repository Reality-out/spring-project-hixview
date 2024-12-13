package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.CompanyArticleCompanyDto;

public interface CompanyArticleCompanyDtoTestUtils {
    /**
     * Create
     */
    default CompanyArticleCompanyDto createCompanyArticleCompanyDto() {
        CompanyArticleCompanyDto companyArticleCompanyDto = new CompanyArticleCompanyDto();
        companyArticleCompanyDto.setNumber(1L);
        companyArticleCompanyDto.setArticleNumber(1L);
        companyArticleCompanyDto.setCompanyCode("000270");
        return companyArticleCompanyDto;
    }

    default CompanyArticleCompanyDto createAnotherCompanyArticleCompanyDto() {
        CompanyArticleCompanyDto companyArticleCompanyDto = new CompanyArticleCompanyDto();
        companyArticleCompanyDto.setNumber(2L);
        companyArticleCompanyDto.setArticleNumber(2L);
        companyArticleCompanyDto.setCompanyCode("000660");
        return companyArticleCompanyDto;
    }
}
