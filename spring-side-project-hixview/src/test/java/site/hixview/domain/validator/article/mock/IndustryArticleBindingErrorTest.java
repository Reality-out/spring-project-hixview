package site.hixview.domain.validator.article.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.domain.entity.article.IndustryArticleDto;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.support.util.IndustryArticleTestUtils;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_INDUSTRY_ARTICLE_URL;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.name.ExceptionName.BEAN_VALIDATION_ERROR;

@OnlyRealControllerContext
class IndustryArticleBindingErrorTest implements IndustryArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndustryArticleService articleService;

    @DisplayName("NotBlank(공백)에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateNotBlankSpaceIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(" ");
        articleDto.setPress(" ");
        articleDto.setLink(" ");
        IndustryArticleDto returnedArticleDto = copyIndustryArticleDto(articleDto);
        returnedArticleDto.setName("");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(returnedArticleDto);
    }

    @DisplayName("NotBlank(null)에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateNotBlankNullIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(null);
        articleDto.setPress(null);
        articleDto.setLink(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotNull에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateNotNullIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setYear(null);
        articleDto.setMonth(null);
        articleDto.setDays(null);
        articleDto.setImportance(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Pattern에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validatePatternIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setLink(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
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
            assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                    .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                            model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                            model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(articleDto);
        }
    }

    @DisplayName("Restrict에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateRestrictIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setImportance(3);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Size에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateSizeIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(getRandomLongString(81));
        articleDto.setLink(getRandomLongString(401));

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("typeMismatch에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateTypeMismatchIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();

        // then
        mockMvc.perform(post(ADD_SINGLE_INDUSTRY_ARTICLE_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(NAME, articleDto.getName())
                        .param(PRESS, articleDto.getPress())
                        .param(LINK, articleDto.getLink())
                        .param(YEAR, INVALID_VALUE)
                        .param(MONTH, INVALID_VALUE)
                        .param(DAYS, INVALID_VALUE)
                        .param(IMPORTANCE, INVALID_VALUE)
                        .param(SUBJECT_FIRST_CATEGORY, articleDto.getSubjectFirstCategory())
                        .param(SUBJECT_SECOND_CATEGORY, articleDto.getSubjectSecondCategory()))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("Enum 타입의 typeMismatch에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateEnumTypeMismatchIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articlePressMismatch = createTestIndustryArticleDto();
        articlePressMismatch.setPress(INVALID_VALUE);
        IndustryArticleDto articleFirstCategoryMismatch = createTestIndustryArticleDto();
        articleFirstCategoryMismatch.setSubjectFirstCategory(INVALID_VALUE);
        IndustryArticleDto articleSecondCategoryMismatch = createTestIndustryArticleDto();
        articleSecondCategoryMismatch.setSubjectSecondCategory(INVALID_VALUE);

        // then
        for (IndustryArticleDto articleDto : List.of(articlePressMismatch, articleFirstCategoryMismatch, articleSecondCategoryMismatch)) {
            mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                    .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                            model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                            model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                            model().attributeExists(ARTICLE));
        }
    }

    @DisplayName("NotBlank(공백)에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateNotBlankSpaceIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(" ");
        articleDto.setPress(" ");
        articleDto.setLink(" ");
        IndustryArticleDto returnedArticleDto = copyIndustryArticleDto(articleDto);
        returnedArticleDto.setName("");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(returnedArticleDto);
    }

    @DisplayName("NotBlank(null)에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateNotBlankNullIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(null);
        articleDto.setPress(null);
        articleDto.setLink(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotNull에 대한 산업 기사 추가 유효성 검증")
    @Test
    void validateNotNullIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setYear(null);
        articleDto.setMonth(null);
        articleDto.setDays(null);
        articleDto.setImportance(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Pattern에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validatePatternIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setLink(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Range에 대한 기업 변경 추가 유효성 검증")
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
            assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                    .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(articleDto);
        }
    }

    @DisplayName("Restrict에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateRestrictIndustryArticleModify() throws Exception {
        // given
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleService.registerArticle(testIndustryArticle);

        // when
        articleDto.setImportance(3);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Size에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateSizeIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setName(getRandomLongString(81));
        articleDto.setLink(getRandomLongString(401));

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("typeMismatch에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateTypeMismatchIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();

        // then
        mockMvc.perform(post(modifyIndustryArticleFinishUrl).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(NAME, articleDto.getName())
                        .param(PRESS, articleDto.getPress())
                        .param(LINK, articleDto.getLink())
                        .param(YEAR, INVALID_VALUE)
                        .param(MONTH, INVALID_VALUE)
                        .param(DAYS, INVALID_VALUE)
                        .param(IMPORTANCE, INVALID_VALUE)
                        .param(SUBJECT_FIRST_CATEGORY, articleDto.getSubjectFirstCategory())
                        .param(SUBJECT_SECOND_CATEGORY, articleDto.getSubjectSecondCategory()))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("Enum 타입의 typeMismatch에 대한 산업 기사 변경 유효성 검증")
    @Test
    void validateEnumTypeMismatchIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articlePressMismatch = createTestIndustryArticleDto();
        articlePressMismatch.setPress(INVALID_VALUE);
        IndustryArticleDto articleFirstCategoryMismatch = createTestIndustryArticleDto();
        articleFirstCategoryMismatch.setSubjectFirstCategory(INVALID_VALUE);
        IndustryArticleDto articleSecondCategoryMismatch = createTestIndustryArticleDto();
        articleSecondCategoryMismatch.setSubjectSecondCategory(INVALID_VALUE);

        // then
        for (IndustryArticleDto articleDto : List.of(articlePressMismatch, articleFirstCategoryMismatch, articleSecondCategoryMismatch)) {
            mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                    .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                            model().attributeExists(ARTICLE));
        }
    }
}
