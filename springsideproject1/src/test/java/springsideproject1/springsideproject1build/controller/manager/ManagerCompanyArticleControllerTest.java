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
import org.springframework.ui.ModelMap;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleBufferSimple;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.entity.article.Press;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.util.MainUtils;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;
import springsideproject1.springsideproject1build.util.test.CompanyTestUtils;

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
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.VALUE;
import static springsideproject1.springsideproject1build.util.MainUtils.encodeWithUTF8;

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
        resetTable(jdbcTemplateTest, COMPANY_ARTICLE_TABLE, true);
        resetTable(jdbcTemplateTest, COMPANY_TABLE);
    }

    @DisplayName("기업 기사 추가 페이지 접속")
    @Test
    public void accessCompanyArticleAdd() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(addSingleArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("기업 기사 추가 완료 페이지 접속")
    @Test
    public void accessCompanyArticleAddFinish() throws Exception {
        // given
        CompanyArticleDto articleDtoOriginal = createTestCompanyArticleDto();
        CompanyArticleDto articleDtoLeftSpace = createTestCompanyArticleDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        CompanyArticleDto articleDtoRightSpace = createTestCompanyArticleDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        CompanyArticleDto articleDtoKoreanPress = createTestCompanyArticleDto();
        articleDtoKoreanPress.setPress(Press.valueOf(articleDtoKoreanPress.getPress()).getPressValue());
        CompanyArticleDto articleDtoLowercasePress = createTestCompanyArticleDto();
        articleDtoLowercasePress.setPress(Press.valueOf(articleDtoLowercasePress.getPress()).name().toLowerCase());

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoOriginal, articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKoreanPress, articleDtoLowercasePress)) {
            mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(articleDtoOriginal.getName())));
            articleService.removeArticleByName(articleDtoOriginal.getName());
        }

        articleService.registerArticle(CompanyArticle.builder().articleDto(articleDtoOriginal).build());

        mockMvc.perform(getWithSingleParam(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX, NAME,
                        encodeWithUTF8(articleDtoOriginal.getName())))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_PATH),
                        model().attribute(VALUE, articleDtoOriginal.getName()));

        assertThat(articleService.findArticleByName(articleDtoOriginal.getName()).orElseThrow().toDto())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(articleDtoOriginal);
    }

    @DisplayName("문자열을 사용하는 기업 기사들 추가 페이지 접속")
    @Test
    public void accessCompanyArticleAddWithString() throws Exception {
        mockMvc.perform(get(ADD_COMPANY_ARTICLE_WITH_STRING_URL))
                .andExpectAll(status().isOk(),
                        view().name(addStringArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH));
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

        String articleStringOriginal = articleBuffer.getNameDatePressString();
        String articleStringLeftSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), " " + article1.getName());
        String articleStringRightSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), article1.getName() + " ");
        String articleStringKoreanPress = testCompanyArticleStringBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().getPressValue())
                .replace(article2.getPress().name(), article2.getPress().getPressValue());
        String articleStringLowercasePress = testCompanyArticleStringBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().name().toLowerCase())
                .replace(article2.getPress().name(), article2.getPress().name().toLowerCase());

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        for (String articleString : List.of(articleStringOriginal, articleStringLeftSpace, articleStringRightSpace,
                articleStringKoreanPress, articleStringLowercasePress)) {
            ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                        put(nameDatePressString, articleString);
                        put(SUBJECT_COMPANY, articleBuffer.getSubjectCompany());
                        put(linkString, articleBuffer.getLinkString());
                    }}))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
                    .andReturn().getModelAndView()).getModelMap();

            assertThat(modelMapPost.get(nameListString)).usingRecursiveComparison().isEqualTo(nameListForURL);
            assertThat(modelMapPost.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(String.valueOf(false));
            assertThat(modelMapPost.get(ERROR_SINGLE)).isEqualTo(null);

            articleService.removeArticleByName(article1.getName());
            articleService.removeArticleByName(article2.getName());
        }

        articleService.registerArticles(article1, article2);

        ModelMap modelMapGet = requireNonNull(mockMvc.perform(getWithMultipleParam(
                        ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX,
                        new HashMap<>() {{
                            put(nameListString, nameListForURL);
                            put(IS_BEAN_VALIDATION_ERROR, String.valueOf(false));
                            put(ERROR_SINGLE, null);
                        }}))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + "multiple-finish-page"),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_PATH),
                        model().attribute(nameListString, MainUtils.decodeWithUTF8(nameList)))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapGet.get(nameListString)).usingRecursiveComparison().isEqualTo(MainUtils.decodeWithUTF8(nameList));
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
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH));
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
                        view().name(modifyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute("updateUrl", modifyArticleFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());

        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                        UPDATE_COMPANY_ARTICLE_URL, "numberOrName", article.getName()))
                .andExpectAll(status().isOk(),
                        view().name(modifyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute("updateUrl", modifyArticleFinishUrl))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());
    }

    @DisplayName("기업 기사 변경 완료 페이지 접속")
    @Test
    public void accessCompanyArticleModifyFinish() throws Exception {
        // given
        CompanyArticle beforeModifyArticle = testCompanyArticle;
        String commonName = beforeModifyArticle.getName();
        CompanyArticle article = CompanyArticle.builder().article(testNewCompanyArticle)
                .name(commonName).link(beforeModifyArticle.getLink()).build();
        CompanyArticleDto articleDtoOriginal = article.toDto();
        CompanyArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        CompanyArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        CompanyArticleDto articleDtoKoreanPress = article.toDto();
        articleDtoKoreanPress.setPress(Press.valueOf(articleDtoKoreanPress.getPress()).getPressValue());
        CompanyArticleDto articleDtoLowercasePress = article.toDto();
        articleDtoLowercasePress.setPress(Press.valueOf(articleDtoLowercasePress.getPress()).name().toLowerCase());

        // when
        companyService.registerCompany(samsungElectronics);
        articleService.registerArticle(beforeModifyArticle);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoOriginal, articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKoreanPress, articleDtoLowercasePress)) {
            mockMvc.perform(postWithCompanyArticleDto(modifyArticleFinishUrl, articleDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(modifyArticleFinishUrl + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(commonName)));
        }
        mockMvc.perform(getWithSingleParam(modifyArticleFinishUrl, NAME, encodeWithUTF8(commonName)))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_PATH),
                        model().attribute(VALUE, commonName));

        assertThat(articleService.findArticleByName(commonName).orElseThrow().toDto())
                .usingRecursiveComparison()
                .isEqualTo(articleDtoOriginal);
    }

    @DisplayName("기업 기사들 조회 페이지 접속")
    @Test
    public void accessCompanyArticlesInquiry() throws Exception {
        // given & when
        List<CompanyArticle> articleList = articleService.registerArticles(testCompanyArticle, testNewCompanyArticle);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_SELECT_VIEW + "company-articles-page"))
                .andReturn().getModelAndView()).getModelMap().get("articles"))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("기업 기사 없애기 페이지 접속")
    @Test
    public void accessCompanyArticleRid() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_ARTICLE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_PATH));
    }

    @DisplayName("기업 기사 없애기 완료 페이지 접속")
    @Test
    public void accessCompanyArticleRidFinish() throws Exception {
        // given & when
        CompanyArticle article = testCompanyArticle;
        String name = article.getName();

        // when & then
        Long number = articleService.registerArticle(article).getNumber();

        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, "numberOrName", String.valueOf(number)))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        articleService.registerArticle(article);

        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, "numberOrName", String.valueOf(name)))
                .andExpectAll(status().isFound(),
                        redirectedUrlPattern(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeWithUTF8(name)));

        mockMvc.perform(getWithSingleParam(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX, NAME, encodeWithUTF8(name)))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_PATH),
                        model().attribute(VALUE, name));

        assertThat(articleService.findArticles()).isEmpty();
    }
}