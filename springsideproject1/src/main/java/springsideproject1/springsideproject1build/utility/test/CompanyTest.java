package springsideproject1.springsideproject1build.utility.test;

import springsideproject1.springsideproject1build.domain.Company;

public interface CompanyTest extends ObjectTest {

    String companyTable = "testcompanies";

    default Company createSamsungElectronics() {
        return new Company.CompanyBuilder()
                .code("005930")
                .country("South Korea")
                .scale("big")
                .name("삼성전자")
                .category1st("electronics")
                .category2nd("semiconductor")
                .build();
    }

    default Company createSKHynix() {
        return new Company.CompanyBuilder()
                .code("000660")
                .country("South Korea")
                .scale("big")
                .name("SK하이닉스")
                .category1st("electronics")
                .category2nd("semiconductor")
                .build();
    }
}
