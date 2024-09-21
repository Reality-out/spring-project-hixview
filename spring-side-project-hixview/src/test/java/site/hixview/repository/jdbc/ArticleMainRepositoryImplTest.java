package site.hixview.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.ArticleMain;
import site.hixview.domain.entity.article.ArticleMainDto;
import site.hixview.domain.repository.ArticleMainRepository;
import site.hixview.util.test.ArticleMainTestUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.domain.vo.name.EntityName.Article.NUMBER;
import static site.hixview.domain.vo.name.SchemaName.TEST_ARTICLE_MAINS_SCHEMA;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@Transactional
class ArticleMainRepositoryImplTest implements ArticleMainTestUtils {

    @Autowired
    ArticleMainRepository articleRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ArticleMainRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAINS_SCHEMA, true);
    }

    @DisplayName("기업 기사들 메인 획득")
    @Test
    public void getArticleMainsTest() {
        // given
        ArticleMain article1 = testCompanyArticleMain;
        ArticleMain article2 = testNewCompanyArticleMain;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("번호로 기사 메인 획득")
    @Test
    public void getArticleMainByNumberTest() {
        // given
        ArticleMain article = testCompanyArticleMain;

        // when
        article = ArticleMain.builder().article(article).number(articleRepository.saveArticle(article)).build();

        // then
        assertThat(articleRepository.getArticleByNumber(article.getNumber()).orElseThrow())
                .usingRecursiveComparison()
                .isEqualTo(article);
    }

    @DisplayName("이름으로 기사 메인 획득")
    @Test
    public void getArticleMainByNameTest() {
        // given
        ArticleMain article = testCompanyArticleMain;

        // when
        articleRepository.saveArticle(article);

        // then
        assertThat(articleRepository.getArticleByName(article.getName()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("이미지 경로로 기사 메인 획득")
    @Test
    public void getArticleMainByImagePathTest() {
        // given
        ArticleMain article = testCompanyArticleMain;

        // when
        articleRepository.saveArticle(article);

        // then
        assertThat(articleRepository.getArticleByImagePath(article.getImagePath()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("비어 있는 기사 메인 획득")
    @Test
    public void getEmptyArticleMainTest() {
        // given & when
        ArticleMain article = ArticleMain.builder().article(testCompanyArticleMain).number(1L).build();

        // then
        for (Optional<ArticleMain> emptyArticle : List.of(
                articleRepository.getArticleByNumber(article.getNumber()),
                articleRepository.getArticleByName(article.getName()),
                articleRepository.getArticleByImagePath(article.getImagePath()))) {
            assertThat(emptyArticle).isEmpty();
        }
    }

    @DisplayName("기사 메인 저장")
    @Test
    public void saveArticleMainTest() {
        // given
        ArticleMain article1 = testCompanyArticleMain;
        ArticleMain article2 = testNewCompanyArticleMain;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("기사 메인 갱신")
    @Test
    public void updateArticleMainTest() {
        // given
        ArticleMainDto article1 = createTestCompanyArticleMainDto();
        String commonName = article1.getName();
        ArticleMainDto article2 = createTestNewCompanyArticleMainDto();
        article2.setName(commonName);

        // when
        articleRepository.saveArticle(ArticleMain.builder().articleDto(article1).build());
        articleRepository.updateArticle(ArticleMain.builder().articleDto(article2).build());

        // then
        assertThat(articleRepository.getArticleByName(commonName).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(ArticleMain.builder().articleDto(article2).build());
    }

    @DisplayName("기사 메인 삭제")
    @Test
    public void deleteArticleMainTest() {
        // given
        ArticleMain article = testCompanyArticleMain;
        articleRepository.saveArticle(article);

        // when
        articleRepository.deleteArticleByName(article.getName());

        // then
        assertThat(articleRepository.getArticles()).isEmpty();
    }
}