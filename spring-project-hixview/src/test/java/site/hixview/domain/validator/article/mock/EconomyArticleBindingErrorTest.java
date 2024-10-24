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
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.service.EconomyArticleService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.EconomyArticleTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_ECONOMY_ARTICLE_URL;
import static site.hixview.util.ControllerUtils.decodeWithUTF8;

@OnlyRealControllerContext
class EconomyArticleBindingErrorTest implements EconomyArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EconomyArticleService articleService;

    @Autowired
    private MessageSource messageSource;

    private final Logger log = LoggerFactory.getLogger(EconomyArticleBindingErrorTest.class);

    private final String IS_BEAN_VALIDATION_ERROR = "isBeanValidationError";
    private final String FIELD_ERROR_MAP = "fieldErrorMap";

    @DisplayName("NotBlank(공백)에 대한 경제 기사 추가 유효성 검증")
    @Test
    void validateNotBlankSpaceEconomyArticleAdd() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setName(" ");
        articleDto.setLink(" ");
        articleDto.setPress(" ");
        articleDto.setSubjectCountry(" ");
        articleDto.setTargetEconomyContents(new ObjectMapper().writeValueAsString(new HashMap<>() {{
            put(TARGET_ECONOMY_CONTENT, List.of(" "));
        }}));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(ADD_SINGLE_ECONOMY_ARTICLE_URL, articleDto))
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
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_COUNTRY)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.subjectCountry", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_ECONOMY_CONTENTS)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.targetEconomyContent", null, Locale.getDefault()));
    }

    @DisplayName("NotBlank(null)에 대한 경제 기사 추가 유효성 검증")
    @Test
    void validateNotBlankNullEconomyArticleAdd() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(ADD_SINGLE_ECONOMY_ARTICLE_URL,
                        new HashMap<>() {{
                            put(YEAR, String.valueOf(articleDto.getYear()));
                            put(MONTH, String.valueOf(articleDto.getMonth()));
                            put(DAYS, String.valueOf(articleDto.getDays()));
                            put(IMPORTANCE, String.valueOf(articleDto.getImportance()));
                            put(TARGET_ECONOMY_CONTENTS, String.valueOf(articleDto.getTargetEconomyContents()));
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
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_COUNTRY)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.subjectCountry", null, Locale.getDefault()));
    }

    @DisplayName("NotNull에 대한 경제 기사 추가 유효성 검증")
    @Test
    void validateNotNullEconomyArticleAdd() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(ADD_SINGLE_ECONOMY_ARTICLE_URL,
                        new HashMap<>() {{
                            put(NAME, articleDto.getName());
                            put(PRESS, articleDto.getPress());
                            put(LINK, articleDto.getLink());
                            put(SUBJECT_COUNTRY, articleDto.getSubjectCountry());
                            put(TARGET_ECONOMY_CONTENTS, articleDto.getTargetEconomyContents());
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

    @DisplayName("Pattern에 대한 경제 기사 추가 유효성 검증")
    @Test
    void validatePatternEconomyArticleAdd() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setLink(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(ADD_SINGLE_ECONOMY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Pattern.article.link", null, Locale.getDefault()));
    }

    @DisplayName("Range에 대한 경제 기사 추가 유효성 검증")
    @Test
    void validateRangeEconomyArticleAdd() throws Exception {
        // given & when
        EconomyArticleDto articleDtoFallShortOf = createTestEconomyArticleDto();
        articleDtoFallShortOf.setYear(1959);
        articleDtoFallShortOf.setMonth(0);
        articleDtoFallShortOf.setDays(0);

        EconomyArticleDto articleDtoExceed = createTestEconomyArticleDto();
        articleDtoExceed.setYear(2100);
        articleDtoExceed.setMonth(13);
        articleDtoExceed.setDays(32);

        // then
        for (EconomyArticleDto articleDto : List.of(articleDtoFallShortOf, articleDtoExceed)) {
            Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(ADD_SINGLE_ECONOMY_ARTICLE_URL, articleDto))
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

    @DisplayName("Restrict에 대한 경제 기사 추가 유효성 검증")
    @Test
    void validateRestrictEconomyArticleAdd() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setImportance(3);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(ADD_SINGLE_ECONOMY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(IMPORTANCE)))
                .isEqualTo(messageSource.getMessage("Restrict.article.importance", null, Locale.getDefault()));
    }

    @DisplayName("Size에 대한 경제 기사 추가 유효성 검증")
    @Test
    void validateSizeEconomyArticleAdd() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setName(getRandomLongString(81));
        articleDto.setLink(createTestEconomyArticleDto().getLink() + getRandomLongString(401));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(ADD_SINGLE_ECONOMY_ARTICLE_URL, articleDto))
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

    @DisplayName("Press의 typeMismatch에 대한 경제 기사 추가 유효성 검증")
    @Test
    void validatePressTypeMismatchEconomyArticleAdd() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setPress(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(ADD_SINGLE_ECONOMY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(PRESS)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.article.press", null, Locale.getDefault()));
    }

    @DisplayName("Country의 typeMismatch에 대한 경제 기사 추가 유효성 검증")
    @Test
    void validateFirstCategoryTypeMismatchEconomyArticleAdd() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setSubjectCountry(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(ADD_SINGLE_ECONOMY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_COUNTRY)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.company.country", null, Locale.getDefault()));
    }
    
    @DisplayName("NotBlank(공백)에 대한 경제 기사 변경 유효성 검증")
    @Test
    void validateNotBlankSpaceEconomyArticleModify() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setName(" ");
        articleDto.setLink(" ");
        articleDto.setPress(" ");
        articleDto.setSubjectCountry(" ");
        articleDto.setTargetEconomyContents(new ObjectMapper().writeValueAsString(new HashMap<>() {{
            put(TARGET_ECONOMY_CONTENT, List.of(" "));
        }}));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(modifyEconomyArticleFinishUrl, articleDto))
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
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_COUNTRY)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.subjectCountry", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_ECONOMY_CONTENTS)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.targetEconomyContent", null, Locale.getDefault()));
    }

    @DisplayName("NotBlank(null)에 대한 경제 기사 변경 유효성 검증")
    @Test
    void validateNotBlankNullEconomyArticleModify() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(modifyEconomyArticleFinishUrl,
                        new HashMap<>() {{
                            put(YEAR, String.valueOf(articleDto.getYear()));
                            put(MONTH, String.valueOf(articleDto.getMonth()));
                            put(DAYS, String.valueOf(articleDto.getDays()));
                            put(IMPORTANCE, String.valueOf(articleDto.getImportance()));
                            put(TARGET_ECONOMY_CONTENTS, String.valueOf(articleDto.getTargetEconomyContents()));
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
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_COUNTRY)))
                .isEqualTo(messageSource.getMessage("NotBlank.article.subjectCountry", null, Locale.getDefault()));
    }

    @DisplayName("NotNull에 대한 경제 기사 변경 유효성 검증")
    @Test
    void validateNotNullEconomyArticleModify() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(modifyEconomyArticleFinishUrl,
                        new HashMap<>() {{
                            put(NAME, articleDto.getName());
                            put(PRESS, articleDto.getPress());
                            put(LINK, articleDto.getLink());
                            put(SUBJECT_COUNTRY, articleDto.getSubjectCountry());
                            put(TARGET_ECONOMY_CONTENTS, articleDto.getTargetEconomyContents());
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

    @DisplayName("Pattern에 대한 경제 기사 변경 유효성 검증")
    @Test
    void validatePatternEconomyArticleModify() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setLink(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(modifyEconomyArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Pattern.article.link", null, Locale.getDefault()));
    }

    @DisplayName("Range에 대한 경제 기사 변경 유효성 검증")
    @Test
    void validateRangeEconomyArticleModify() throws Exception {
        // given & when
        EconomyArticleDto articleDtoFallShortOf = createTestEconomyArticleDto();
        articleDtoFallShortOf.setYear(1959);
        articleDtoFallShortOf.setMonth(0);
        articleDtoFallShortOf.setDays(0);

        EconomyArticleDto articleDtoExceed = createTestEconomyArticleDto();
        articleDtoExceed.setYear(2100);
        articleDtoExceed.setMonth(13);
        articleDtoExceed.setDays(32);

        // then
        for (EconomyArticleDto articleDto : List.of(articleDtoFallShortOf, articleDtoExceed)) {
            Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(modifyEconomyArticleFinishUrl, articleDto))
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

    @DisplayName("Restrict에 대한 경제 기사 변경 유효성 검증")
    @Test
    void validateRestrictEconomyArticleModify() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setImportance(3);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(modifyEconomyArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(IMPORTANCE)))
                .isEqualTo(messageSource.getMessage("Restrict.article.importance", null, Locale.getDefault()));
    }

    @DisplayName("Size에 대한 경제 기사 변경 유효성 검증")
    @Test
    void validateSizeEconomyArticleModify() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setName(getRandomLongString(81));
        articleDto.setLink(createTestEconomyArticleDto().getLink() + getRandomLongString(401));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(modifyEconomyArticleFinishUrl, articleDto))
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

    @DisplayName("Press의 typeMismatch에 대한 경제 기사 변경 유효성 검증")
    @Test
    void validatePressTypeMismatchEconomyArticleModify() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setPress(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(modifyEconomyArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(PRESS)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.article.press", null, Locale.getDefault()));
    }

    @DisplayName("Country의 typeMismatch에 대한 경제 기사 변경 유효성 검증")
    @Test
    void validateFirstCategoryTypeMismatchEconomyArticleModify() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        articleDto.setSubjectCountry(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithEconomyArticleDto(modifyEconomyArticleFinishUrl, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(SUBJECT_COUNTRY)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.company.country", null, Locale.getDefault()));
    }
}
