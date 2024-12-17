package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;
import site.hixview.aggregate.service.supers.CrudAllowedService;

import java.util.List;
import java.util.Optional;

public interface CompanyService extends CrudAllowedService<Company> {
    List<Company> getByCountryListed(Country countryListed);

    List<Company> getByScale(Scale scale);

    List<Company> getByFirstCategory(FirstCategory firstCategory);

    List<Company> getBySecondCategory(SecondCategory secondCategory);

    Optional<Company> getByCode(String code);

    Optional<Company> getByKoreanName(String koreanName);

    Optional<Company> getByEnglishName(String englishName);

    Optional<Company> getByNameListed(String nameListed);

    void removeByCode(String code);
}
