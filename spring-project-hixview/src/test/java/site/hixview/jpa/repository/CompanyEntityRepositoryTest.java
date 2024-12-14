package site.hixview.jpa.repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.CompanyEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.COMPANY;
import static site.hixview.aggregate.vo.WordSnake.*;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class CompanyEntityRepositoryTest implements CompanyEntityTestUtils {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    private final CompanyEntityRepository companyEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryRepository;
    private final SecondCategoryEntityRepository secondCategoryRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + COMPANY,
            TEST_TABLE_PREFIX + SECOND_CATEGORY_SNAKE, TEST_TABLE_PREFIX + FIRST_CATEGORY_SNAKE,
            TEST_TABLE_PREFIX + INDUSTRY_CATEGORY_SNAKE};

    @Autowired
    CompanyEntityRepositoryTest(CompanyEntityRepository companyEntityRepository, FirstCategoryEntityRepository firstCategoryRepository, SecondCategoryEntityRepository secondCategoryRepository, IndustryCategoryEntityRepository industryCategoryRepository, JdbcTemplate jdbcTemplate) {
        this.companyEntityRepository = companyEntityRepository;
        this.firstCategoryRepository = firstCategoryRepository;
        this.secondCategoryRepository = secondCategoryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    private static final Logger log = LoggerFactory.getLogger(CompanyEntityRepositoryTest.class);

    @DisplayName("상장된 국가로 기업 찾기")
    @Test
    void findByCountryListedTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyEntityRepository.save(company);

        // then
        assertThat(companyEntityRepository.findByCountryListed(company.getCountryListed())).isEqualTo(List.of(company));
    }

    @DisplayName("규모로 기업 찾기")
    @Test
    void findByScaleTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyEntityRepository.save(company);

        // then
        assertThat(companyEntityRepository.findByScale(company.getScale())).isEqualTo(List.of(company));
    }

    @DisplayName("1차 업종으로 기업 찾기")
    @Test
    void findByFirstCategoryTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyEntityRepository.save(company);

        // then
        assertThat(companyEntityRepository.findByFirstCategory(company.getFirstCategory())).isEqualTo(List.of(company));
    }

    @DisplayName("2차 업종으로 기업 찾기")
    @Test
    void findBySecondCategoryTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyEntityRepository.save(company);

        // then
        assertThat(companyEntityRepository.findBySecondCategory(company.getSecondCategory())).isEqualTo(List.of(company));
    }

    @DisplayName("코드로 기업 찾기")
    @Test
    void findByCodeTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyEntityRepository.save(company);

        // then
        assertThat(companyEntityRepository.findByCode(company.getCode()).orElseThrow()).isEqualTo(company);
    }

    @DisplayName("한글명으로 기업 찾기")
    @Test
    void findByKoreanNameTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyEntityRepository.save(company);

        // then
        assertThat(companyEntityRepository.findByKoreanName(company.getKoreanName()).orElseThrow()).isEqualTo(company);
    }

    @DisplayName("영문명으로 기업 찾기")
    @Test
    void findByEnglishNameTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyEntityRepository.save(company);

        // then
        assertThat(companyEntityRepository.findByEnglishName(company.getEnglishName()).orElseThrow()).isEqualTo(company);
    }

    @DisplayName("상장된 이름으로 기업 찾기")
    @Test
    void findByNameListedTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyEntityRepository.save(company);

        // then
        assertThat(companyEntityRepository.findByNameListed(company.getNameListed()).orElseThrow()).isEqualTo(company);
    }

    @DisplayName("코드로 기업 삭제")
    @Test
    void deleteByCodeTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyEntityRepository.save(company);

        // when
        companyEntityRepository.deleteByCode(company.getCode());

        // then
        assertThat(companyEntityRepository.findAll()).isEmpty();
    }

    @DisplayName("코드로 기업 확인")
    @Test
    void existsByNumberTest() {
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);

        // when
        companyEntityRepository.save(company);

        // then
        assertThat(companyEntityRepository.existsByCode(company.getCode())).isEqualTo(true);
    }
}