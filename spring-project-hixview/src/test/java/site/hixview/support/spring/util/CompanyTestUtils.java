package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;

public interface CompanyTestUtils {
    Company company = Company.builder()
            .code("000270")
            .koreanName("기아")
            .englishName("KIA")
            .nameListed("기아")
            .countryListed(Country.SOUTH_KOREA)
            .scale(Scale.BIG)
            .firstCategoryNumber(1L)
            .secondCategoryNumber(1L)
            .build();

    Company anotherCompany = Company.builder()
            .code("000660")
            .koreanName("SK하이닉스")
            .englishName("SK_HYNIX")
            .nameListed("SK하이닉스")
            .countryListed(Country.SOUTH_KOREA)
            .scale(Scale.BIG)
            .firstCategoryNumber(2L)
            .secondCategoryNumber(2L)
            .build();
}
