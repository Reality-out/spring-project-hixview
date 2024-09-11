package springsideproject1.springsideproject1build.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.error.AlreadyExistException;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;
import springsideproject1.springsideproject1build.util.test.ArticleMainTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.ALREADY_EXIST_ARTICLE_MAIN_NAME;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_ARTICLE_MAIN_WITH_THAT_NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NUMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_ARTICLE_MAIN_TABLE;

@SpringBootTest
@Transactional
class ArticleMainServiceJdbcTest implements ArticleMainTestUtils {

    @Autowired
    ArticleMainService articleMainService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ArticleMainServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAIN_TABLE, true);
    }

    @DisplayName("기사 메인들 동시 등록")
    @Test
    public void registerArticleMainsTest() {
        assertThat(articleMainService.registerArticles(testCompanyArticleMain, testNewCompanyArticleMain))
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(testCompanyArticleMain, testNewCompanyArticleMain));
    }

    @DisplayName("기사 메인 등록")
    @Test
    public void registerArticleMainTest() {
        // given
        ArticleMain article = testCompanyArticleMain;

        // when
        article = articleMainService.registerArticle(article);

        // then
        assertThat(articleMainService.findArticles().getFirst())
                .usingRecursiveComparison()
                .isEqualTo(article);
    }

    @DisplayName("기사 메인 중복 이름으로 등록")
    @Test
    public void registerDuplicatedArticleMainWithSameNameTest() {
        AlreadyExistException e = assertThrows(AlreadyExistException.class,
                () -> articleMainService.registerArticles(testCompanyArticleMain,
                        ArticleMain.builder().article(testNewCompanyArticleMain).name(testCompanyArticleMain.getName()).build()));
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_ARTICLE_MAIN_NAME);
    }

    @DisplayName("기사 메인 존재하지 않는 이름으로 수정")
    @Test
    public void correctArticleMainWithFaultNameTest() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleMainService.correctArticle(testCompanyArticleMain));
        assertThat(e.getMessage()).isEqualTo(NO_ARTICLE_MAIN_WITH_THAT_NAME);
    }

    @DisplayName("기사 메인 존재하지 않는 이름으로 제거")
    @Test
    public void removeArticleMainByFaultNameTest() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleMainService.removeArticleByName(INVALID_VALUE));
        assertThat(e.getMessage()).isEqualTo(NO_ARTICLE_MAIN_WITH_THAT_NAME);
    }
}