package site.hixview.web.controller.nomock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.service.*;
import site.hixview.support.property.TestSchemaName;
import site.hixview.support.util.ArticleTestUtils;
import site.hixview.support.util.BlogPostTestUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.ViewName.USER_HOME_VIEW;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@TestSchemaName
@Transactional
class UserMainControllerTest implements ArticleTestUtils, BlogPostTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplateTest;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private IndustryArticleService industryArticleService;

    @Autowired
    private EconomyArticleService economyArticleService;

    @Autowired
    private ArticleMainService articleMainService;

    @Autowired
    private BlogPostService blogPostService;

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_ECONOMY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAINS_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_BLOG_POSTS_SCHEMA, true);
    }

    @DisplayName("유저 메인 페이지 접속")
    @Test
    void accessUserMainPage() throws Exception {
        // given & when
        companyArticleService.registerArticle(testCompanyArticle);
        articleMainService.registerArticle(testCompanyArticleMain);
        industryArticleService.registerArticle(testIndustryArticle);
        articleMainService.registerArticle(testIndustryArticleMain);
        economyArticleService.registerArticle(testEconomyArticle);
        economyArticleService.registerArticle(testEqualDateEconomyArticle);
        articleMainService.registerArticle(testDomesticEconomyArticleMain);
        articleMainService.registerArticle(testForeignEconomyArticleMain);
        blogPostService.registerPost(testBlogPostCompany);
        blogPostService.registerPost(testBlogPostIndustry);
        blogPostService.registerPost(testBlogPostEconomy);

        // then
        mockMvc.perform(getWithNoParam(""))
                .andExpectAll(status().isOk(),
                        view().name(USER_HOME_VIEW),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT));
    }
}
