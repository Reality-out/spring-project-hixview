package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.executor.SqlExecutor;
import site.hixview.support.jpa.util.CompanyArticleCompanyMapperTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class CompanyArticleCompanyRepositoryTest implements CompanyArticleCompanyMapperTestUtils {

    @Autowired
    private CompanyArticleCompanyRepository companyArticleMapperRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FirstCategoryRepository firstCategoryRepository;

    @Autowired
    private SecondCategoryRepository secondCategoryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(CompanyArticleCompanyRepositoryTest.class);

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 기업 기사와 기업 간 매퍼 찾기")
    @Test
    void findByNumberTest() {
        // given
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyMapper();
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
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyMapper();
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
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyMapper();
        mapper.updateCompany(company);

        // when
        companyArticleMapperRepository.save(mapper);

        // then
        assertThat(companyArticleMapperRepository.findByCompany(mapper.getCompany())).isEqualTo(List.of(mapper));
    }

    @DisplayName("번호로 기업 기사와 기업 간 매퍼 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyMapper();
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
        CompanyEntity company = createCompany();
        FirstCategoryEntity firstCategory = company.getFirstCategory();
        SecondCategoryEntity secondCategory = company.getSecondCategory();
        firstCategoryRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryRepository.save(secondCategory);
        companyRepository.save(company);
        CompanyArticleCompanyEntity mapper = createCompanyArticleCompanyMapper();
        mapper.updateCompany(company);

        // when
        companyArticleMapperRepository.save(mapper);

        // then
        assertThat(companyArticleMapperRepository.existsByNumber(mapper.getNumber())).isEqualTo(true);
    }
}