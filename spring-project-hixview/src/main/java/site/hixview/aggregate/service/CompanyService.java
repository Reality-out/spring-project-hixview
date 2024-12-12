package site.hixview.aggregate.service;

import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.service.supers.Service;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CompanyService extends Service<Company> {
    List<Company> getByCountryListed(String countryListed);

    List<Company> getByScale(String scale);

    List<Company> getByFirstCategory(FirstCategoryEntity firstCategory);

    List<Company> getBySecondCategory(SecondCategoryEntity secondCategory);

    Optional<Company> getByCode(String code);

    Optional<Company> getByKoreanName(String koreanName);

    Optional<Company> getByEnglishName(String englishName);

    Optional<Company> getByNameListed(String nameListed);

    void removeByCode(String code);
}
