package springsideproject1.springsideproject1build.util.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.entity.company.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.UPDATE_COMPANY_URL;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.URL_FINISH_SUFFIX;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

public interface CompanyTestUtils extends ObjectTestUtils {
    // Assertion
    String addSingleCompanyProcessPage = ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
    String modifyCompanyProcessPage = UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    String modifyCompanyFinishUrl = UPDATE_COMPANY_URL + URL_FINISH_SUFFIX;

    // Test Object
    Company samsungElectronics = Company.builder().code("005930").country(Country.SOUTH_KOREA).scale(Scale.BIG).name("삼성전자")
            .firstCategory(FirstCategory.SEMICONDUCTOR).secondCategory(SecondCategory.SEMICONDUCTOR_MANUFACTURING).build();

    Company skHynix = Company.builder().code("000660").country(Country.SOUTH_KOREA).scale(Scale.BIG).name("SK하이닉스")
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
        target.setCountry(source.getCountry());
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
                .param(COUNTRY, company.getCountry().name())
                .param(SCALE, company.getScale().name())
                .param(NAME, company.getName())
                .param(FIRST_CATEGORY, company.getFirstCategory().name())
                .param(SECOND_CATEGORY, company.getSecondCategory().name());
    }

    default MockHttpServletRequestBuilder postWithCompanyDto(String url, CompanyDto companyDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(CODE, companyDto.getCode())
                .param(COUNTRY, companyDto.getCountry())
                .param(SCALE, companyDto.getScale())
                .param(NAME, companyDto.getName())
                .param(FIRST_CATEGORY, companyDto.getFirstCategory())
                .param(SECOND_CATEGORY, companyDto.getSecondCategory());
    }
}
