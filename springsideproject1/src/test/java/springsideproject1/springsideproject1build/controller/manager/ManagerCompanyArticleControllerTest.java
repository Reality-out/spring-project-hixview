package springsideproject1.springsideproject1build.controller.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.service.CompanyArticleService;

import javax.sql.DataSource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.Utility.*;
import static springsideproject1.springsideproject1build.config.constant.FOLDER_PATH_CONFIG.FINISH_ADD_COMPANY_ARTICLE_PATH;
import static springsideproject1.springsideproject1build.config.constant.FOLDER_PATH_CONFIG.PROCESS_ADD_COMPANY_ARTICLE_PATH;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.ADD_COMPANY_ARTICLE_VIEW_NAME;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.MANAGER_REMOVE_VIEW_NAME;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerCompanyArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ManagerCompanyArticleControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, articleTable, true);
    }

    @DisplayName("단일 기사 등록 페이지 접속")
    @Test
    public void accessArticleRegisterPage() throws Exception {
        mockMvc.perform(get("/manager/article/add/single"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_COMPANY_ARTICLE_VIEW_NAME + "singleProcessPage"))
                .andExpect(model().attribute("layoutPath", PROCESS_ADD_COMPANY_ARTICLE_PATH));
    }

    @DisplayName("단일 기사 등록 완료 페이지 접속")
    @Test
    public void accessArticleRegisterFinishPage() throws Exception {
        // given
        CompanyArticle article = createTestArticle();

        // then
        mockMvc.perform(post("/manager/article/add/single")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", article.getName())
                        .param("press", article.getPress())
                        .param("subjectCompany", article.getSubjectCompany())
                        .param("link", article.getLink())
                        .param("year", String.valueOf(article.getDate().getYear()))
                        .param("month", String.valueOf(article.getDate().getMonthValue()))
                        .param("date", String.valueOf(article.getDate().getDayOfMonth()))
                        .param("importance", String.valueOf(article.getImportance())))
                .andExpect(status().isSeeOther())
                .andExpect(model().attribute("name", article.getName()));

        mockMvc.perform(get("/manager/article/add/single/finish")
                        .param("name", article.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_COMPANY_ARTICLE_VIEW_NAME + "singleFinishPage"))
                .andExpect(model().attribute("layoutPath", FINISH_ADD_COMPANY_ARTICLE_PATH))
                .andExpect(model().attribute("name", article.getName()));
    }

    @DisplayName("단일 문자열을 사용하는 기사 등록 페이지 접속")
    @Test
    public void StringArticleRegisterPage() throws Exception {
        mockMvc.perform(get("/manager/article/add/multiple/string"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_COMPANY_ARTICLE_VIEW_NAME + "multipleProcessStringPage"))
                .andExpect(model().attribute("layoutPath", PROCESS_ADD_COMPANY_ARTICLE_PATH));
    }

    @DisplayName("단일 문자열을 사용하는 기사 등록 완료 페이지 접속")
    @Test
    public void StringArticleRegisterFinishPage() throws Exception {
        // given
        List<String> articleString = createTestStringArticle();
        List<String> nameList = articleService.joinArticlesWithString(
                articleString.getFirst(), articleString.get(1), articleString.getLast());
        articleService.removeArticle(createTestEqualDateArticle().getName());
        articleService.removeArticle(createTestNewArticle().getName());

        // then
        mockMvc.perform(post("/manager/article/add/multiple/string")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("subjectCompany", articleString.getFirst())
                        .param("articleString", articleString.get(1))
                        .param("linkString", articleString.getLast()))
                .andExpect(status().isSeeOther());

        mockMvc.perform(get("/manager/article/add/multiple/string/finish")
                        .param("nameList", nameList.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_COMPANY_ARTICLE_VIEW_NAME + "multipleFinishStringPage"))
                .andExpect(model().attribute("layoutPath", FINISH_ADD_COMPANY_ARTICLE_PATH));
    }

    @DisplayName("단일 기사 삭제 페이지 접속")
    @Test
    public void accessArticleRemovePage() throws Exception {
        mockMvc.perform(get("/manager/article/remove")
                        .param("dataTypeKor", "기사")
                        .param("dataTypeEng", "article")
                        .param("key", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name(MANAGER_REMOVE_VIEW_NAME + "processPage"))
                .andExpect(model().attribute("dataTypeKor", "기사"))
                .andExpect(model().attribute("dataTypeEng", "article"))
                .andExpect(model().attribute("key", "name"));
    }

    @DisplayName("단일 기사 삭제 완료 페이지 접속")
    @Test
    public void accessArticleRemoveFinishPage() throws Exception {
        // given
        CompanyArticle article = createTestArticle();
        String name = article.getName();

        // when
        articleService.joinArticle(article);

        // then
        mockMvc.perform(post("/manager/article/remove")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", name))
                .andExpect(status().isSeeOther());

        mockMvc.perform(get("/manager/article/remove/finish")
                        .param("name", URLEncoder.encode(name, StandardCharsets.UTF_8)))
                .andExpect(status().isOk())
                .andExpect(view().name(MANAGER_REMOVE_VIEW_NAME + "finishPage"))
                .andExpect(model().attribute("dataTypeKor", "기사"))
                .andExpect(model().attribute("key", "제목"))
                .andExpect(model().attribute("value", name));
    }
}