package springsideproject1.springsideproject1build.controller.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.service.CompanyArticleService;
import springsideproject1.springsideproject1build.utility.test.CompanyArticleTest;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.ConstantUtility.*;
import static springsideproject1.springsideproject1build.utility.MainUtility.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerCompanyArticleControllerTest implements CompanyArticleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;
    private final String NAME = "name";

    @Autowired
    public ManagerCompanyArticleControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, companyArticleTable, true);
    }

    @DisplayName("단일 기사 등록 페이지 접속")
    @Test
    public void accessArticleRegisterPage() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + "single" + VIEW_PASCAL_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, PROCESS_ADD_COMPANY_ARTICLE_PATH));
    }

    @DisplayName("단일 기사 등록 완료 페이지 접속")
    @Test
    public void accessArticleRegisterFinishPage() throws Exception {
        // given
        CompanyArticle article = createTestArticle();

        // then
        mockMvc.perform(processPostWithCompanyArticle(ADD_SINGLE_COMPANY_ARTICLE_URL, article))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(article.getName())));

        mockMvc.perform(processGetWithSingleParam(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX,
                        NAME, encodeUTF8(article.getName())))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + "single" + VIEW_PASCAL_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, FINISH_ADD_COMPANY_ARTICLE_PATH),
                        model().attribute(NAME, article.getName()));
    }

    @DisplayName("단일 문자열을 사용하는 기사 등록 페이지 접속")
    @Test
    public void StringArticleRegisterPage() throws Exception {
        mockMvc.perform(get(ADD_COMPANY_ARTICLE_WITH_STRING_URL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + "multipleString" + VIEW_PASCAL_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, PROCESS_ADD_COMPANY_ARTICLE_PATH));
    }

    @DisplayName("단일 문자열을 사용하는 기사 등록 완료 페이지 접속")
    @Test
    public void StringArticleRegisterFinishPage() throws Exception {
        // given
        List<String> articleString = createTestStringArticle();
        List<String> nameList = articleService.joinArticlesWithString(
                articleString.getFirst(), articleString.get(1), articleString.getLast())
                .stream().map(CompanyArticle::getName).collect(Collectors.toList());
        articleService.removeArticle(createTestEqualDateArticle().getName());
        articleService.removeArticle(createTestNewArticle().getName());

        // then
        String nameListForURL = toStringForUrl(encodeUTF8(nameList));
        String nameListString = "nameList";

        assertThat(mockMvc.perform(processPostWithMultipleParam(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>(){{
                    put("subjectCompany", articleString.getFirst());
                    put("articleString", articleString.get(1));
                    put("linkString", articleString.getLast());
                }}))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
                .andReturn().getModelAndView().getModelMap().get(nameListString))
                .usingRecursiveComparison()
                .isEqualTo(nameListForURL);

        assertThat(mockMvc.perform(processGetWithSingleParam(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX,
                        nameListString, nameListForURL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + "multipleString" + VIEW_PASCAL_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, FINISH_ADD_COMPANY_ARTICLE_PATH))
                .andReturn().getModelAndView().getModelMap().get(nameListString))
                .usingRecursiveComparison()
                .isEqualTo(decodeUTF8(nameList));
    }

    @DisplayName("단일 기사 갱신 페이지 접속")
    @Test
    public void accessArticleUpdatePage() throws Exception {
        mockMvc.perform(get(UPDATE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + "before" + VIEW_PASCAL_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, PROCESS_UPDATE_COMPANY_ARTICLE_PATH));
    }

    @DisplayName("단일 기사 갱신 페이지 내 이름 검색")
    @Test
    public void searchNameInArticleUpdatePage() throws Exception {
        // given
        CompanyArticle article = createTestArticle();

        // when
        article = articleService.joinArticle(article);

        // then
        assertThat(mockMvc.perform(processPostWithSingleParam(UPDATE_COMPANY_ARTICLE_URL, NAME, article.getName()))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + "after" + VIEW_PASCAL_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, PROCESS_UPDATE_COMPANY_ARTICLE_PATH),
                        model().attribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX),
                        model().attribute(YEAR, article.getDate().getYear()),
                        model().attribute(MONTH, article.getDate().getMonthValue()),
                        model().attribute(DATE, article.getDate().getDayOfMonth()))
                .andReturn().getModelAndView().getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article);
    }

    @DisplayName("단일 기사 갱신 완료 페이지 접속")
    @Test
    public void accessArticleUpdateFinishPage() throws Exception {
        // given
        CompanyArticle article = createTestArticle();

        // when
        article = articleService.joinArticle(article);

        // then
        mockMvc.perform(processPostWithCompanyArticle(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX, article))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(article.getName())));

        mockMvc.perform(processGetWithSingleParam(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX,
                        NAME, encodeUTF8(article.getName())))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, FINISH_UPDATE_COMPANY_ARTICLE_PATH),
                        model().attribute(NAME, article.getName()));
    }

    @DisplayName("단일 기사 삭제 페이지 접속")
    @Test
    public void accessArticleRemovePage() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, "기사"),
                        model().attribute(DATA_TYPE_ENGLISH, ARTICLE),
                        model().attribute(KEY, NAME));
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
        mockMvc.perform(processPostWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, NAME, name))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(name)));

        mockMvc.perform(processGetWithSingleParam(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX, NAME, encodeUTF8(name)))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, "기사"),
                        model().attribute(KEY, "제목"),
                        model().attribute(VALUE, name));
    }
}