package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.CompanyArticleEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.aggregate.vo.WordCamel.PRESS;
import static site.hixview.aggregate.vo.WordSnake.COMPANY_ARTICLE_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class CompanyArticleEntityRepositoryEntityTest implements CompanyArticleEntityTestUtils {

    @Autowired
    private CompanyArticleEntityRepository companyArticleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + COMPANY_ARTICLE_SNAKE,
            TEST_TABLE_PREFIX + ARTICLE, TEST_TABLE_PREFIX + PRESS};

    private static final Logger log = LoggerFactory.getLogger(CompanyArticleEntityRepositoryEntityTest.class);

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("날짜로 기업 기사 찾기")
    @Test
    void findByDateTest() {
        // given
        CompanyArticleEntity article = createCompanyArticleEntity();

        // when
        companyArticleRepository.save(article);

        // then
        assertThat(companyArticleRepository.findByDate(article.getDate())).isEqualTo(List.of(article));
    }

    @DisplayName("날짜 범위로 기업 기사 찾기")
    @Test
    void findByDateBetweenTest() {
        // given
        CompanyArticleEntity articleFirst = createCompanyArticleEntity();
        CompanyArticleEntity articleLast = createAnotherCompanyArticleEntity();
        List<CompanyArticleEntity> articleList = List.of(articleFirst, articleLast);

        // when
        companyArticleRepository.saveAll(articleList);

        // then
        assertThat(companyArticleRepository.findByDateBetween(articleFirst.getDate(), articleLast.getDate())).isEqualTo(articleList);
    }

    @DisplayName("대상 국가로 기업 기사 찾기")
    @Test
    void findBySubjectCountryTest() {
        // given
        CompanyArticleEntity article = createCompanyArticleEntity();

        // when
        companyArticleRepository.save(article);

        // then
        assertThat(companyArticleRepository.findBySubjectCountry(article.getSubjectCountry())).isEqualTo(List.of(article));
    }

    @DisplayName("중요성으로 기업 기사 찾기")
    @Test
    void findByImportanceTest() {
        // given
        CompanyArticleEntity article = createCompanyArticleEntity();
        CompanyArticleEntity anotherCompanyArticle = createAnotherCompanyArticleEntity();
        List<CompanyArticleEntity> articleList = List.of(article, anotherCompanyArticle);

        // when
        companyArticleRepository.saveAll(articleList);

        // then
        assertThat(companyArticleRepository.findByImportance(article.getImportance())).isEqualTo(articleList);
    }

    @DisplayName("번호로 기업 기사 찾기")
    @Test
    void findByNumberTest() {
        // given
        CompanyArticleEntity article = createCompanyArticleEntity();

        // when
        companyArticleRepository.save(article);

        // then
        assertThat(companyArticleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("이름으로 기업 기사 찾기")
    @Test
    void findByNameTest() {
        // given
        CompanyArticleEntity article = createCompanyArticleEntity();

        // when
        companyArticleRepository.save(article);

        // then
        assertThat(companyArticleRepository.findByName(article.getName()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("링크로 기업 기사 찾기")
    @Test
    void findByLinkTest() {
        // given
        CompanyArticleEntity article = createCompanyArticleEntity();

        // when
        companyArticleRepository.save(article);

        // then
        assertThat(companyArticleRepository.findByLink(article.getLink()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("번호로 기업 기사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        CompanyArticleEntity article = companyArticleRepository.save(createCompanyArticleEntity());

        // when
        companyArticleRepository.deleteByNumber(article.getNumber());

        // then
        assertThat(companyArticleRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 기업 기사 확인")
    @Test
    void existsByNumberTest() {
        // given
        CompanyArticleEntity article = createCompanyArticleEntity();

        // when
        companyArticleRepository.save(article);

        // then
        assertThat(companyArticleRepository.existsByNumber(article.getNumber())).isEqualTo(true);
    }
}