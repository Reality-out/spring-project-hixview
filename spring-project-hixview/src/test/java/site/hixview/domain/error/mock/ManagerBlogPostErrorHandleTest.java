package site.hixview.domain.error.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import site.hixview.domain.service.BlogPostService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.BlogPostTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.REMOVE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_QUERY_LAYOUT;
import static site.hixview.domain.vo.manager.RequestPath.REMOVE_BLOG_POST_PATH;
import static site.hixview.domain.vo.manager.RequestPath.UPDATE_BLOG_POST_PATH;
import static site.hixview.domain.vo.manager.ViewName.REMOVE_BLOG_POST_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_BLOG_POST_VIEW;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_BLOG_POST_ERROR;
import static site.hixview.domain.vo.name.ViewName.VIEW_BEFORE_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;

@OnlyRealControllerContext
class ManagerBlogPostErrorHandleTest implements BlogPostTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogPostService blogPostService;

    private void expectUpdateQueryViewLayoutPathError(ResultActions resultActions) throws Exception {
        resultActions.andExpectAll(status().isOk(),
                view().name(UPDATE_BLOG_POST_VIEW + VIEW_BEFORE_PROCESS),
                model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                model().attribute(ERROR, NOT_FOUND_BLOG_POST_ERROR));
    }

    private void expectRemoveSearchViewLayoutPathError(ResultActions resultActions) throws Exception {
        resultActions.andExpectAll(status().isOk(),
                view().name(REMOVE_BLOG_POST_VIEW + VIEW_PROCESS),
                model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                model().attribute(ERROR, NOT_FOUND_BLOG_POST_ERROR));
    }

    @DisplayName("존재하지 않는 포스트 번호 또는 포스트명을 사용하여 블로그 포스트를 검색하는, 블로그 포스트 변경")
    @Test
    void notFoundNumberOrNameBlogPostModify() throws Exception {
        // given & when
        when(blogPostService.findPostByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        expectUpdateQueryViewLayoutPathError(mockMvc.perform(postWithSingleParam(UPDATE_BLOG_POST_PATH, NUMBER_OR_NAME, "")));
        expectUpdateQueryViewLayoutPathError(mockMvc.perform(postWithSingleParam(UPDATE_BLOG_POST_PATH, NUMBER_OR_NAME, "1")));
        expectUpdateQueryViewLayoutPathError(mockMvc.perform(postWithSingleParam(UPDATE_BLOG_POST_PATH, NUMBER_OR_NAME, INVALID_VALUE)));
    }

    @DisplayName("존재하지 않는 포스트 번호 또는 포스트명을 사용하는, 블로그 포스트 없애기")
    @Test
    void notFoundPostNumberOrNameCompanyPostRid() throws Exception {
        // given & when
        when(blogPostService.findPostByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        expectRemoveSearchViewLayoutPathError(mockMvc.perform(postWithSingleParam(REMOVE_BLOG_POST_PATH, NUMBER_OR_NAME, "")));
        expectRemoveSearchViewLayoutPathError(mockMvc.perform(postWithSingleParam(REMOVE_BLOG_POST_PATH, NUMBER_OR_NAME, "1")));
        expectRemoveSearchViewLayoutPathError(mockMvc.perform(postWithSingleParam(REMOVE_BLOG_POST_PATH, NUMBER_OR_NAME, INVALID_VALUE)));
    }
}
