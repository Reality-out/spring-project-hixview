package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.CompanyDto;
import site.hixview.support.spring.util.CompanyTestUtils;

public interface CompanyDtoTestUtils extends CompanyTestUtils {
    /**
     * Create
     */
    default CompanyDto createCompanyDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(company.getCode());
        companyDto.setKoreanName(company.getKoreanName());
        companyDto.setEnglishName(company.getEnglishName());
        companyDto.setNameListed(company.getNameListed());
        companyDto.setCountryListed(company.getCountryListed().name());
        companyDto.setScale(company.getScale().name());
        companyDto.setFirstCategoryNumber(company.getFirstCategoryNumber());
        companyDto.setSecondCategoryNumber(company.getSecondCategoryNumber());
        return companyDto;
    }

    default CompanyDto createAnotherCompanyDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(anotherCompany.getCode());
        companyDto.setKoreanName(anotherCompany.getKoreanName());
        companyDto.setEnglishName(anotherCompany.getEnglishName());
        companyDto.setNameListed(anotherCompany.getNameListed());
        companyDto.setCountryListed(anotherCompany.getCountryListed().name());
        companyDto.setScale(anotherCompany.getScale().name());
        companyDto.setFirstCategoryNumber(anotherCompany.getFirstCategoryNumber());
        companyDto.setSecondCategoryNumber(anotherCompany.getSecondCategoryNumber());
        return companyDto;
    }
}
