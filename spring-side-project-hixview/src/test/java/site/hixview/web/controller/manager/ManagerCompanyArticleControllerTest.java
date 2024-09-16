package site.hixview.web.controller.manager;

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
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.CompanyArticleBufferSimple;
import site.hixview.domain.entity.article.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.ControllerUtils;
import site.hixview.util.test.CompanyArticleTestUtils;
import site.hixview.util.test.CompanyTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANY_ARTICLES_SCHEMA;
import static site.hixview.domain.vo.name.ViewName.*;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerCompanyArticleControllerTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ManagerCompanyArticleControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("기업 기사 추가 페이지 접속")
    @Test
    public void accessCompanyArticleAdd() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("기업 기사 추가 완료 페이지 접속")
    @Test
    public void accessCompanyArticleAddFinish() throws Exception {
        // given
        CompanyArticleDto articleDto = createTestCompanyArticleDto();

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(ADD_SINGLE_COMPANY_ARTICLE_URL + FINISH_URL + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(articleDto.getName())));

        mockMvc.perform(getWithSingleParam(ADD_SINGLE_COMPANY_ARTICLE_URL + FINISH_URL, NAME,
                        encodeWithUTF8(articleDto.getName())))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + SINGLE_FINISH_VIEW),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute(VALUE, articleDto.getName()));

        assertThat(articleService.findArticleByName(articleDto.getName()).orElseThrow().toDto())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(articleDto);
    }

    @DisplayName("문자열을 사용하는 기업 기사들 추가 페이지 접속")
    @Test
    public void accessCompanyArticleAddWithString() throws Exception {
        mockMvc.perform(get(ADD_COMPANY_ARTICLE_WITH_STRING_URL))
                .andExpectAll(status().isOk(),
                        view().name(addStringCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT));
    }

    @DisplayName("문자열을 사용하는 기업 기사들 추가 완료 페이지 접속")
    @Test
    public void accessCompanyArticleAddWithStringFinish() throws Exception {
        // given
        CompanyArticle article1 = testEqualDateCompanyArticle;
        CompanyArticle article2 = testNewCompanyArticle;
        CompanyArticleBufferSimple articleBuffer = testCompanyArticleStringBuffer;

        List<String> nameList = Stream.of(article1, article2)
                .map(CompanyArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        String articleString = articleBuffer.getNameDatePressString();

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, articleString);
                    put(SUBJECT_COMPANY, articleBuffer.getSubjectCompany());
                    put(linkString, articleBuffer.getLinkString());
                }}))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL + ALL_QUERY_STRING))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapPost.get(nameListString)).usingRecursiveComparison().isEqualTo(nameListForURL);
        assertThat(modelMapPost.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(String.valueOf(false));
        assertThat(modelMapPost.get(ERROR_SINGLE)).isEqualTo(null);

        ModelMap modelMapGet = requireNonNull(mockMvc.perform(getWithMultipleParam(
                        ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL,
                        new HashMap<>() {{
                            put(nameListString, nameListForURL);
                            put(IS_BEAN_VALIDATION_ERROR, String.valueOf(false));
                            put(ERROR_SINGLE, null);
                        }}))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + "multiple-finish-page"),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
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

    @DisplayName("기업 기사 변경 페이지 접속")
    @Test
    public void accessCompanyArticleModify() throws Exception {
        mockMvc.perform(get(UPDATE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + BEFORE_PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT));
    }

    @DisplayName("기업 기사 변경 페이지 검색")
    @Test
    public void searchCompanyArticleModify() throws Exception {
        // given
        CompanyArticle article = testCompanyArticle;

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                        UPDATE_COMPANY_ARTICLE_URL, "numberOrName", String.valueOf(article.getNumber())))
                .andExpectAll(status().isOk(),
                        view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute("updateUrl", modifyCompanyArticleFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());

        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                        UPDATE_COMPANY_ARTICLE_URL, "numberOrName", article.getName()))
                .andExpectAll(status().isOk(),
                        view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute("updateUrl", modifyCompanyArticleFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());
    }

    @DisplayName("기업 기사 변경 완료 페이지 접속")
    @Test
    public void accessCompanyArticleModifyFinish() throws Exception {
        // given
        CompanyArticle beforeModifyArticle = testCompanyArticle;
        CompanyArticle article = CompanyArticle.builder().article(testNewCompanyArticle)
                .name(beforeModifyArticle.getName()).link(beforeModifyArticle.getLink()).build();

        // when
        companyService.registerCompany(samsungElectronics);
        articleService.registerArticle(beforeModifyArticle);
        String commonName = beforeModifyArticle.getName();
        CompanyArticleDto articleDto = article.toDto();

        // then
        mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(modifyCompanyArticleFinishUrl + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(commonName)));

        mockMvc.perform(getWithSingleParam(modifyCompanyArticleFinishUrl, NAME, encodeWithUTF8(commonName)))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + FINISH_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
                        model().attribute(VALUE, commonName));

        assertThat(articleService.findArticleByName(commonName).orElseThrow().toDto())
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기업 기사들 조회 페이지 접속")
    @Test
    public void accessCompanyArticlesInquiry() throws Exception {
        // given & when
        List<CompanyArticle> articleList = articleService.registerArticles(testCompanyArticle, testNewCompanyArticle);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "company-articles-page"))
                .andReturn().getModelAndView()).getModelMap().get("articles"))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("기업 기사 없애기 페이지 접속")
    @Test
    public void accessCompanyArticleRid() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_ARTICLE_VIEW + PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("기업 기사 없애기 완료 페이지 접속")
    @Test
    public void accessCompanyArticleRidFinish() throws Exception {
        // given
        CompanyArticle article = testCompanyArticle;
        String name = article.getName();

        // when
        Long number = articleService.registerArticle(article).getNumber();

        // then
        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, "numberOrName", String.valueOf(number)))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_COMPANY_ARTICLE_URL + FINISH_URL + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        articleService.registerArticle(article);

        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, "numberOrName", String.valueOf(name)))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_COMPANY_ARTICLE_URL + FINISH_URL + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        mockMvc.perform(getWithSingleParam(REMOVE_COMPANY_ARTICLE_URL + FINISH_URL, NAME, encodeWithUTF8(name)))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_ARTICLE_VIEW + FINISH_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(VALUE, name));

        assertThat(articleService.findArticles()).isEmpty();
    }
}