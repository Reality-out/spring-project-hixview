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
import org.springframework.ui.ModelMap;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleBufferSimple;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleDto;
import springsideproject1.springsideproject1build.domain.service.IndustryArticleService;
import springsideproject1.springsideproject1build.util.ControllerUtils;
import springsideproject1.springsideproject1build.util.test.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERROR_SINGLE;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.IS_BEAN_VALIDATION_ERROR;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_INDUSTRY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.VALUE;
import static springsideproject1.springsideproject1build.util.ControllerUtils.encodeWithUTF8;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerIndustryArticleControllerTest implements IndustryArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IndustryArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ManagerIndustryArticleControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLE_TABLE, true);
    }

    @DisplayName("산업 기사 추가 페이지 접속")
    @Test
    public void accessIndustryArticleAdd() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_INDUSTRY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("산업 기사 추가 완료 페이지 접속")
    @Test
    public void accessIndustryArticleAddFinish() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();

        // then
        mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(ADD_SINGLE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(articleDto.getName())));

        mockMvc.perform(getWithSingleParam(ADD_SINGLE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX, NAME,
                        encodeWithUTF8(articleDto.getName())))
                .andExpectAll(status().isOk(),
                        view().name(ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_PATH),
                        model().attribute(VALUE, articleDto.getName()));

        assertThat(articleService.findArticleByName(articleDto.getName()).orElseThrow().toDto())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(articleDto);
    }

    @DisplayName("문자열을 사용하는 산업 기사들 추가 페이지 접속")
    @Test
    public void accessIndustryArticleAddWithString() throws Exception {
        mockMvc.perform(get(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL))
                .andExpectAll(status().isOk(),
                        view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH));
    }

    @DisplayName("문자열을 사용하는 산업 기사들 추가 완료 페이지 접속")
    @Test
    public void accessIndustryArticleAddWithStringFinish() throws Exception {
        // given
        IndustryArticle article1 = testEqualDateIndustryArticle;
        IndustryArticle article2 = testNewIndustryArticle;
        IndustryArticleBufferSimple articleBuffer = testIndustryArticleStringBuffer;

        List<String> nameList = Stream.of(article1, article2)
                .map(IndustryArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        String articleString = articleBuffer.getNameDatePressString();

        // then
        ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, articleString);
                    put(linkString, articleBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, articleBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, articleBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapPost.get(nameListString)).usingRecursiveComparison().isEqualTo(nameListForURL);
        assertThat(modelMapPost.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(String.valueOf(false));
        assertThat(modelMapPost.get(ERROR_SINGLE)).isEqualTo(null);

        ModelMap modelMapGet = requireNonNull(mockMvc.perform(getWithMultipleParam(
                        ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX,
                        new HashMap<>() {{
                            put(nameListString, nameListForURL);
                            put(IS_BEAN_VALIDATION_ERROR, String.valueOf(false));
                            put(ERROR_SINGLE, null);
                        }}))
                .andExpectAll(status().isOk(),
                        view().name(ADD_INDUSTRY_ARTICLE_VIEW + "multiple-finish-page"),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_PATH),
                        model().attribute(nameListString, ControllerUtils.decodeWithUTF8(nameList)))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapGet.get(nameListString)).usingRecursiveComparison().isEqualTo(ControllerUtils.decodeWithUTF8(nameList));
        assertThat(modelMapGet.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);
        assertThat(modelMapGet.get(ERROR_SINGLE)).isEqualTo(null);

        assertThat(articleService.findArticleByName(nameList.getFirst()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(articleService.findArticleByName(nameList.getLast()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
    }

    @DisplayName("산업 기사 변경 페이지 접속")
    @Test
    public void accessIndustryArticleModify() throws Exception {
        mockMvc.perform(get(UPDATE_INDUSTRY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH));
    }

    @DisplayName("산업 기사 변경 페이지 검색")
    @Test
    public void searchIndustryArticleModify() throws Exception {
        // given
        IndustryArticle article = testIndustryArticle;

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                        UPDATE_INDUSTRY_ARTICLE_URL, "numberOrName", String.valueOf(article.getNumber())))
                .andExpectAll(status().isOk(),
                        view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute("updateUrl", modifyIndustryArticleFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());

        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                        UPDATE_INDUSTRY_ARTICLE_URL, "numberOrName", article.getName()))
                .andExpectAll(status().isOk(),
                        view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute("updateUrl", modifyIndustryArticleFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());
    }

    @DisplayName("산업 기사 변경 완료 페이지 접속")
    @Test
    public void accessIndustryArticleModifyFinish() throws Exception {
        // given
        IndustryArticle beforeModifyArticle = testIndustryArticle;
        IndustryArticle article = IndustryArticle.builder().article(testNewIndustryArticle)
                .name(beforeModifyArticle.getName()).link(beforeModifyArticle.getLink()).build();

        // when
        articleService.registerArticle(beforeModifyArticle);
        String commonName = beforeModifyArticle.getName();
        IndustryArticleDto articleDto = article.toDto();

        // then
        mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(modifyIndustryArticleFinishUrl + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(commonName)));

        mockMvc.perform(getWithSingleParam(modifyIndustryArticleFinishUrl, NAME, encodeWithUTF8(commonName)))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_PATH),
                        model().attribute(VALUE, commonName));

        assertThat(articleService.findArticleByName(commonName).orElseThrow().toDto())
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("산업 기사들 조회 페이지 접속")
    @Test
    public void accessIndustryArticlesInquiry() throws Exception {
        // given & when
        List<IndustryArticle> articleList = articleService.registerArticles(testIndustryArticle, testNewIndustryArticle);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_INDUSTRY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_SELECT_VIEW + "industry-articles-page"))
                .andReturn().getModelAndView()).getModelMap().get("articles"))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("산업 기사 없애기 페이지 접속")
    @Test
    public void accessIndustryArticleRid() throws Exception {
        mockMvc.perform(get(REMOVE_INDUSTRY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_PATH));
    }

    @DisplayName("산업 기사 없애기 완료 페이지 접속")
    @Test
    public void accessIndustryArticleRidFinish() throws Exception {
        // given
        IndustryArticle article = testIndustryArticle;
        String name = article.getName();

        // when
        Long number = articleService.registerArticle(article).getNumber();

        // then
        mockMvc.perform(postWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL, "numberOrName", String.valueOf(number)))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        articleService.registerArticle(article);

        mockMvc.perform(postWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL, "numberOrName", String.valueOf(name)))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        mockMvc.perform(getWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX, NAME, encodeWithUTF8(name)))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_PATH),
                        model().attribute(VALUE, name));

        assertThat(articleService.findArticles()).isEmpty();
    }
}