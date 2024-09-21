package site.hixview.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.util.test.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static site.hixview.domain.vo.name.EntityName.Article.NUMBER;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_INDUSTRY_ARTICLE_NAME;
import static site.hixview.domain.vo.ExceptionMessage.NO_INDUSTRY_ARTICLE_WITH_THAT_NAME;
import static site.hixview.domain.vo.name.SchemaName.TEST_INDUSTRY_ARTICLES_SCHEMA;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
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
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLES_SCHEMA, true);
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

    @DisplayName("산업 기사 제거")
    @Test
    public void removeIndustryArticleTest() {
        // given
        articleService.registerArticle(testIndustryArticle);

        // when
        articleService.removeArticleByName(testIndustryArticle.getName());

        // then
        assertThat(articleService.findArticles()).isEmpty();
    }

    @DisplayName("산업 기사 존재하지 않는 이름으로 제거")
    @Test
    public void removeIndustryArticleByFaultNameTest() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.removeArticleByName(INVALID_VALUE));
        assertThat(e.getMessage()).isEqualTo(NO_INDUSTRY_ARTICLE_WITH_THAT_NAME);
    }
}