package springsideproject1.springsideproject1build.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.company.CompanyArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.company.CompanyArticleMainDto;
import springsideproject1.springsideproject1build.domain.repository.CompanyArticleMainRepository;
import springsideproject1.springsideproject1build.util.test.CompanyArticleMainTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NUMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_COMPANY_ARTICLE_MAIN_TABLE;

@SpringBootTest
@Transactional
class CompanyArticleMainRepositoryImplTest implements CompanyArticleMainTestUtils {

    @Autowired
    CompanyArticleMainRepository articleRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleMainRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLE_MAIN_TABLE, true);
    }

    @DisplayName("기업 기사들 메인 획득")
    @Test
    public void getCompanyArticleMainsTest() {
        // given
        CompanyArticleMain article1 = testCompanyArticleMain;
        CompanyArticleMain article2 = testNewCompanyArticleMain;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("번호로 기업 기사 메인 획득")
    @Test
    public void getCompanyArticleMainByNumberTest() {
        // given
        CompanyArticleMain article = testCompanyArticleMain;

        // when
        article = CompanyArticleMain.builder().article(article).number(articleRepository.saveArticle(article)).build();

        // then
        assertThat(articleRepository.getArticleByNumber(article.getNumber()).orElseThrow())
                .usingRecursiveComparison()
                .isEqualTo(article);
    }

    @DisplayName("이름으로 기업 기사 메인 획득")
    @Test
    public void getCompanyArticleMainByNameTest() {
        // given
        CompanyArticleMain article = testCompanyArticleMain;

        // when
        articleRepository.saveArticle(article);

        // then
        assertThat(articleRepository.getArticleByName(article.getName()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("이미지 경로로 기업 기사 메인 획득")
    @Test
    public void getCompanyArticleMainByImagePathTest() {
        // given
        CompanyArticleMain article = testCompanyArticleMain;

        // when
        articleRepository.saveArticle(article);

        // then
        assertThat(articleRepository.getArticleByImagePath(article.getImagePath()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("기업 기사 메인 저장")
    @Test
    public void saveCompanyArticleMainTest() {
        // given
        CompanyArticleMain article1 = testCompanyArticleMain;
        CompanyArticleMain article2 = testNewCompanyArticleMain;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("기업 기사 메인 갱신")
    @Test
    public void updateCompanyArticleMainTest() {
        // given
        CompanyArticleMainDto article1 = createTestCompanyArticleMainDto();
        String commonName = article1.getName();
        CompanyArticleMainDto article2 = createTestNewCompanyArticleMainDto();
        article2.setName(commonName);

        // when
        articleRepository.saveArticle(CompanyArticleMain.builder().articleDto(article1).build());
        articleRepository.updateArticle(CompanyArticleMain.builder().articleDto(article2).build());

        // then
        assertThat(articleRepository.getArticleByName(commonName).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(CompanyArticleMain.builder().articleDto(article2).build());
    }

    @DisplayName("기업 기사 메인 삭제")
    @Test
    public void deleteCompanyArticleMainTest() {
        // given
        CompanyArticleMain article = testCompanyArticleMain;
        articleRepository.saveArticle(article);

        // when
        articleRepository.deleteArticleByName(article.getName());

        // then
        assertThat(articleRepository.getArticles()).isEmpty();
    }
}