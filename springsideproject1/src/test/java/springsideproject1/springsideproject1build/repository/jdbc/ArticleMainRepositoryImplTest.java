package springsideproject1.springsideproject1build.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMainDto;
import springsideproject1.springsideproject1build.domain.repository.ArticleMainRepository;
import springsideproject1.springsideproject1build.util.test.ArticleMainTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NUMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_ARTICLE_MAIN_TABLE;

@SpringBootTest
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
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAIN_TABLE, true);
    }

    @DisplayName("기업 기사들 메인 획득")
    @Test
    public void getArticleMainsTest() {
        // given
        ArticleMain article1 = testArticleMain;
        ArticleMain article2 = testNewArticleMain;

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
        ArticleMain article = testArticleMain;

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
        ArticleMain article = testArticleMain;

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
        ArticleMain article = testArticleMain;

        // when
        articleRepository.saveArticle(article);

        // then
        assertThat(articleRepository.getArticleByImagePath(article.getImagePath()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("기사 메인 저장")
    @Test
    public void saveArticleMainTest() {
        // given
        ArticleMain article1 = testArticleMain;
        ArticleMain article2 = testNewArticleMain;

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
        ArticleMainDto article1 = createTestArticleMainDto();
        String commonName = article1.getName();
        ArticleMainDto article2 = createTestNewArticleMainDto();
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
        ArticleMain article = testArticleMain;
        articleRepository.saveArticle(article);

        // when
        articleRepository.deleteArticleByName(article.getName());

        // then
        assertThat(articleRepository.getArticles()).isEmpty();
    }
}