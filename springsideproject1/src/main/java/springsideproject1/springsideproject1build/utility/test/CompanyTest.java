package springsideproject1.springsideproject1build.utility.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.Company;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.utility.ConstantUtility.CODE;

public interface CompanyTest extends ObjectTest {

    // DB table name
    String companyTable = "testcompanies";

    /**
     * Create
     */
    default Company createSamsungElectronics() {
        return Company.builder().code("005930").country("South Korea").scale("big").name("삼성전자")
                .category1st("electronics").category2nd("semiconductor").build();
    }

    default Company createSKHynix() {
        return Company.builder().code("000660").country("South Korea").scale("big").name("SK하이닉스")
                .category1st("electronics").category2nd("semiconductor").build();
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder processPostWithCompany(String url, Company company) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(CODE, company.getCode())
                .param("country", company.getCountry())
                .param("scale", company.getScale())
                .param("name", company.getName())
                .param("category1st", company.getCategory1st())
                .param("category2nd", company.getCategory2nd());
    }
}
