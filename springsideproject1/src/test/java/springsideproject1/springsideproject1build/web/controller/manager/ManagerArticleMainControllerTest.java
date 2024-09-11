package springsideproject1.springsideproject1build.web.controller.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMainDto;
import springsideproject1.springsideproject1build.domain.service.ArticleMainService;
import springsideproject1.springsideproject1build.util.test.ArticleMainTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NUMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_ARTICLE_MAIN_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.VALUE;
import static springsideproject1.springsideproject1build.util.ControllerUtils.encodeWithUTF8;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerArticleMainControllerTest implements ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ArticleMainService articleMainService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ManagerArticleMainControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAIN_TABLE, true);
    }

    @DisplayName("기사 메인 추가 페이지 접속")
    @Test
    public void accessArticleMainAdd() throws Exception {
        mockMvc.perform(get(ADD_ARTICLE_MAIN_URL))
                .andExpectAll(status().isOk(),
                        view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("기사 메인 추가 완료 페이지 접속")
    @Test
    public void accessArticleMainAddFinish() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestArticleMainDto();

        // then
        mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(ADD_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(articleDto.getName())));

        mockMvc.perform(getWithSingleParam(ADD_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX, NAME,
                        encodeWithUTF8(articleDto.getName())))
                .andExpectAll(status().isOk(),
                        view().name(ADD_ARTICLE_MAIN_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_PATH),
                        model().attribute(VALUE, articleDto.getName()));

        assertThat(articleMainService.findArticleByName(articleDto.getName()).orElseThrow().toDto())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(articleDto);
    }

    @DisplayName("기사 메인 변경 페이지 접속")
    @Test
    public void accessArticleMainModify() throws Exception {
        mockMvc.perform(get(UPDATE_ARTICLE_MAIN_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH));
    }

    @DisplayName("기사 메인 변경 페이지 검색")
    @Test
    public void searchArticleMainModify() throws Exception {
        // given
        ArticleMain article = testArticleMain;

        // when
        Long number = articleMainService.registerArticle(article).getNumber();

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                        UPDATE_ARTICLE_MAIN_URL, "numberOrName", String.valueOf(number)))
                .andExpectAll(status().isOk(),
                        view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute("updateUrl", modifyArticleMainFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());

        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                        UPDATE_ARTICLE_MAIN_URL, "numberOrName", article.getName()))
                .andExpectAll(status().isOk(),
                        view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute("updateUrl", modifyArticleMainFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());
    }

    @DisplayName("기사 메인 변경 완료 페이지 접속")
    @Test
    public void accessArticleMainModifyFinish() throws Exception {
        // given
        String commonName = testArticleMain.getName();
        ArticleMain article = ArticleMain.builder().article(testNewArticleMain).name(commonName).build();

        // when
        articleMainService.registerArticle(testArticleMain);

        // then
        mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, article.toDto()))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(modifyArticleMainFinishUrl + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(commonName)));

        mockMvc.perform(getWithSingleParam(modifyArticleMainFinishUrl, NAME, encodeWithUTF8(commonName)))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_PATH),
                        model().attribute(VALUE, commonName));

        assertThat(articleMainService.findArticleByName(commonName).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("기사 메인들 조회 페이지 접속")
    @Test
    public void accessArticleMainsInquiry() throws Exception {
        // given & when
        List<ArticleMain> articleList = articleMainService.registerArticles(testArticleMain, testNewArticleMain);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_ARTICLE_MAIN_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_SELECT_VIEW + "article-mains-page"))
                .andReturn().getModelAndView()).getModelMap().get("articleMains"))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("기사 메인 없애기 페이지 접속")
    @Test
    public void accessArticleMainRid() throws Exception {
        mockMvc.perform(get(REMOVE_ARTICLE_MAIN_URL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_PATH));
    }

    @DisplayName("기사 번호로 기사 메인 없애기 완료 페이지 접속")
    @Test
    public void accessArticleMainRidFinish() throws Exception {
        // given
        ArticleMain article = testArticleMain;
        String name = article.getName();

        // when & then
        Long number = articleMainService.registerArticle(article).getNumber();

        mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, "numberOrName", String.valueOf(number)))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        articleMainService.registerArticle(article);

        mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, "numberOrName", name))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        mockMvc.perform(getWithSingleParam(REMOVE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX, NAME, encodeWithUTF8(name)))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_PATH),
                        model().attribute(VALUE, name));

        assertThat(articleMainService.findArticles()).isEmpty();
    }
}