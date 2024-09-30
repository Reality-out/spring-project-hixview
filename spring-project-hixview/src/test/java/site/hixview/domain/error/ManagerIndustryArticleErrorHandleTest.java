package site.hixview.domain.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.IndustryArticleBufferSimple;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.domain.validation.validator.IndustryArticleAddSimpleValidator;
import site.hixview.support.util.IndustryArticleTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.REMOVE_INDUSTRY_ARTICLE_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_INDUSTRY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_FIRST_CATEGORY;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_SECOND_CATEGORY;
import static site.hixview.domain.vo.name.ExceptionName.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_BEFORE_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;

@OnlyRealControllerContext
class ManagerIndustryArticleErrorHandleTest implements IndustryArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndustryArticleService industryArticleService;

    @Autowired
    private IndustryArticleAddSimpleValidator industryArticleAddSimpleValidator;

    @DisplayName("존재하지 않는 대상 1차 업종을 사용하는, 문자열을 사용하는 산업 기사들 추가")
    @Test
    void notFoundSubjectFirstCategoryIndustryArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateIndustryArticleBuffer.getNameDatePressString());
                    put(linkString, testEqualDateIndustryArticleBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, INVALID_VALUE);
                    put(SUBJECT_SECOND_CATEGORY, testEqualDateIndustryArticle.getSubjectSecondCategory().name());
                }}))
                .andExpectAll(view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_FIRST_CATEGORY_ERROR)));
    }

    @DisplayName("존재하지 않는 대상 2차 업종을 사용하는, 문자열을 사용하는 산업 기사들 추가")
    @Test
    void notFoundSubjectSecondCategoryIndustryArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateIndustryArticleBuffer.getNameDatePressString());
                    put(linkString, testEqualDateIndustryArticleBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, testEqualDateIndustryArticle.getSubjectFirstCategory().name());
                    put(SUBJECT_SECOND_CATEGORY, INVALID_VALUE);
                }}))
                .andExpectAll(view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_SECOND_CATEGORY_ERROR)));
    }

    @DisplayName("기사 리스트의 크기가 링크 리스트의 크기보다 큰, 문자열을 사용하는 산업 기사들 추가")
    @Test
    void articleListBiggerIndustryArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testIndustryArticleBuffer.getNameDatePressString());
                    put(linkString, testEqualDateIndustryArticle.getLink());
                    put(SUBJECT_FIRST_CATEGORY, testIndustryArticleBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, testIndustryArticleBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("링크 리스트의 크기가 기사 리스트의 크기보다 큰, 문자열을 사용하는 산업 기사들 추가")
    @Test
    void linkListBiggerIndustryArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, IndustryArticleBufferSimple.builder().article(testNewIndustryArticle).build().getNameDatePressString());
                    put(linkString, testIndustryArticleBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, testIndustryArticleBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, testIndustryArticleBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("비어 있는 기사를 사용하는, 문자열을 사용하는 산업 기사들 추가")
    @Test
    void emptyIndustryArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, "");
                    put(linkString, "");
                    put(SUBJECT_FIRST_CATEGORY, testIndustryArticleBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, testIndustryArticleBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_BLANK_ARTICLE_ERROR)));
    }

    @DisplayName("서식이 올바르지 않은 입력일 값을 포함하는, 문자열을 사용하는 산업 기사들 추가")
    @Test
    void dateFormatIndustryArticleAddWithString() throws Exception {
        // given & when
        IndustryArticle passedArticle = testEqualDateIndustryArticle;
        when(industryArticleService.registerArticle(any())).thenReturn(passedArticle);
        doNothing().when(industryArticleAddSimpleValidator).validate(any(), any());

        IndustryArticleBufferSimple invalidFormatArticleBuffer = IndustryArticleBufferSimple.builder()
                .nameDatePressString(testIndustryArticleBuffer.getNameDatePressString().
                        replace(createTestIndustryArticleDto().getDays().toString(), INVALID_VALUE))
                .linkString(testIndustryArticleBuffer.getLinkString())
                .importance(testIndustryArticleBuffer.getImportance())
                .subjectFirstCategory(testIndustryArticleBuffer.getSubjectFirstCategory())
                .subjectSecondCategory(testIndustryArticleBuffer.getSubjectSecondCategory()).build();

        IndustryArticleBufferSimple invalidFormatArticleAddNameDatePress = IndustryArticleBufferSimple.builder()
                .articleBuffer(IndustryArticleBufferSimple.builder().article(passedArticle).build())
                .articleBuffer(invalidFormatArticleBuffer).build();

        // then
        for (IndustryArticleBufferSimple articleBuffer : List.of(invalidFormatArticleBuffer, invalidFormatArticleAddNameDatePress)) {
            requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                        put(nameDatePressString, articleBuffer.getNameDatePressString());
                        put(linkString, articleBuffer.getLinkString());
                        put(SUBJECT_FIRST_CATEGORY, articleBuffer.getSubjectFirstCategory());
                        put(SUBJECT_SECOND_CATEGORY, articleBuffer.getSubjectSecondCategory());
                    }}))
                    .andExpectAll(view().name(
                                    REDIRECT_URL + ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + FINISH_URL),
                            model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                            model().attribute(ERROR_SINGLE, NUMBER_FORMAT_LOCAL_DATE_ERROR)));
        }
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하여 산업 기사를 검색하는, 산업 기사 변경")
    @Test
    void notFoundNumberOrNameIndustryArticleModify() throws Exception {
        // given & when
        when(industryArticleService.findArticleByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_INDUSTRY_ARTICLE_URL, "numberOrName", ""))
                .andExpectAll(view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_INDUSTRY_ARTICLE_URL, "numberOrName", "1"))
                .andExpectAll(view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_INDUSTRY_ARTICLE_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하는, 산업 기사 없애기")
    @Test
    void notFoundArticleNumberOrNameIndustryArticleRid() throws Exception {
        // given & when
        when(industryArticleService.findArticleByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL, "numberOrName", ""))
                .andExpectAll(view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL, "numberOrName", "1"))
                .andExpectAll(view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));
    }
}
