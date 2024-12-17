package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.CompanyArticleCompanyEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class CompanyArticleCompanyEntityRepositoryTest implements CompanyArticleCompanyEntityTestUtils {

    private final CompanyArticleCompanyEntityRepository companyArticleMapperRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryRepository;
    private final SecondCategoryEntityRepository secondCategoryRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    CompanyArticleCompanyEntityRepositoryTest(CompanyArticleCompanyEntityRepository companyArticleMapperRepository, CompanyEntityRepository companyEntityRepository, FirstCategoryEntityRepository firstCategoryRepository, SecondCategoryEntityRepository secondCategoryRepository, JdbcTemplate jdbcTemplate) {
        this.companyArticleMapperRepository = companyArticleMapperRepository;
        this.companyEntityRepository = companyEntityRepository;
        this.firstCategoryRepository = firstCategoryRepository;
        this.secondCategoryRepository = secondCategoryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 기업 기사와 기업 간 매퍼 찾기")
    @Test
    void findByNumberTest() {
        // given
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyEntityRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyEntity();
        mapper.updateCompany(company);

        // when
        companyArticleMapperRepository.save(mapper);

        // then
        assertThat(companyArticleMapperRepository.findByNumber(mapper.getNumber()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("기업 기사로 기업 기사와 기업 간 매퍼 찾기")
    @Test
    void findByCompanyArticleTest() {
        // given
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyEntityRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyEntity();
        mapper.updateCompany(company);

        // when
        companyArticleMapperRepository.save(mapper);

        // then
        assertThat(companyArticleMapperRepository.findByCompanyArticle(mapper.getCompanyArticle())).isEqualTo(List.of(mapper));
    }

    @DisplayName("기업으로 기업 기사와 기업 간 매퍼 찾기")
    @Test
    void findByCompanyTest() {
        // given
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyEntityRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyEntity();
        mapper.updateCompany(company);

        // when
        companyArticleMapperRepository.save(mapper);

        // then
        assertThat(companyArticleMapperRepository.findByCompany(mapper.getCompany())).isEqualTo(List.of(mapper));
    }

    @DisplayName("기업 기사와 기업으로 기업 기사와 기업 간 매퍼 찾기")
    @Test
    void findByCompanyArticleAndCompanyTest() {
        // given
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyEntityRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyEntity();
        mapper.updateCompany(company);

        // when
        companyArticleMapperRepository.save(mapper);

        // then
        assertThat(companyArticleMapperRepository.findByCompanyArticleAndCompany(
                mapper.getCompanyArticle(), mapper.getCompany()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("번호로 기업 기사와 기업 간 매퍼 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyEntityRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyEntity();
        mapper.updateCompany(company);
        companyArticleMapperRepository.save(mapper);

        // when
        companyArticleMapperRepository.deleteByNumber(mapper.getNumber());

        // then
        assertThat(companyArticleMapperRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 기업 기사와 기업 간 매퍼 확인")
    @Test
    void existsByNumberTest() {
        // given
        CompanyEntity company = createCompanyEntity();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyEntityRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyEntity();
        mapper.updateCompany(company);

        // when
        companyArticleMapperRepository.save(mapper);

        // then
        assertThat(companyArticleMapperRepository.existsByNumber(mapper.getNumber())).isEqualTo(true);
    }
}