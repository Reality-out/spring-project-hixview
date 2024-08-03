package springsideproject1.springsideproject1build.utility.test;

import springsideproject1.springsideproject1build.domain.Company;

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
}
