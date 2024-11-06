package site.hixview.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.domain.entity.home.ArticleMain;
import site.hixview.domain.entity.home.dto.ArticleMainDto;
import site.hixview.domain.repository.ArticleMainRepository;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.util.ArticleMainTestUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.domain.vo.Word.NUMBER;

@OnlyRealRepositoryContext
class ArticleMainRepositoryImplTest implements ArticleMainTestUtils {

    @Autowired
    private ArticleMainRepository articleRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    ArticleMainRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAINS_SCHEMA, true);
    }

    @DisplayName("기업 기사 메인들 획득")
    @Test
    void getArticleMainsTest() {
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
    void getArticleMainByNumberTest() {
        // given
        ArticleMain article = testCompanyArticleMain;

        // when
        article = ArticleMain.builder().article(article).number(articleRepository.saveArticle(article)).build();

        // then
        assertThat(articleRepository.getArticleByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("이름으로 기사 메인 획득")
    @Test
    void getArticleMainByNameTest() {
        // given
        ArticleMain article = testCompanyArticleMain;

        // when
        article = ArticleMain.builder().article(article).number(articleRepository.saveArticle(article)).build();

        // then
        assertThat(articleRepository.getArticleByName(article.getName()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("이미지 경로로 기사 메인 획득")
    @Test
    void getArticleMainByImagePathTest() {
        // given
        ArticleMain article = testCompanyArticleMain;

        // when
        article = ArticleMain.builder().article(article).number(articleRepository.saveArticle(article)).build();

        // then
        assertThat(articleRepository.getArticleByImagePath(article.getImagePath()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("비어 있는 기사 메인 획득")
    @Test
    void getEmptyArticleMainTest() {
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
    void saveArticleMainTest() {
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
    void updateArticleMainTest() {
        // given
        ArticleMainDto article1 = createTestCompanyArticleMainDto();
        String commonName = article1.getName();
        ArticleMainDto article2 = createTestNewCompanyArticleMainDto();
        article2.setName(commonName);

        // when
        articleRepository.saveArticle(ArticleMain.builder().articleDto(article1).build());
        ArticleMain articleMainUpdate = ArticleMain.builder().articleDto(article2).number(1L).build();
        articleRepository.updateArticle(articleMainUpdate);

        // then
        assertThat(articleRepository.getArticleByName(commonName).orElseThrow()).isEqualTo(articleMainUpdate);
    }

    @DisplayName("기사 메인 삭제")
    @Test
    void deleteArticleMainTest() {
        // given
        ArticleMain article = testCompanyArticleMain;
        articleRepository.saveArticle(article);

        // when
        articleRepository.deleteArticleByName(article.getName());

        // then
        assertThat(articleRepository.getArticles()).isEmpty();
    }
}