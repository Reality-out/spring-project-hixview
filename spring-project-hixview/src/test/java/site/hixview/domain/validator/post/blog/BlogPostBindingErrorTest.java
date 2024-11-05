package site.hixview.domain.validator.post.blog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.BlogPostTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.RequestPath.ADD_BLOG_POST_PATH;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;
import static site.hixview.util.ControllerUtils.decodeWithUTF8;

@OnlyRealControllerContext
class BlogPostBindingErrorTest implements BlogPostTestUtils {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogPostService blogPostService;

    @DisplayName("NotBlank(공백)에 대한 블로그 포스트 추가 유효성 검증")
    @Test
    void validateNotBlankSpaceBlogPostAdd() throws Exception {
        // given & when
        ObjectMapper objectMapper = new ObjectMapper();
        BlogPostDto postDto = createTestBlogPostCompanyDto();
        postDto.setName(" ");
        postDto.setLink(" ");
        postDto.setClassification(" ");
        postDto.setTargetName(" ");
        postDto.setTargetArticleNames(objectMapper.writeValueAsString(new HashMap<>() {{
            put(TARGET_ARTICLE_NAME, List.of(" "));
        }}));
        postDto.setTargetArticleLinks(objectMapper.writeValueAsString(new HashMap<>() {{
            put(TARGET_ARTICLE_LINK, List.of(" "));
        }}));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(ADD_BLOG_POST_PATH, postDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(CLASSIFICATION)))
                .isEqualTo(messageSource.getMessage("NotBlank.enum.classification", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetName", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_ARTICLE_NAMES)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetArticleName", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_ARTICLE_LINKS)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetArticleLink", null, Locale.getDefault()));
    }

