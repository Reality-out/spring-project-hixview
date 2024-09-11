package springsideproject1.springsideproject1build.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.error.AlreadyExistException;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;
import springsideproject1.springsideproject1build.util.test.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NUMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_INDUSTRY_ARTICLE_TABLE;

@SpringBootTest
@Transactional
class IndustryArticleServiceJdbcTest implements IndustryArticleTestUtils {

    @Autowired
    IndustryArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public IndustryArticleServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLE_TABLE, true);
    }

    @DisplayName("산업 기사 번호와 이름으로 찾기")
    @Test
    public void findIndustryArticleWithNumberAndNameTest() {
        // given
        IndustryArticle article = testIndustryArticle;

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(articleService.findArticleByNumberOrName(article.getNumber().toString()))
                .usingRecursiveComparison()
                .isEqualTo(articleService.findArticleByNumberOrName(article.getName()));
    }

    @DisplayName("산업 기사들 동시 등록")
    @Test
    public void registerIndustryArticlesTest() {
        assertThat(articleService.registerArticles(testIndustryArticle, testNewIndustryArticle))
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(testIndustryArticle, testNewIndustryArticle));
    }

    @DisplayName("산업 기사 등록")
    @Test
    public void registerIndustryArticleTest() {
        // given
        IndustryArticle article = testIndustryArticle;

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(articleService.findArticles().getFirst())
                .usingRecursiveComparison()
                .isEqualTo(article);
    }

    @DisplayName("산업 기사 중복 이름으로 등록")
    @Test
    public void registerDuplicatedIndustryArticleWithSameNameTest() {
        AlreadyExistException e = assertThrows(AlreadyExistException.class,
                () -> articleService.registerArticles(testIndustryArticle,
                        IndustryArticle.builder().article(testNewIndustryArticle).name(testIndustryArticle.getName()).build()));
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_INDUSTRY_ARTICLE_NAME);
    }

    @DisplayName("산업 기사 존재하지 않는 이름으로 수정")
    @Test
    public void correctIndustryArticleWithFaultNameTest() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.correctArticle(testIndustryArticle));
        assertThat(e.getMessage()).isEqualTo(NO_INDUSTRY_ARTICLE_WITH_THAT_NAME);
    }

    @DisplayName("산업 기사 존재하지 않는 이름으로 제거")
    @Test
    public void removeIndustryArticleByFaultNameTest() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.removeArticleByName(INVALID_VALUE));
        assertThat(e.getMessage()).isEqualTo(NO_INDUSTRY_ARTICLE_WITH_THAT_NAME);
    }
}