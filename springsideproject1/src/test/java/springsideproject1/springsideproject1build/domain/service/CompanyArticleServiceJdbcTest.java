package springsideproject1.springsideproject1build.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.error.AlreadyExistException;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.ALREADY_EXIST_ARTICLE_NAME;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_ARTICLE_WITH_THAT_NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NUMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_ARTICLE_TABLE;

@SpringBootTest
@Transactional
class CompanyArticleServiceJdbcTest implements CompanyArticleTestUtils {

    @Autowired
    CompanyArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, COMPANY_ARTICLE_TABLE, true);
    }

    @DisplayName("기업 기사 번호와 이름으로 찾기")
    @Test
    public void findCompanyArticleWithNumberAndNameTest() {
        // given
        CompanyArticle article = testArticle;

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(articleService.findArticleByNumberOrName(article.getNumber().toString()))
                .usingRecursiveComparison()
                .isEqualTo(articleService.findArticleByNumberOrName(article.getName()));
    }

    @DisplayName("기업 기사들 동시 등록")
    @Test
    public void registerCompanyArticlesTest() {
        assertThat(articleService.registerArticles(testArticle, testNewArticle))
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(testArticle, testNewArticle));
    }

    @DisplayName("기업 기사 등록")
    @Test
    public void registerCompanyArticleTest() {
        // given
        CompanyArticle article = testArticle;

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(articleService.findArticles().getFirst())
                .usingRecursiveComparison()
                .isEqualTo(article);
    }

    @DisplayName("기업 기사 중복 이름으로 등록")
    @Test
    public void registerDuplicatedCompanyArticleWithSameNameTest() {
        AlreadyExistException e = assertThrows(AlreadyExistException.class,
                () -> articleService.registerArticles(testArticle,
                        CompanyArticle.builder().article(testNewArticle).name(testArticle.getName()).build()));
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_ARTICLE_NAME);
    }

    @DisplayName("기업 기사 존재하지 않는 이름으로 수정")
    @Test
    public void correctCompanyArticleWithFaultNameTest() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.correctArticle(testArticle));
        assertThat(e.getMessage()).isEqualTo(NO_ARTICLE_WITH_THAT_NAME);
    }

    @DisplayName("기업 기사 존재하지 않는 이름으로 제거")
    @Test
    public void removeCompanyArticleByFaultNameTest() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.removeArticleByName("123456"));
        assertThat(e.getMessage()).isEqualTo(NO_ARTICLE_WITH_THAT_NAME);
    }
}