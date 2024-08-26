package springsideproject1.springsideproject1build.domain.validator;

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
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleBufferSimple;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;
import springsideproject1.springsideproject1build.util.test.CompanyTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyArticleFieldValidatorTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    @Autowired
    CompanyService companyService;

    // Request Key
    private final String nameDatePress = "nameDatePressString";
    private final String link = "linkString";

    // Request Value
    private final String dataTypeKorValue = "기사";
    private final String requestNameDatePress = testArticleStringBuffers.getNameDatePressString();
    private final String requestLink = testArticleStringBuffers.getLinkString();
    private final String requestSubjectCompany = testArticleStringBuffers.getSubjectCompany();

    // Assertion
    private final String addSingleProcessPage = ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
    private final String addStringProcessPage = ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage";
    private final String modifySingleProcessPage = UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    private final String modifySingleFinishUrl = UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleFieldValidatorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, COMPANY_ARTICLE_TABLE, true);
        resetTable(jdbcTemplateTest, COMPANY_TABLE, false);
    }

    @DisplayName("NotNull(공백)에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateSpaceCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setName(" ");
        articleDto.setPress(" ");
        articleDto.setSubjectCompany(" ");
        articleDto.setLink(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleProcessPage),
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
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, new CompanyArticleDto()))
                .andExpectAll(view().name(addSingleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(new CompanyArticleDto());
    }

    @DisplayName("URL에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateURLCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setLink("NotUrl");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Range에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateRangeCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setYear(1950);
        articleDto.setMonth(1);
        articleDto.setDays(1);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Restrict에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateRestrictCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setImportance(3);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("TypeButInvalid에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateTypeButInvalidCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("typeMismatch에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateTypeMismatchCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();

        // then
        mockMvc.perform(post(ADD_SINGLE_COMPANY_ARTICLE_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(NAME, articleDto.getName())
                        .param(PRESS, articleDto.getPress())
                        .param(SUBJECT_COMPANY, articleDto.getSubjectCompany())
                        .param(LINK, articleDto.getLink())
                        .param(YEAR, INVALID_VALUE)
                        .param(MONTH, INVALID_VALUE)
                        .param(DAYS, INVALID_VALUE)
                        .param(IMPORTANCE, INVALID_VALUE))
                .andExpectAll(view().name(addSingleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("Press의 typeMismatch에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validatePressTypeMismatchCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setPress(INVALID_VALUE);

        // then
        mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, (String) null),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("기사 리스트의 크기가 링크 리스트의 크기보다 큰, 문자열을 사용하는 기업 기사들 추가 검증")
    @Test
    public void articleListBiggerCompanyArticleAddWithString() throws Exception {
        // given & when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePress, requestNameDatePress);
                    put(SUBJECT_COMPANY, requestSubjectCompany);
                    put(link, testEqualDateArticle.getLink());
                }}))
                .andExpectAll(view().name(addStringProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("링크 리스트의 크기가 기사 리스트의 크기보다 큰, 문자열을 사용하는 기업 기사들 추가 검증")
    @Test
    public void linkListBiggerCompanyArticleAddWithString() throws Exception {
        // given & when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePress, CompanyArticleBufferSimple.builder().article(testNewArticle).build().getNameDatePressString());
                    put(SUBJECT_COMPANY, requestSubjectCompany);
                    put(link, requestLink);
                }}))
                .andExpectAll(view().name(addStringProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("서식이 올바르지 않은 입력일 값을 포함하는, 문자열을 사용하는 기업 기사들 추가 검증")
    @Test
    public void dateFormatCompanyArticleAddWithString() throws Exception {
        // given & when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePress, requestNameDatePress.replace("2024", INVALID_VALUE));
                    put(SUBJECT_COMPANY, requestSubjectCompany);
                    put(link, requestLink);
                }}))
                .andExpectAll(view().name(
                                URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX),
                        model().attribute(BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, NUMBER_FORMAT_LOCAL_DATE_ERROR)));
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

    @DisplayName("NotNull(공백)에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateSpaceCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setName(" ");
        articleDto.setPress(" ");
        articleDto.setSubjectCompany(" ");
        articleDto.setLink(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifySingleFinishUrl, articleDto))
                .andExpectAll(view().name(modifySingleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotNull(null)에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateNullCompanyArticleModify() throws Exception {
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifySingleFinishUrl, new CompanyArticleDto()))
                .andExpectAll(view().name(modifySingleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(new CompanyArticleDto());
    }

    @DisplayName("URL에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateURLCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setLink("NotUrl");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifySingleFinishUrl, articleDto))
                .andExpectAll(view().name(modifySingleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Range에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateRangeCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setYear(1950);
        articleDto.setMonth(1);
        articleDto.setDays(1);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifySingleFinishUrl, articleDto))
                .andExpectAll(view().name(modifySingleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Restrict에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateRestrictCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setImportance(3);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifySingleFinishUrl, articleDto))
                .andExpectAll(view().name(modifySingleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("TypeButInvalid에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateTypeButInvalidCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifySingleFinishUrl, articleDto))
                .andExpectAll(view().name(modifySingleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("typeMismatch에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateTypeMismatchCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();

        // then
        mockMvc.perform(post(modifySingleFinishUrl).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(NAME, articleDto.getName())
                        .param(PRESS, articleDto.getPress())
                        .param(SUBJECT_COMPANY, articleDto.getSubjectCompany())
                        .param(LINK, articleDto.getLink())
                        .param(YEAR, INVALID_VALUE)
                        .param(MONTH, INVALID_VALUE)
                        .param(DAYS, INVALID_VALUE)
                        .param(IMPORTANCE, INVALID_VALUE))
                .andExpectAll(view().name(modifySingleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("Press의 typeMismatch에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validatePressTypeMismatchCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();
        articleDto.setPress(INVALID_VALUE);

        // then
        mockMvc.perform(postWithCompanyArticleDto(modifySingleFinishUrl, articleDto))
                .andExpectAll(view().name(modifySingleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
                        model().attribute(ERROR, (String) null),
                        model().attributeExists(ARTICLE));
    }
}
