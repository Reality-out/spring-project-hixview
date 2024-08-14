package springsideproject1.springsideproject1build.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.article.CompanyArticle;
import springsideproject1.springsideproject1build.utility.test.CompanyArticleTestUtility;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.ALREADY_EXIST_ARTICLE_NAME;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_ARTICLE_WITH_THAT_NAME;

@SpringBootTest
@Transactional
class CompanyArticleServiceJdbcTest implements CompanyArticleTestUtility {

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

    @DisplayName("기사 번호와 기사명을 사용하는 단일한 기사 조회")
    @Test
    public void inquiryArticleWithNumberAndName() {
        // given
        CompanyArticle article = createTestArticle();

        // when
        article = articleService.joinArticle(article);

        // then
        assertThat(articleService.findArticleByNumberOrName(article.getNumber().toString()))
                .usingRecursiveComparison()
                .isEqualTo(articleService.findArticleByNumberOrName(article.getName()));
    }

    @DisplayName("다수의 기사를 통한 기사 동시 등록")
    @Test
    public void registerArticles() {
        assertThat(articleService.joinArticles(createTestArticle(), createTestNewArticle()))
                .usingRecursiveComparison()
                .ignoringFields("number")
                .isEqualTo(List.of(createTestArticle(), createTestNewArticle()));
    }

    @DisplayName("단일 문자열을 통한 기사 동시 등록")
    @Test
    public void registerArticlesWithString() {
        // given
        List<String> articleString = createTestStringArticle();

        // then
        assertThat(articleService.joinArticlesWithString(articleString.getFirst(), articleString.get(1), articleString.getLast()))
                .usingRecursiveComparison()
                .ignoringFields("number")
                .isEqualTo(List.of(createTestEqualDateArticle(), createTestNewArticle()));
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

    @DisplayName("중복 기사 제목을 사용하는 다수 기사 등록")
    @Test
    public void registerArticleWithSameName() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.joinArticles(createTestArticle(), createTestArticle()));
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
    public void renewArticleWithFaultName() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.renewArticle(createTestArticle()));
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