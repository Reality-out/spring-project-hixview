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
import org.springframework.ui.ModelMap;
import springsideproject1.springsideproject1build.domain.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.article.CompanyArticleDtoNoNumber;
import springsideproject1.springsideproject1build.service.CompanyArticleService;
import springsideproject1.springsideproject1build.service.CompanyService;
import springsideproject1.springsideproject1build.utility.test.CompanyArticleTestUtility;
import springsideproject1.springsideproject1build.utility.test.CompanyTestUtility;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT.*;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.utility.MainUtils.decodeUTF8;
import static springsideproject1.springsideproject1build.utility.MainUtils.encodeUTF8;
import static springsideproject1.springsideproject1build.utility.WordUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerCompanyArticleControllerTest implements CompanyArticleTestUtility, CompanyTestUtility {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;
    private final String dataTypeKorValue = "기사";
    private final String keyValue = "기사명";

    @Autowired
    public ManagerCompanyArticleControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, companyArticleTable, true);
        resetTable(jdbcTemplateTest, companyTable, true);
    }

    @DisplayName("기업 기사 추가 페이지 접속")
    @Test
    public void accessCompanyArticleAdd() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("기업 기사 추가 완료 페이지 접속")
    @Test
    public void accessCompanyArticleAddFinish() throws Exception {
        // given
        CompanyArticleDtoNoNumber articleDto = createTestArticleDtoNoNumber();

        // when
        companyService.registerCompany(createSamsungElectronics());

        // then
        mockMvc.perform(postWithCompanyArticleDtoNoNumber(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(articleDto.getName())));

        mockMvc.perform(getWithSingleParam(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX,
                        NAME, encodeUTF8(articleDto.getName())))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_ADD_VIEW + VIEW_SINGLE_FINISH_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(VALUE, articleDto.getName()));
    }

    @DisplayName("대상 기업의 NotFound에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateNotFoundSubjectCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDtoNoNumber articleDto = createTestArticleDtoNoNumber();
        articleDto.setSubjectCompany(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDtoNoNumber(
                ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotNull(공백)에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateSpaceCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDtoNoNumber articleDto = createTestArticleDtoNoNumber();
        articleDto.setName(" ");
        articleDto.setPress(" ");
        articleDto.setSubjectCompany(" ");
        articleDto.setLink(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDtoNoNumber(
                ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotNull(null)에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateNullCompanyArticleAdd() throws Exception {
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDtoNoNumber(
                ADD_SINGLE_COMPANY_ARTICLE_URL, new CompanyArticleDtoNoNumber()))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attributeExists(ARTICLE))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(new CompanyArticleDtoNoNumber());
    }

    @DisplayName("URL에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateURLCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDtoNoNumber articleDto = createTestArticleDtoNoNumber();
        articleDto.setLink("NotUrl");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDtoNoNumber(
                ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Range에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateRangeCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDtoNoNumber articleDto = createTestArticleDtoNoNumber();
        articleDto.setYear(1950);
        articleDto.setMonth(1);
        articleDto.setDate(1);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDtoNoNumber(
                ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attributeExists(ARTICLE))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Restrict에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateRestrictCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDtoNoNumber articleDto = createTestArticleDtoNoNumber();
        articleDto.setImportance(3);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDtoNoNumber(
                ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attributeExists(ARTICLE))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("TypeButInvalid에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateTypeButInvalidCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDtoNoNumber articleDto = createTestArticleDtoNoNumber();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDate(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDtoNoNumber(
                ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attributeExists(ARTICLE))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("typeMismatch에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateTypeMismatchCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDtoNoNumber articleDto = createTestArticleDtoNoNumber();

        // then
        mockMvc.perform(post(ADD_SINGLE_COMPANY_ARTICLE_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param(NAME, articleDto.getName())
                                .param("press", articleDto.getPress())
                                .param("subjectCompany", articleDto.getSubjectCompany())
                                .param("link", articleDto.getLink())
                                .param("year", INVALID_VALUE)
                                .param("month", INVALID_VALUE)
                                .param("date", INVALID_VALUE)
                                .param("importance", INVALID_VALUE))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("Press의 typeMismatch에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validatePressTypeMismatchCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDtoNoNumber articleDto = createTestArticleDtoNoNumber();
        articleDto.setPress(INVALID_VALUE);

        // then
        mockMvc.perform(postWithCompanyArticleDtoNoNumber(
                        ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("중복 기업 기사명을 사용하는 기사 추가")
    @Test
    public void duplicatedNameCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticle article1 = createTestArticle();
        String commonName = article1.getName();
        CompanyArticleDtoNoNumber articleDto2 = createTestNewArticleDtoNoNumber();
        articleDto2.setName(commonName);

        // when
        articleService.registerArticle(article1);
        companyService.registerCompany(createSamsungElectronics());

        // then
        mockMvc.perform(postWithCompanyArticleDtoNoNumber(
                ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto2))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, EXIST_COMPANY_ARTICLE_ERROR),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("문자열을 사용하는 기업 기사들 추가 페이지 접속")
    @Test
    public void accessCompanyArticleAddWithString() throws Exception {
        mockMvc.perform(get(ADD_COMPANY_ARTICLE_WITH_STRING_URL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage"),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue));
    }

    @DisplayName("문자열을 사용하는 기업 기사들 추가 완료 페이지 접속")
    @Test
    public void accessCompanyArticleAddWithStringFinish() throws Exception {
        // given
        List<String> articleString = createTestStringArticle();
        List<String> nameList = Stream.of(createTestEqualDateArticle(), createTestNewArticle())
                .map(CompanyArticle::getName).collect(Collectors.toList());

        // when
        companyService.registerCompany(createSamsungElectronics());

        // then
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put("subjectCompany", articleString.getFirst());
                    put("articleString", articleString.get(1));
                    put("linkString", articleString.getLast());
                }}))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapPost.get(nameListString)).usingRecursiveComparison().isEqualTo(nameListForURL);
        assertThat(modelMapPost.get(BEAN_VALIDATION_ERROR)).isEqualTo(String.valueOf(false));
        assertThat(modelMapPost.get(ERROR_SINGLE)).isEqualTo(null);

        ModelMap modelMapGet = requireNonNull(mockMvc.perform(getWithMultipleParam(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX,
                        new HashMap<>() {{
                            put(nameListString, nameListForURL);
                            put(BEAN_VALIDATION_ERROR, String.valueOf(false));
                            put(ERROR_SINGLE, null);
                        }}))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_ADD_VIEW + "multipleFinishPage"),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(nameListString, decodeUTF8(nameList)))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapGet.get(nameListString)).usingRecursiveComparison().isEqualTo(decodeUTF8(nameList));
        assertThat(modelMapGet.get(BEAN_VALIDATION_ERROR)).isEqualTo(false);
        assertThat(modelMapGet.get(ERROR_SINGLE)).isEqualTo(null);
    }

    @DisplayName("존재하지 않는 대상 기업을 사용하는, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void addCompanyArticleWithStringNotExistSubject() throws Exception {
        // given & when
        List<String> articleString = createTestStringArticle();

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put("subjectCompany", INVALID_VALUE);
                    put("articleString", articleString.get(1));
                    put("linkString", String.valueOf(articleString.getLast()));
                }}))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage"),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }

    @DisplayName("URL 값이 아닌 링크를 사용하는, 문자열을 사용하는 기업 기사들 추가 검증")
    @Test
    public void validateNotMatchLinkCompanyArticleAddWithString() throws Exception {
        // given
        List<String> articleString = createTestStringArticle();

        // when
        companyService.registerCompany(createSamsungElectronics());

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put("subjectCompany", articleString.getFirst());
                    put("articleString", articleString.get(1));
                    put("linkString", String.valueOf(List.of("", "")));
                }}))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage"),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, NOT_MATCH_LINK_ERROR)));

        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put("subjectCompany", articleString.getFirst());
                    put("articleString", articleString.get(1));
                    put("linkString", String.valueOf(List.of(" ", " ")));
                }}))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage"),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, NOT_MATCH_LINK_ERROR)));

        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put("subjectCompany", articleString.getFirst());
                    put("articleString", articleString.get(1));
                    put("linkString", String.valueOf(List.of(INVALID_VALUE, INVALID_VALUE)));
                }}))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage"),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, NOT_MATCH_LINK_ERROR)));
    }

    @DisplayName("기사 리스트의 크기가 링크 리스트의 크기보다 큰, 문자열을 사용하는 기업 기사들 추가 검증")
    @Test
    public void registerArticleListBiggerThanLinkListCompanyArticleWithString() throws Exception {
        // given
        List<String> articleString = new ArrayList<>(createTestStringArticle());
        articleString.set(2, createTestEqualDateArticle().getLink());

        // when
        companyService.registerCompany(createSamsungElectronics());

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put("subjectCompany", articleString.getFirst());
                    put("articleString", articleString.get(1));
                    put("linkString", articleString.getLast());
                }}))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage"),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("링크 리스트의 크기가 기사 리스트의 크기보다 큰, 문자열을 사용하는 기업 기사들 추가 검증")
    @Test
    public void registerLinkListBiggerThanArticleListCompanyArticleWithString() throws Exception {
        // given
        List<String> articleString = new ArrayList<>(createTestStringArticle());
        articleString.set(1, String.join(System.lineSeparator(),
                List.of("삼성전자도 현대차 이어 인도법인 상장 가능성, '코리아 디스카운트' 해소 기회",
                        "(2024-6-18, BUSINESS_POST)")));

        // when
        companyService.registerCompany(createSamsungElectronics());

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put("subjectCompany", articleString.getFirst());
                    put("articleString", articleString.get(1));
                    put("linkString", articleString.getLast());
                }}))
                .andExpectAll(view().name(ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage"),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("서식이 올바르지 않은 입력일 값을 포함하는, 문자열을 사용하는 기업 기사들 추가 검증")
    @Test
    public void registerNotCorrectNumberFormatDateCompanyArticleWithString() throws Exception {
        // given
        List<String> articleString = new ArrayList<>(createTestStringArticle());
        articleString.set(1, articleString.get(1).replace("2024", INVALID_VALUE));

        // when
        companyService.registerCompany(createSamsungElectronics());

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put("subjectCompany", articleString.getFirst());
                    put("articleString", articleString.get(1));
                    put("linkString", articleString.getLast());
                }}))
                .andExpectAll(view().name(
                                URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX),
                        model().attribute(BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, NUMBER_FORMAT_LOCAL_DATE_ERROR)));
    }

    @DisplayName("기업 기사 단일 문자열로 중복으로 등록")
    @Test
    public void registerDuplicatedCompanyArticleWithString() throws Exception {
        // given
        CompanyArticle article = createTestNewArticle();
        List<String> articleString = createTestStringArticle();

        // when
        articleService.registerArticle(article);
        companyService.registerCompany(createSamsungElectronics());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put("subjectCompany", articleString.getFirst());
                    put("articleString", articleString.get(1));
                    put("linkString", articleString.getLast());
                }}))
                .andExpectAll(view().name(
                                URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX),
                        model().attribute(BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, EXIST_COMPANY_ARTICLE_ERROR))
                .andReturn().getModelAndView()).getModelMap().get("nameList"))
                .usingRecursiveComparison().isEqualTo(toStringForUrl(List.of(createTestEqualDateArticle().getName())));
    }

    @DisplayName("기업 기사 변경 페이지 접속")
    @Test
    public void accessCompanyArticleModify() throws Exception {
        mockMvc.perform(get(UPDATE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue));
    }

    @DisplayName("기업 기사 변경 페이지 내 이름 검색")
    @Test
    public void searchNameCompanyArticleModify() throws Exception {
        // given
        CompanyArticle article = createTestArticle();

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                UPDATE_COMPANY_ARTICLE_URL, "numberOrName", article.getName()))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(article.toDto());
    }

    @DisplayName("NotFound에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateNotFoundCompanyArticleModify() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_ARTICLE_URL, "numberOrName", ""))
                .andExpectAll(view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_ARTICLE_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR)));
    }

    @DisplayName("기업 기사 변경 완료 페이지 접속")
    @Test
    public void accessCompanyArticleModifyFinish() throws Exception {
        // given
        CompanyArticle article = createTestArticle();

        // when
        article = articleService.registerArticle(article);

        // then
        mockMvc.perform(postWithCompanyArticle(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX, article))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(article.getName())));

        mockMvc.perform(getWithSingleParam(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX,
                        NAME, encodeUTF8(article.getName())))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_UPDATE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(VALUE, article.getName()));
    }

    @DisplayName("기업 기사들 보기 페이지 접속")
    @Test
    public void accessCompanyArticlesSee() throws Exception {
        // given & when
        List<CompanyArticle> articleList = articleService.registerArticles(createTestArticle(), createTestNewArticle());

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_SELECT_VIEW + "companyArticlesPage"))
                .andReturn().getModelAndView()).getModelMap().get("articles"))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("기업 기사 없애기 페이지 접속")
    @Test
    public void accessCompanyArticleRid() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(DATA_TYPE_ENGLISH, ARTICLE),
                        model().attribute(REMOVE_KEY, NAME));
    }

    @DisplayName("기업 기사 없애기 완료 페이지 접속")
    @Test
    public void accessCompanyArticleRidFinish() throws Exception {
        // given & when
        CompanyArticle article = createTestArticle();
        String name = article.getName();

        // when
        articleService.registerArticle(article);

        // then
        mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, NAME, name))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(NAME, encodeUTF8(name)));

        mockMvc.perform(getWithSingleParam(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX, NAME, encodeUTF8(name)))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(KEY, keyValue),
                        model().attribute(VALUE, name));
    }
}