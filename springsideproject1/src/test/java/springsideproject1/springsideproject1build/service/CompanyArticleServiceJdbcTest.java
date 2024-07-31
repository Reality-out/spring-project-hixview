package springsideproject1.springsideproject1build.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.utility.test.CompanyArticleTest;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.ALREADY_EXIST_ARTICLE_NAME;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_ARTICLE_WITH_THAT_NAME;

@SpringBootTest
@Transactional
class CompanyArticleServiceJdbcTest implements CompanyArticleTest {

    @Autowired
    CompanyArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, companyArticleTable, true);
    }

    @DisplayName("단일 기사 등록")
    @Test
    public void registerArticle() {
        // given
        CompanyArticle article = createTestArticle();

        // when
        article = articleService.joinArticle(article);

        // then
        assertThat(articleService.findArticles().getFirst())
                .usingRecursiveComparison()
                .isEqualTo(article);
    }

    @DisplayName("단일 문자열을 통한 기사 동시 등록")
    @Test
    public void registerArticlesWithString() {
        // given
        List<String> articleString = createTestStringArticle();

        // when
        articleService.joinArticlesWithString(articleString.getFirst(), articleString.get(1), articleString.getLast());

        // then
        assertThat(articleService.findArticles())
                .usingRecursiveComparison()
                .ignoringFields("number")
                .isEqualTo(List.of(createTestEqualDateArticle(), createTestNewArticle()));
    }

    @DisplayName("중복 기사 제목을 사용하는 단일 기사 등록")
    @Test
    public void registerArticleWithSameName() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestArticle();

        // when
        articleService.joinArticle(article1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.joinArticle(article2));
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_ARTICLE_NAME);
    }

    @DisplayName("중복 기사 제목과 단일 문자열을 사용하는 기사 동시 등록")
    @Test
    public void registerArticlesWithSameNameAndString() {
        // given
        CompanyArticle article = createTestNewArticle();
        List<String> articleString = createTestStringArticle();

        // when
        articleService.joinArticle(article);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.joinArticlesWithString(
                        articleString.getFirst(), articleString.get(1), articleString.getLast()));
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_ARTICLE_NAME);
    }

    @DisplayName("존재하지 않는 이름을 포함하는 단일 기사 갱신")
    @Test
    public void updateArticleWithFaultName() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.updateArticle(createTestArticle()));
        assertThat(e.getMessage()).isEqualTo(NO_ARTICLE_WITH_THAT_NAME);
    }

    @DisplayName("존재하지 않는 이름을 통한 단일 기사 삭제")
    @Test
    public void removeArticleByFaultName() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.removeArticle("123456"));
        assertThat(e.getMessage()).isEqualTo(NO_ARTICLE_WITH_THAT_NAME);
    }
}