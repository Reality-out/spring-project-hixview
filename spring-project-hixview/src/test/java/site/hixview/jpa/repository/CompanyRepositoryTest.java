package site.hixview.jpa.repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.executor.SqlExecutor;
import site.hixview.support.jpa.util.CompanyTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class CompanyRepositoryTest implements CompanyTestUtils {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FirstCategoryRepository firstCategoryRepository;

    @Autowired
    private SecondCategoryRepository secondCategoryRepository;

    @Autowired
    private IndustryCategoryRepository industryCategoryRepository;

    @BeforeAll
    public static void beforeAll(@Autowired ApplicationContext applicationContext) {
        new SqlExecutor().resetTable(applicationContext);
    }

    private static final Logger log = LoggerFactory.getLogger(CompanyRepositoryTest.class);

    @DisplayName("상장된 국가로 기업 찾기")
    @Test
    void findByCountryListedTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyRepository.save(company);

        // then
        assertThat(companyRepository.findByCountryListed(company.getCountryListed())).isEqualTo(List.of(company));
    }

    @DisplayName("규모로 기업 찾기")
    @Test
    void findByScaleTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyRepository.save(company);

        // then
        assertThat(companyRepository.findByScale(company.getScale())).isEqualTo(List.of(company));
    }

    @DisplayName("1차 업종으로 기업 찾기")
    @Test
    void findByFirstCategoryTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyRepository.save(company);

        // then
        assertThat(companyRepository.findByFirstCategory(company.getFirstCategory())).isEqualTo(List.of(company));
    }

    @DisplayName("2차 업종으로 기업 찾기")
    @Test
    void findBySecondCategoryTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyRepository.save(company);

        // then
        assertThat(companyRepository.findBySecondCategory(company.getSecondCategory())).isEqualTo(List.of(company));
    }

    @DisplayName("코드로 기업 찾기")
    @Test
    void findByCodeTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyRepository.save(company);

        // then
        assertThat(companyRepository.findByCode(company.getCode()).orElseThrow()).isEqualTo(company);
    }

    @DisplayName("한글명으로 기업 찾기")
    @Test
    void findByKoreanNameTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyRepository.save(company);

        // then
        assertThat(companyRepository.findByKoreanName(company.getKoreanName()).orElseThrow()).isEqualTo(company);
    }

    @DisplayName("영문명으로 기업 찾기")
    @Test
    void findByEnglishNameTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyRepository.save(company);

        // then
        assertThat(companyRepository.findByEnglishName(company.getEnglishName()).orElseThrow()).isEqualTo(company);
    }

    @DisplayName("상장된 이름으로 기업 찾기")
    @Test
    void findByNameListedTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyRepository.save(company);

        // then
        assertThat(companyRepository.findByNameListed(company.getNameListed()).orElseThrow()).isEqualTo(company);
    }

    @DisplayName("코드로 기업 삭제")
    @Test
    void deleteByCodeTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyRepository.save(company);

        // when
        companyRepository.deleteByCode(company.getCode());

        // then
        assertThat(companyRepository.findAll()).isEmpty();
    }

    @DisplayName("코드로 기업 확인")
    @Test
    void existsByNumberTest() {
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyRepository.save(company);

        // then
        assertThat(companyRepository.existsByCode(company.getCode())).isEqualTo(true);
    }
}