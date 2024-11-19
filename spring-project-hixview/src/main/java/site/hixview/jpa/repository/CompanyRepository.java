package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
    /**
     * SELECT Company
     */
    List<CompanyEntity> findByCountryListed(String countryListed);

    List<CompanyEntity> findByScale(String scale);

    List<CompanyEntity> findByFirstCategory(FirstCategoryEntity firstCategory);

    List<CompanyEntity> findBySecondCategory(SecondCategoryEntity secondCategory);

    Optional<CompanyEntity> findByCode(String code);

    Optional<CompanyEntity> findByKoreanName(String koreanName);

    Optional<CompanyEntity> findByEnglishName(String englishName);

    Optional<CompanyEntity> findByNameListed(String nameListed);

    /**
     * REMOVE Company
     */
    @Transactional
    void deleteByCode(String code);
}