    @DisplayName("NotBlank(null)에 대한 블로그 포스트 추가 유효성 검증")
    @Test
    void validateNotBlankNullBlogPostAdd() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(ADD_BLOG_POST_PATH,
                        new HashMap<>() {{
                            put(YEAR, postDto.getYear().toString());
                            put(MONTH, postDto.getMonth().toString());
                            put(DAYS, postDto.getDays().toString());
                            put(TARGET_ARTICLE_NAMES, postDto.getTargetArticleNames());
                            put(TARGET_ARTICLE_LINKS, postDto.getTargetArticleLinks());
                        }}))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(CLASSIFICATION)))
                .isEqualTo(messageSource.getMessage("NotBlank.enum.classification", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetName", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_IMAGE_PATH)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetImagePath", null, Locale.getDefault()));
    }

    @DisplayName("NotNull에 대한 블로그 포스트 추가 유효성 검증")
    @Test
    void validateNotNullBlogPostAdd() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(ADD_BLOG_POST_PATH,
                        new HashMap<>() {{
                            put(NAME, postDto.getName());
                            put(LINK, postDto.getLink());
                            put(CLASSIFICATION, postDto.getClassification());
                            put(TARGET_NAME, postDto.getTargetName());
                            put(TARGET_IMAGE_PATH, postDto.getTargetImagePath());
                            put(TARGET_ARTICLE_NAMES, postDto.getTargetArticleNames());
                            put(TARGET_ARTICLE_LINKS, postDto.getTargetArticleLinks());
                        }}))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(YEAR)))
                .isEqualTo(messageSource.getMessage("NotNull.post.year", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(MONTH)))
                .isEqualTo(messageSource.getMessage("NotNull.post.month", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(DAYS)))
                .isEqualTo(messageSource.getMessage("NotNull.post.days", null, Locale.getDefault()));
    }

    @DisplayName("Pattern에 대한 블로그 포스트 추가 유효성 검증")
    @Test
    void validatePatternBlogPostAdd() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();
        postDto.setLink(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(ADD_BLOG_POST_PATH, postDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Pattern.post.link", null, Locale.getDefault()));
    }

    @DisplayName("Range에 대한 블로그 포스트 추가 유효성 검증")
    @Test
    void validateRangeBlogPostAdd() throws Exception {
        // given & when
        BlogPostDto postDtoFallShortOf = createTestBlogPostCompanyDto();
        postDtoFallShortOf.setYear(1959);
        postDtoFallShortOf.setMonth(0);
        postDtoFallShortOf.setDays(0);

        BlogPostDto postDtoExceed = createTestBlogPostCompanyDto();
        postDtoExceed.setYear(2100);
        postDtoExceed.setMonth(13);
        postDtoExceed.setDays(32);

        // then
        for (BlogPostDto postDto : List.of(postDtoFallShortOf, postDtoExceed)) {
            Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(ADD_BLOG_POST_PATH, postDto))
                    .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

            @SuppressWarnings("unchecked")
            Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
            assertThat(decodeWithUTF8((fieldErrorMap).get(YEAR)))
                    .isEqualTo(messageSource.getMessage("Range.post.year", new Object[]{"연", 2099, 1960}, Locale.getDefault()));
            assertThat(decodeWithUTF8((fieldErrorMap).get(MONTH)))
                    .isEqualTo(messageSource.getMessage("Range.post.month", new Object[]{"월", 12, 1}, Locale.getDefault()));
            assertThat(decodeWithUTF8((fieldErrorMap).get(DAYS)))
                    .isEqualTo(messageSource.getMessage("Range.post.days", new Object[]{"일", 31, 1}, Locale.getDefault()));
        }
    }

    @DisplayName("Size에 대한 블로그 포스트 추가 유효성 검증")
    @Test
    void validateSizeBlogPostAdd() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();
        postDto.setName(getRandomLongString(81));
        postDto.setLink(createTestBlogPostCompanyDto().getLink() + getRandomLongString(401));
        postDto.setTargetName(getRandomLongString(81));
        postDto.setTargetImagePath(getRandomLongString(81));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(ADD_BLOG_POST_PATH, postDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("Size.post.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Size.post.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_NAME)))
                .isEqualTo(messageSource.getMessage("Size.post.targetName", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_IMAGE_PATH)))
                .isEqualTo(messageSource.getMessage("Size.post.targetImagePath", null, Locale.getDefault()));
    }

    @DisplayName("Classification의 typeMismatch에 대한 블로그 포스트 추가 유효성 검증")
    @Test
    void validateClassificationTypeMismatchBlogPostAdd() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();
        postDto.setClassification(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(ADD_BLOG_POST_PATH, postDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(CLASSIFICATION)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.classification", null, Locale.getDefault()));
    }

    @DisplayName("NotBlank(공백)에 대한 블로그 포스트 변경 유효성 검증")
    @Test
    void validateNotBlankSpaceBlogPostModify() throws Exception {
        // given & when
        ObjectMapper objectMapper = new ObjectMapper();
        BlogPostDto postDto = createTestBlogPostCompanyDto();
        postDto.setName(" ");
        postDto.setLink(" ");
        postDto.setClassification(" ");
        postDto.setTargetName(" ");
        postDto.setTargetArticleNames(objectMapper.writeValueAsString(new HashMap<>() {{
            put(TARGET_ARTICLE_NAME, List.of(" "));
        }}));
        postDto.setTargetArticleLinks(objectMapper.writeValueAsString(new HashMap<>() {{
            put(TARGET_ARTICLE_LINK, List.of(" "));
        }}));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(modifyBlogPostFinishUrl, postDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(CLASSIFICATION)))
                .isEqualTo(messageSource.getMessage("NotBlank.enum.classification", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetName", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_ARTICLE_NAMES)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetArticleName", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_ARTICLE_LINKS)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetArticleLink", null, Locale.getDefault()));
    }

    @DisplayName("NotBlank(null)에 대한 블로그 포스트 변경 유효성 검증")
    @Test
    void validateNotBlankNullBlogPostModify() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(modifyBlogPostFinishUrl,
                        new HashMap<>() {{
                            put(YEAR, postDto.getYear().toString());
                            put(MONTH, postDto.getMonth().toString());
                            put(DAYS, postDto.getDays().toString());
                            put(TARGET_ARTICLE_NAMES, postDto.getTargetArticleNames());
                            put(TARGET_ARTICLE_LINKS, postDto.getTargetArticleLinks());
                        }}))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(CLASSIFICATION)))
                .isEqualTo(messageSource.getMessage("NotBlank.enum.classification", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_NAME)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetName", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_IMAGE_PATH)))
                .isEqualTo(messageSource.getMessage("NotBlank.post.targetImagePath", null, Locale.getDefault()));
    }

    @DisplayName("NotNull에 대한 블로그 포스트 변경 유효성 검증")
    @Test
    void validateNotNullBlogPostModify() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMultipleParams(modifyBlogPostFinishUrl,
                        new HashMap<>() {{
                            put(NAME, postDto.getName());
                            put(LINK, postDto.getLink());
                            put(CLASSIFICATION, postDto.getClassification());
                            put(TARGET_NAME, postDto.getTargetName());
                            put(TARGET_IMAGE_PATH, postDto.getTargetImagePath());
                            put(TARGET_ARTICLE_NAMES, postDto.getTargetArticleNames());
                            put(TARGET_ARTICLE_LINKS, postDto.getTargetArticleLinks());
                        }}))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(YEAR)))
                .isEqualTo(messageSource.getMessage("NotNull.post.year", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(MONTH)))
                .isEqualTo(messageSource.getMessage("NotNull.post.month", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(DAYS)))
                .isEqualTo(messageSource.getMessage("NotNull.post.days", null, Locale.getDefault()));
    }

    @DisplayName("Pattern에 대한 블로그 포스트 변경 유효성 검증")
    @Test
    void validatePatternBlogPostModify() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();
        postDto.setLink(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(modifyBlogPostFinishUrl, postDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Pattern.post.link", null, Locale.getDefault()));
    }

    @DisplayName("Range에 대한 블로그 포스트 변경 유효성 검증")
    @Test
    void validateRangeBlogPostModify() throws Exception {
        // given & when
        BlogPostDto postDtoFallShortOf = createTestBlogPostCompanyDto();
        postDtoFallShortOf.setYear(1959);
        postDtoFallShortOf.setMonth(0);
        postDtoFallShortOf.setDays(0);

        BlogPostDto postDtoExceed = createTestBlogPostCompanyDto();
        postDtoExceed.setYear(2100);
        postDtoExceed.setMonth(13);
        postDtoExceed.setDays(32);

        // then
        for (BlogPostDto postDto : List.of(postDtoFallShortOf, postDtoExceed)) {
            Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(modifyBlogPostFinishUrl, postDto))
                    .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

            @SuppressWarnings("unchecked")
            Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
            assertThat(decodeWithUTF8((fieldErrorMap).get(YEAR)))
                    .isEqualTo(messageSource.getMessage("Range.post.year", new Object[]{"연", 2099, 1960}, Locale.getDefault()));
            assertThat(decodeWithUTF8((fieldErrorMap).get(MONTH)))
                    .isEqualTo(messageSource.getMessage("Range.post.month", new Object[]{"월", 12, 1}, Locale.getDefault()));
            assertThat(decodeWithUTF8((fieldErrorMap).get(DAYS)))
                    .isEqualTo(messageSource.getMessage("Range.post.days", new Object[]{"일", 31, 1}, Locale.getDefault()));
        }
    }

    @DisplayName("Size에 대한 블로그 포스트 변경 유효성 검증")
    @Test
    void validateSizeBlogPostModify() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();
        postDto.setName(getRandomLongString(81));
        postDto.setLink(createTestBlogPostCompanyDto().getLink() + getRandomLongString(401));
        postDto.setTargetName(getRandomLongString(81));
        postDto.setTargetImagePath(getRandomLongString(81));

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(modifyBlogPostFinishUrl, postDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("Size.post.name", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Size.post.link", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_NAME)))
                .isEqualTo(messageSource.getMessage("Size.post.targetName", null, Locale.getDefault()));
        assertThat(decodeWithUTF8((fieldErrorMap).get(TARGET_IMAGE_PATH)))
                .isEqualTo(messageSource.getMessage("Size.post.targetImagePath", null, Locale.getDefault()));
    }

    @DisplayName("Classification의 typeMismatch에 대한 블로그 포스트 변경 유효성 검증")
    @Test
    void validateClassificationTypeMismatchBlogPostModify() throws Exception {
        // given & when
        BlogPostDto postDto = createTestBlogPostCompanyDto();
        postDto.setClassification(INVALID_VALUE);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(modifyBlogPostFinishUrl, postDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(true);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(CLASSIFICATION)))
                .isEqualTo(messageSource.getMessage("typeMismatch.enum.classification", null, Locale.getDefault()));
    }
}