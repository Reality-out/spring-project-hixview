package springsideproject1.springsideproject1build.util.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.entity.company.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.CODE;

public interface CompanyTestUtils extends ObjectTestUtils {
    // Test Object
    Company samsungElectronics = Company.builder().code("005930").country(Country.SOUTH_KOREA).scale(Scale.BIG).name("삼성전자")
            .category1st(FirstCategory.SEMICONDUCTOR).category2nd(SecondCategory.SEMICONDUCTOR_MANUFACTURING).build();

    Company skHynix = Company.builder().code("000660").country(Country.SOUTH_KOREA).scale(Scale.BIG).name("SK하이닉스")
            .category1st(FirstCategory.SEMICONDUCTOR).category2nd(SecondCategory.SEMICONDUCTOR_MANUFACTURING).build();

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithCompany(String url, Company company) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(CODE, company.getCode())
                .param("country", company.getCountry().name())
                .param("scale", company.getScale().name())
                .param("name", company.getName())
                .param("category1st", company.getCategory1st().name())
                .param("category2nd", company.getCategory2nd().name());
    }
}