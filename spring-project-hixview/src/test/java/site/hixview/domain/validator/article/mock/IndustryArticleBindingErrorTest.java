package site.hixview.domain.validator.article.mock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.IndustryArticleTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_INDUSTRY_ARTICLE_URL;
import static site.hixview.util.ControllerUtils.decodeWithUTF8;

@OnlyRealControllerContext
class IndustryArticleBindingErrorTest implements IndustryArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndustryArticleService articleService;

    @Autowired
    private MessageSource messageSource;

    private final Logger log = LoggerFactory.getLogger(IndustryArticleBindingErrorTest.class);

    private final String IS_BEAN_VALIDATION_ERROR = "isBeanValidationError";
    private final String FIELD_ERROR_MAP = "fieldErrorMap";

    @DisplayName("NotBlank(공백)에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateNotBlankSpaceIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(" ");
        articleDto.setLink(" ");
        articleDto.setPress(" ");
        articleDto.setSubjectFirstCategory(" ");
        articleDto.setSubjectSecondCategories(new ObjectMapper().writeValueAsString(new HashMap<>() {{
            put(SUBJECT_SECOND_CATEGORY, List.of(" "));
        }}));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(PRESS)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.press", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_FIRST_CATEGORY)))
                .isEqualTo(messageSource.getMessage("NotBlank.company.firstCategory", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_SECOND_CATEGORIES)))
                .isEqualTo(messageSource.getMessage("NotBlank.company.secondCategory", null, Locale.getDefault()));
    }

    @DisplayName("NotBlank(null)에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateNotBlankNullIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(ADD_SINGLE_INDUSTRY_ARTICLE_URL,
                        new HashMap<>() {{
                            put(YEAR, String.valueOf(articleDto.getYear()));
                            put(MONTH, String.valueOf(articleDto.getMonth()));
                            put(DAYS, String.valueOf(articleDto.getDays()));
                            put(IMPORTANCE, String.valueOf(articleDto.getImportance()));
                            put(SUBJECT_SECOND_CATEGORIES, String.valueOf(articleDto.getSubjectSecondCategories()));
                        }}))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(PRESS)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.press", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_FIRST_CATEGORY)))
                .isEqualTo(messageSource.getMessage("NotBlank.company.firstCategory", null, Locale.getDefault()));
    }

    @DisplayName("NotNull에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateNotNullIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(ADD_SINGLE_INDUSTRY_ARTICLE_URL,
                        new HashMap<>() {{
                            put(NAME, articleDto.getName());
                            put(PRESS, articleDto.getPress());
                            put(LINK, articleDto.getLink());
                            put(SUBJECT_FIRST_CATEGORY, articleDto.getSubjectFirstCategory());
                            put(SUBJECT_SECOND_CATEGORIES, articleDto.getSubjectSecondCategories());
                        }}))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(YEAR)))
                .isEqualTo(messageSource.getMessage("NotNull.article.year", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(MONTH)))
                .isEqualTo(messageSource.getMessage("NotNull.article.month", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(DAYS)))
                .isEqualTo(messageSource.getMessage("NotNull.article.days", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(IMPORTANCE)))
                .isEqualTo(messageSource.getMessage("NotNull.article.importance", null, Locale.getDefault()));
    }

    @DisplayName("Pattern에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validatePatternIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setLink(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Pattern.article.link", null, Locale.getDefault()));
    }

    @DisplayName("Range에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateRangeIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDtoFallShortOf = createTestIndustryArticleDto();
        articleDtoFallShortOf.setYear(1959);
        articleDtoFallShortOf.setMonth(0);
        articleDtoFallShortOf.setDays(0);

        IndustryArticleDto articleDtoExceed = createTestIndustryArticleDto();
        articleDtoExceed.setYear(2100);
        articleDtoExceed.setMonth(13);
        articleDtoExceed.setDays(32);

        // then
        for (IndustryArticleDto articleDto : List.of(articleDtoFallShortOf, articleDtoExceed)) {
            Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                    .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
            assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

            @SuppressWarnings("unchecked")
            Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
            assertThat(decodeWithUTF8((fieldErrorMap).get(YEAR)))
                    .isEqualTo(messageSource.getMessage("Range.article.year", new Object[]{"연", 2099, 1960}, Locale.getDefault()));
            assertThat(decodeWithUTF8((fieldErrorMap).get(MONTH)))
                    .isEqualTo(messageSource.getMessage("Range.article.month", new Object[]{"월", 12, 1}, Locale.getDefault()));
            assertThat(decodeWithUTF8((fieldErrorMap).get(DAYS)))
                    .isEqualTo(messageSource.getMessage("Range.article.days", new Object[]{"일", 31, 1}, Locale.getDefault()));
        }
    }

    @DisplayName("Restrict에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateRestrictIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setImportance(3);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(IMPORTANCE)))
                .isEqualTo(messageSource.getMessage("Restrict.article.importance", null, Locale.getDefault()));
    }

    @DisplayName("Size에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateSizeIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(getRandomLongString(81));
        articleDto.setLink(createTestIndustryArticleDto().getLink() + getRandomLongString(401));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("Size.article.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Size.article.link", null, Locale.getDefault()));
    }

    @DisplayName("Press의 typeMismatch에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validatePressTypeMismatchIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setPress(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(PRESS)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.article.press", null, Locale.getDefault()));
    }

    @DisplayName("FirstCategory의 typeMismatch에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateFirstCategoryTypeMismatchIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setSubjectFirstCategory(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_FIRST_CATEGORY)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.company.firstCategory", null, Locale.getDefault()));
    }

    @DisplayName("SecondCategory의 typeMismatch에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateSecondCategoryTypeMismatchIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setSubjectSecondCategories(new ObjectMapper().writeValueAsString(new HashMap<String, List<String>>() {{
            put(SUBJECT_SECOND_CATEGORY, List.of(INVALID_VALUE));
        }}));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_SECOND_CATEGORIES)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.company.secondCategory", null, Locale.getDefault()));
    }

    @DisplayName("NotBlank(공백)에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateNotBlankSpaceIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(" ");
        articleDto.setLink(" ");
        articleDto.setPress(" ");
        articleDto.setSubjectFirstCategory(" ");
        articleDto.setSubjectSecondCategories(new ObjectMapper().writeValueAsString(new HashMap<>() {{
            put(SUBJECT_SECOND_CATEGORY, List.of(" "));
        }}));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(PRESS)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.press", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_FIRST_CATEGORY)))
                .isEqualTo(messageSource.getMessage("NotBlank.company.firstCategory", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_SECOND_CATEGORIES)))
                .isEqualTo(messageSource.getMessage("NotBlank.company.secondCategory", null, Locale.getDefault()));
    }

    @DisplayName("NotBlank(null)에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateNotBlankNullIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(modifyIndustryArticleFinishUrl,
                        new HashMap<>() {{
                            put(YEAR, String.valueOf(articleDto.getYear()));
                            put(MONTH, String.valueOf(articleDto.getMonth()));
                            put(DAYS, String.valueOf(articleDto.getDays()));
                            put(IMPORTANCE, String.valueOf(articleDto.getImportance()));
                            put(SUBJECT_SECOND_CATEGORIES, String.valueOf(articleDto.getSubjectSecondCategories()));
                        }}))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(PRESS)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.press", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_FIRST_CATEGORY)))
                .isEqualTo(messageSource.getMessage("NotBlank.company.firstCategory", null, Locale.getDefault()));
    }

    @DisplayName("NotNull에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateNotNullIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(modifyIndustryArticleFinishUrl,
                        new HashMap<>() {{
                            put(NAME, articleDto.getName());
                            put(PRESS, articleDto.getPress());
                            put(LINK, articleDto.getLink());
                            put(SUBJECT_FIRST_CATEGORY, articleDto.getSubjectFirstCategory());
                            put(SUBJECT_SECOND_CATEGORIES, articleDto.getSubjectSecondCategories());
                        }}))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(YEAR)))
                .isEqualTo(messageSource.getMessage("NotNull.article.year", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(MONTH)))
                .isEqualTo(messageSource.getMessage("NotNull.article.month", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(DAYS)))
                .isEqualTo(messageSource.getMessage("NotNull.article.days", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(IMPORTANCE)))
                .isEqualTo(messageSource.getMessage("NotNull.article.importance", null, Locale.getDefault()));
    }

    @DisplayName("Pattern에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validatePatternIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setLink(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Pattern.article.link", null, Locale.getDefault()));
    }

    @DisplayName("Range에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateRangeIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDtoFallShortOf = createTestIndustryArticleDto();
        articleDtoFallShortOf.setYear(1959);
        articleDtoFallShortOf.setMonth(0);
        articleDtoFallShortOf.setDays(0);

        IndustryArticleDto articleDtoExceed = createTestIndustryArticleDto();
        articleDtoExceed.setYear(2100);
        articleDtoExceed.setMonth(13);
        articleDtoExceed.setDays(32);

        // then
        for (IndustryArticleDto articleDto : List.of(articleDtoFallShortOf, articleDtoExceed)) {
            Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                    .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
            assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

            @SuppressWarnings("unchecked")
            Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
            assertThat(decodeWithUTF8((fieldErrorMap).get(YEAR)))
                    .isEqualTo(messageSource.getMessage("Range.article.year", new Object[]{"연", 2099, 1960}, Locale.getDefault()));
            assertThat(decodeWithUTF8((fieldErrorMap).get(MONTH)))
                    .isEqualTo(messageSource.getMessage("Range.article.month", new Object[]{"월", 12, 1}, Locale.getDefault()));
            assertThat(decodeWithUTF8((fieldErrorMap).get(DAYS)))
                    .isEqualTo(messageSource.getMessage("Range.article.days", new Object[]{"일", 31, 1}, Locale.getDefault()));
        }
    }

    @DisplayName("Restrict에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateRestrictIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setImportance(3);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(IMPORTANCE)))
                .isEqualTo(messageSource.getMessage("Restrict.article.importance", null, Locale.getDefault()));
    }

    @DisplayName("Size에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateSizeIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(getRandomLongString(81));
        articleDto.setLink(createTestIndustryArticleDto().getLink() + getRandomLongString(401));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("Size.article.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Size.article.link", null, Locale.getDefault()));
    }

    @DisplayName("Press의 typeMismatch에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validatePressTypeMismatchIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setPress(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(PRESS)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.article.press", null, Locale.getDefault()));
    }

    @DisplayName("FirstCategory의 typeMismatch에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateFirstCategoryTypeMismatchIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setSubjectFirstCategory(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_FIRST_CATEGORY)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.company.firstCategory", null, Locale.getDefault()));
    }

    @DisplayName("SecondCategory의 typeMismatch에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateSecondCategoryTypeMismatchIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setSubjectSecondCategories(new ObjectMapper().writeValueAsString(new HashMap<String, List<String>>() {{
            put(SUBJECT_SECOND_CATEGORY, List.of(INVALID_VALUE));
        }}));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_SECOND_CATEGORIES)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.company.secondCategory", null, Locale.getDefault()));
    }
}
