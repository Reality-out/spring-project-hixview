package springsideproject1.springsideproject1build.util.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.entity.Country;
import springsideproject1.springsideproject1build.domain.entity.FirstCategory;
import springsideproject1.springsideproject1build.domain.entity.Scale;
import springsideproject1.springsideproject1build.domain.entity.SecondCategory;
import springsideproject1.springsideproject1build.domain.entity.company.Company;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.domain.vo.EntityName.Company.*;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.FINISH_URL;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.AFTER_PROCESS_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.SINGLE_PROCESS_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.Word.NAME;
import static springsideproject1.springsideproject1build.domain.vo.manager.RequestUrl.UPDATE_COMPANY_URL;
import static springsideproject1.springsideproject1build.domain.vo.manager.ViewName.ADD_COMPANY_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.manager.ViewName.UPDATE_COMPANY_VIEW;

public interface CompanyTestUtils extends ObjectTestUtils {
    // Assertion
    String addSingleCompanyProcessPage = ADD_COMPANY_VIEW + SINGLE_PROCESS_VIEW;
    String modifyCompanyProcessPage = UPDATE_COMPANY_VIEW + AFTER_PROCESS_VIEW;
    String modifyCompanyFinishUrl = UPDATE_COMPANY_URL + FINISH_URL;

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
