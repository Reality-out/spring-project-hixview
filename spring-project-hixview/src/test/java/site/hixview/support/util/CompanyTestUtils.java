package site.hixview.support.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Scale;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.entity.company.dto.CompanyDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_COMPANY_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_COMPANY_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_COMPANY_VIEW;
import static site.hixview.domain.vo.name.EntityName.Company.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_AFTER_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_SINGLE_PROCESS;

public interface CompanyTestUtils extends ObjectTestUtils {
    // Assertion
    String addSingleCompanyProcessPage = ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS;
    String modifyCompanyProcessPage = UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS;
    String modifyCompanyFinishUrl = UPDATE_COMPANY_URL + FINISH_URL;

    // Schema Name
    String TEST_COMPANIES_SCHEMA = "test_companies";

    // Test Object
    Company samsungElectronics = Company.builder().code("005930").listedCountry(Country.SOUTH_KOREA).scale(Scale.BIG).name("삼성전자")
            .firstCategory(FirstCategory.SEMICONDUCTOR).secondCategory(SecondCategory.SEMICONDUCTOR_MANUFACTURING).build();

    Company skHynix = Company.builder().code("000660").listedCountry(Country.SOUTH_KOREA).scale(Scale.BIG).name("SK하이닉스")
            .firstCategory(FirstCategory.SEMICONDUCTOR).secondCategory(SecondCategory.SEMICONDUCTOR_MANUFACTURING).build();

    /**
     * Create
     */
    default CompanyDto createSamsungElectronicsDto() {
        return samsungElectronics.toDto();
    }

    default CompanyDto createSKHynixDto() {
        return skHynix.toDto();
    }

    default CompanyDto copyCompanyDto(CompanyDto source) {
        CompanyDto target = new CompanyDto();

        target.setCode(source.getCode());
        target.setListedCountry(source.getListedCountry());
        target.setScale(source.getScale());
        target.setName(source.getName());
        target.setFirstCategory(source.getFirstCategory());
        target.setSecondCategory(source.getSecondCategory());

        return target;
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithCompany(String url, Company company) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(CODE, company.getCode())
                .param(LISTED_COUNTRY, company.getListedCountry().name())
                .param(SCALE, company.getScale().name())
                .param(NAME, company.getName())
                .param(FIRST_CATEGORY, company.getFirstCategory().name())
                .param(SECOND_CATEGORY, company.getSecondCategory().name());
    }

    default MockHttpServletRequestBuilder postWithCompanyDto(String url, CompanyDto companyDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(CODE, companyDto.getCode())
                .param(LISTED_COUNTRY, companyDto.getListedCountry())
                .param(SCALE, companyDto.getScale())
                .param(NAME, companyDto.getName())
                .param(FIRST_CATEGORY, companyDto.getFirstCategory())
                .param(SECOND_CATEGORY, companyDto.getSecondCategory());
    }
}
