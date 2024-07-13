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
import springsideproject1.springsideproject1build.domain.Article;
import springsideproject1.springsideproject1build.service.ArticleService;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.Utility.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ManagerArticleControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, memberTable, true);
    }

    @DisplayName("기사 등록 페이지 접속")
    @Test
    public void accessArticleRegisterPage() throws Exception {
        mockMvc.perform(get("/manager/article/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/add/article/processPage"));
    }

    @DisplayName("기사 등록 완료 페이지 접속")
    @Test
    public void accessArticleRegisterFinishPage() throws Exception {
        // given
        Article article = createTestArticle();

        // then
        mockMvc.perform(post("/manager/article/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", article.getName())
                        .param("date", article.getDate().toString())
                        .param("link", article.getLink()))
                .andExpect(status().isSeeOther());

        mockMvc.perform(get("/manager/article/add/finish"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/add/article/finishPage"));
    }

    @DisplayName("기사 삭제 페이지 접속")
    @Test
    public void accessArticleRemovePage() throws Exception {
        mockMvc.perform(get("/manager/article/remove"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/remove/article/processPage"));
    }

    @DisplayName("기사 삭제 완료 페이지 접속")
    @Test
    public void accessArticleRemoveFinishPage() throws Exception {
        // given
        Article article = createTestArticle();
        String name = article.getName();

        // when
        articleService.joinArticle(article);

        // then
        mockMvc.perform(post("/manager/article/remove")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", name))
                .andExpect(status().isSeeOther());

        mockMvc.perform(get("/manager/article/remove/finish")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/remove/article/finishPage"));
    }
}