package site.hixview.domain.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.ArticleMainTestUtils;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.REMOVE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_QUERY_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.REMOVE_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.manager.ViewName.REMOVE_ARTICLE_MAIN_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_ARTICLE_MAIN_VIEW;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_ARTICLE_MAIN_ERROR;
import static site.hixview.domain.vo.name.ViewName.VIEW_BEFORE_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;

@OnlyRealControllerContext
class ManagerArticleMainErrorHandleTest implements ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleMainService articleMainService;

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하여 기사 메인을 검색하는, 기사 메인 변경")
    @Test
    void notFoundNumberOrNameArticleMainModify() throws Exception {
        // given & when
        when(articleMainService.findArticleByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_ARTICLE_MAIN_URL, NUMBER_OR_NAME, ""))
                .andExpectAll(view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_ARTICLE_MAIN_URL, NUMBER_OR_NAME, "1"))
                .andExpectAll(view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_ARTICLE_MAIN_URL, NUMBER_OR_NAME, INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하는, 기사 메인 없애기")
    @Test
    void notFoundArticleNumberOrNameCompanyArticleRid() throws Exception {
        // given & when
        when(articleMainService.findArticleByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, NUMBER_OR_NAME, ""))
                .andExpectAll(view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, NUMBER_OR_NAME, "1"))
                .andExpectAll(view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, NUMBER_OR_NAME, INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));
    }
}
