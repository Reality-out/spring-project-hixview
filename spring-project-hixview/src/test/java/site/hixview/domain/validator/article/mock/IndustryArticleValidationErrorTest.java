package site.hixview.domain.validator.article.mock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.support.context.RealControllerAndValidatorContext;
import site.hixview.support.util.IndustryArticleTestUtils;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_INDUSTRY_ARTICLE_URL;
import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE;
import static site.hixview.domain.vo.name.EntityName.Article.LINK;
import static site.hixview.util.ControllerUtils.decodeWithUTF8;

@RealControllerAndValidatorContext
class IndustryArticleValidationErrorTest implements IndustryArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndustryArticleService articleService;

    @Autowired
    private MessageSource messageSource;

    private final String IS_BEAN_VALIDATION_ERROR = "isBeanValidationError";
    private final String FIELD_ERROR_MAP = "fieldErrorMap";

    @DisplayName("미래의 기사 입력일을 사용하는 산업 기사 추가 유효성 검증")
    @Test
    void futureDateIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDtoFuture = createTestIndustryArticleDto();
        articleDtoFuture.setYear(2099);
        articleDtoFuture.setMonth(12);
        articleDtoFuture.setDays(31);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDtoFuture))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);
    }

    @DisplayName("기사 입력일이 유효하지 않은 산업 기사 추가 유효성 검증")
    @Test
    void invalidDateIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(DAYS)))
                .isEqualTo(messageSource.getMessage("TypeButInvalid.java.lang.LocalDate", null, Locale.getDefault()));
    }

    @DisplayName("중복 기사명을 사용하는 산업 기사 추가")
    @Test
    void duplicatedNameIndustryArticleAdd() throws Exception {
        // given
        IndustryArticle article = testIndustryArticle;
        when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(article)).thenReturn(article);

        IndustryArticleDto articleDtoName = createTestNewIndustryArticleDto();
        articleDtoName.setName(article.getName());

        // when
        articleService.registerArticle(article);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDtoName))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("Exist.article.name", null, Locale.getDefault()));
    }

    @DisplayName("중복 기사 링크를 사용하는 산업 기사 추가")
    @Test
    void duplicatedLinkIndustryArticleAdd() throws Exception {
        // given
        IndustryArticle article = testIndustryArticle;
        when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(article)).thenReturn(article);

        IndustryArticleDto articleDtoLink = createTestNewIndustryArticleDto();
        articleDtoLink.setLink(article.getLink());

        // when
        articleService.registerArticle(article);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDtoLink))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(LINK)))
                .isEqualTo(messageSource.getMessage("Exist.article.link", null, Locale.getDefault()));
    }

    @DisplayName("미래의 기사 입력일을 사용하는 산업 기사 변경 유효성 검증")
    @Test
    void futureDateIndustryArticleModify() throws Exception {
        IndustryArticleDto articleDtoFuture = createTestIndustryArticleDto();
        articleDtoFuture.setYear(2099);
        articleDtoFuture.setMonth(12);
        articleDtoFuture.setDays(31);

        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDtoFuture))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDtoFuture);
    }

    @DisplayName("기사 입력일이 유효하지 않은 산업 기사 변경")
    @Test
    void invalidDateIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기사명 또는 기사 링크까지 변경을 시도하는, 산업 기사 변경")
    @Test
    void changeNameOrLinkIndustryArticleModify() throws Exception {
        // given & when
        when(articleService.findArticleByName(testIndustryArticle.getName())).thenReturn(Optional.empty());
        when(articleService.findArticleByLink(testIndustryArticle.getLink())).thenReturn(Optional.empty());
        when(articleService.registerArticle(testIndustryArticle)).thenReturn(testIndustryArticle);
        IndustryArticle article = articleService.registerArticle(testIndustryArticle);

        // then
        requireNonNull(mockMvc.perform(postWithIndustryArticle(modifyIndustryArticleFinishUrl,
                        IndustryArticle.builder().article(article).name(testNewIndustryArticle.getName()).build()))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null)));

        requireNonNull(mockMvc.perform(postWithIndustryArticle(modifyIndustryArticleFinishUrl,
                        IndustryArticle.builder().article(article).link(testNewIndustryArticle.getLink()).build()))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null)));
    }

    @DisplayName("대상 산업이 추가되지 않은 산업 기사 변경")
    @Test
    void notRegisteredSubjectIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
