package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.CompanyDto;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;

public interface CompanyDtoTestUtils {
    /**
     * Create
     */
    default CompanyDto createCompanyDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode("000270");
        companyDto.setKoreanName("기아");
        companyDto.setEnglishName("KIA");
        companyDto.setNameListed("기아");
        companyDto.setCountryListed(Country.SOUTH_KOREA.name());
        companyDto.setScale(Scale.BIG.name());
        companyDto.setFirstCategoryNumber(1L);
        companyDto.setSecondCategoryNumber(1L);
        return companyDto;
    }

    default CompanyDto createAnotherCompanyDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode("000660");
        companyDto.setKoreanName("SK하이닉스");
        companyDto.setEnglishName("SK_HYNIX");
        companyDto.setNameListed("SK하이닉스");
        companyDto.setCountryListed(Country.SOUTH_KOREA.name());
        companyDto.setScale(Scale.BIG.name());
        companyDto.setFirstCategoryNumber(2L);
        companyDto.setSecondCategoryNumber(2L);
        return companyDto;
    }
}
