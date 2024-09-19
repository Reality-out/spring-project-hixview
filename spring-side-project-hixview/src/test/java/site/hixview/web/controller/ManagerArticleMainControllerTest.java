package site.hixview.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.ArticleMain;
import site.hixview.domain.entity.article.ArticleMainDto;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.util.test.ArticleMainTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE;
import static site.hixview.domain.vo.name.EntityName.Article.NUMBER;
import static site.hixview.domain.vo.name.SchemaName.TEST_ARTICLE_MAINS_SCHEMA;
import static site.hixview.domain.vo.name.ViewName.*;

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
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAINS_SCHEMA, true);
    }

    @DisplayName("기사 메인 추가 페이지 접속")
    @Test
    public void accessArticleMainAdd() throws Exception {
        mockMvc.perform(get(ADD_ARTICLE_MAIN_URL))
                .andExpectAll(status().isOk(),
                        view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("기사 메인 추가 완료 페이지 접속")
    @Test
    public void accessArticleMainAddFinish() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestArticleMainDto();
        String redirectedURL = fromPath(ADD_ARTICLE_MAIN_URL + FINISH_URL).queryParam(NAME, articleDto.getName())
                .build().toUriString();

        // then
        mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_ARTICLE_MAIN_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
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
                        view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT));
    }

    @DisplayName("기사 메인 변경 페이지 검색")
    @Test
    public void searchArticleMainModify() throws Exception {
        // given
        ArticleMain article = testCompanyArticleMain;

        // when
        Long number = articleMainService.registerArticle(article).getNumber();

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                        UPDATE_ARTICLE_MAIN_URL, "numberOrName", String.valueOf(number)))
                .andExpectAll(status().isOk(),
                        view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute("updateUrl", modifyArticleMainFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());

        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                        UPDATE_ARTICLE_MAIN_URL, "numberOrName", article.getName()))
                .andExpectAll(status().isOk(),
                        view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute("updateUrl", modifyArticleMainFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());
    }

    @DisplayName("기사 메인 변경 완료 페이지 접속")
    @Test
    public void accessArticleMainModifyFinish() throws Exception {
        // given
        String commonName = testCompanyArticleMain.getName();
        ArticleMain article = ArticleMain.builder().article(testNewCompanyArticleMain).name(commonName).build();
        String redirectedURL = fromPath(UPDATE_ARTICLE_MAIN_URL + FINISH_URL).queryParam(NAME, article.getName()).build().toUriString();

        // when
        articleMainService.registerArticle(testCompanyArticleMain);

        // then
        mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, article.toDto()))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
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
        List<ArticleMain> articleList = articleMainService.registerArticles(testCompanyArticleMain, testNewCompanyArticleMain);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_ARTICLE_MAIN_URL))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "article-mains-page"))
                .andReturn().getModelAndView()).getModelMap().get("articleMains"))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("기사 메인 없애기 페이지 접속")
    @Test
    public void accessArticleMainRid() throws Exception {
        mockMvc.perform(get(REMOVE_ARTICLE_MAIN_URL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("기사 메인 없애기 완료 페이지 접속")
    @Test
    public void accessArticleMainRidFinish() throws Exception {
        // given
        ArticleMain article = testCompanyArticleMain;
        String name = article.getName();
        String redirectedURL = fromPath(REMOVE_ARTICLE_MAIN_URL + FINISH_URL).queryParam(NAME, name).build().toUriString();

        // when & then
        Long number = articleMainService.registerArticle(article).getNumber();

        mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, "numberOrName", String.valueOf(number)))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        articleMainService.registerArticle(article);

        mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, "numberOrName", name))
                .andExpectAll(status().isFound(), redirectedUrlPattern(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(VALUE, name));

        assertThat(articleMainService.findArticles()).isEmpty();
    }
}