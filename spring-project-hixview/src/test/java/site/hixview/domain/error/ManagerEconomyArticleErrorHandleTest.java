package site.hixview.domain.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.EconomyArticleBufferSimple;
import site.hixview.domain.service.EconomyArticleService;
import site.hixview.domain.validation.validator.EconomyArticleAddSimpleValidator;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.EconomyArticleTestUtils;

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
import static site.hixview.domain.vo.manager.ViewName.REMOVE_ECONOMY_ARTICLE_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_ECONOMY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_COUNTRY;
import static site.hixview.domain.vo.name.EntityName.Article.TARGET_ECONOMY_CONTENT;
import static site.hixview.domain.vo.name.ExceptionName.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_BEFORE_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;

@OnlyRealControllerContext
class ManagerEconomyArticleErrorHandleTest implements EconomyArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EconomyArticleService economyArticleService;

    @Autowired
    private EconomyArticleAddSimpleValidator economyArticleAddSimpleValidator;

    @DisplayName("존재하지 않는 대상 국가를 사용하는, 문자열을 사용하는 경제 기사들 추가")
    @Test
    void notFoundSubjectFirstCategoryEconomyArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_ECONOMY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateEconomyArticleBuffer.getNameDatePressString());
                    put(linkString, testEqualDateEconomyArticleBuffer.getLinkString());
                    put(SUBJECT_COUNTRY, INVALID_VALUE);
                    put(TARGET_ECONOMY_CONTENT, testEqualDateEconomyArticleBuffer.getTargetEconomyContents());
                }}))
                .andExpectAll(view().name(addStringEconomyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_SUBJECT_COUNTRY_ERROR)));
    }

    @DisplayName("기사 리스트의 크기가 링크 리스트의 크기보다 큰, 문자열을 사용하는 경제 기사들 추가")
    @Test
    void articleListBiggerEconomyArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_ECONOMY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEconomyArticleBuffer.getNameDatePressString());
                    put(linkString, testEqualDateEconomyArticle.getLink());
                    put(SUBJECT_COUNTRY, testEconomyArticleBuffer.getSubjectCountry());
                    put(TARGET_ECONOMY_CONTENT, testEconomyArticleBuffer.getTargetEconomyContents());
                }}))
                .andExpectAll(view().name(addStringEconomyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("링크 리스트의 크기가 기사 리스트의 크기보다 큰, 문자열을 사용하는 경제 기사들 추가")
    @Test
    void linkListBiggerEconomyArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_ECONOMY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, EconomyArticleBufferSimple.builder().article(testNewEconomyArticle).build().getNameDatePressString());
                    put(linkString, testEconomyArticleBuffer.getLinkString());
                    put(SUBJECT_COUNTRY, testEconomyArticleBuffer.getSubjectCountry());
                    put(TARGET_ECONOMY_CONTENT, testEconomyArticleBuffer.getTargetEconomyContents());
                }}))
                .andExpectAll(view().name(addStringEconomyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("비어 있는 기사를 사용하는, 문자열을 사용하는 경제 기사들 추가")
    @Test
    void emptyEconomyArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_ECONOMY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, "");
                    put(linkString, "");
                    put(SUBJECT_COUNTRY, testEconomyArticleBuffer.getSubjectCountry());
                    put(TARGET_ECONOMY_CONTENT, testEconomyArticleBuffer.getTargetEconomyContents());
                }}))
                .andExpectAll(view().name(addStringEconomyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_BLANK_ARTICLE_ERROR)));
    }

    @DisplayName("서식이 올바르지 않은 입력일 값을 포함하는, 문자열을 사용하는 경제 기사들 추가")
    @Test
    void dateFormatEconomyArticleAddWithString() throws Exception {
        // given & when
        EconomyArticle passedArticle = testEqualDateEconomyArticle;
        when(economyArticleService.registerArticle(any())).thenReturn(passedArticle);
        doNothing().when(economyArticleAddSimpleValidator).validate(any(), any());

        EconomyArticleBufferSimple invalidFormatArticleBuffer = EconomyArticleBufferSimple.builder()
                .nameDatePressString(testEconomyArticleBuffer.getNameDatePressString().
                        replace(createTestEconomyArticleDto().getDays().toString(), INVALID_VALUE))
                .linkString(testEconomyArticleBuffer.getLinkString())
                .importance(testEconomyArticleBuffer.getImportance())
                .subjectCountry(testEconomyArticleBuffer.getSubjectCountry())
                .targetEconomyContents(testEconomyArticleBuffer.getTargetEconomyContents()).build();

        EconomyArticleBufferSimple invalidFormatArticleAddNameDatePress = EconomyArticleBufferSimple.builder()
                .articleBuffer(EconomyArticleBufferSimple.builder().article(passedArticle).build())
                .articleBuffer(invalidFormatArticleBuffer).build();

        // then
        for (EconomyArticleBufferSimple articleBuffer : List.of(invalidFormatArticleBuffer, invalidFormatArticleAddNameDatePress)) {
            requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_ECONOMY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                        put(nameDatePressString, articleBuffer.getNameDatePressString());
                        put(linkString, articleBuffer.getLinkString());
                        put(SUBJECT_COUNTRY, articleBuffer.getSubjectCountry());
                        put(TARGET_ECONOMY_CONTENT, articleBuffer.getTargetEconomyContents());
                    }}))
                    .andExpectAll(view().name(
                                    REDIRECT_URL + ADD_ECONOMY_ARTICLE_WITH_STRING_URL + FINISH_URL),
                            model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                            model().attribute(ERROR_SINGLE, NUMBER_FORMAT_LOCAL_DATE_ERROR)));
        }
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하여 경제 기사를 검색하는, 경제 기사 변경")
    @Test
    void notFoundNumberOrNameEconomyArticleModify() throws Exception {
        // given & when
        when(economyArticleService.findArticleByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_ECONOMY_ARTICLE_URL, "numberOrName", ""))
                .andExpectAll(view().name(UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ECONOMY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_ECONOMY_ARTICLE_URL, "numberOrName", "1"))
                .andExpectAll(view().name(UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ECONOMY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_ECONOMY_ARTICLE_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ECONOMY_ARTICLE_ERROR)));
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하는, 경제 기사 없애기")
    @Test
    void notFoundArticleNumberOrNameEconomyArticleRid() throws Exception {
        // given & when
        when(economyArticleService.findArticleByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_ECONOMY_ARTICLE_URL, "numberOrName", ""))
                .andExpectAll(view().name(REMOVE_ECONOMY_ARTICLE_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ECONOMY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_ECONOMY_ARTICLE_URL, "numberOrName", "1"))
                .andExpectAll(view().name(REMOVE_ECONOMY_ARTICLE_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ECONOMY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_ECONOMY_ARTICLE_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_ECONOMY_ARTICLE_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ECONOMY_ARTICLE_ERROR)));
    }
}
