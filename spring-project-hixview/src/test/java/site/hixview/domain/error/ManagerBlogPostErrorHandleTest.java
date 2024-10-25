package site.hixview.domain.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.service.BlogPostService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.BlogPostTestUtils;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.REMOVE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_QUERY_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.REMOVE_BLOG_POST_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_BLOG_POST_URL;
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

    @DisplayName("존재하지 않는 포스트 번호 또는 포스트명을 사용하여 블로그 포스트를 검색하는, 블로그 포스트 변경")
    @Test
    void notFoundNumberOrNameBlogPostModify() throws Exception {
        // given & when
        when(blogPostService.findPostByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_BLOG_POST_URL, NUMBER_OR_NAME, ""))
                .andExpectAll(view().name(UPDATE_BLOG_POST_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_BLOG_POST_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_BLOG_POST_URL, NUMBER_OR_NAME, "1"))
                .andExpectAll(view().name(UPDATE_BLOG_POST_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_BLOG_POST_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_BLOG_POST_URL, NUMBER_OR_NAME, INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_BLOG_POST_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_BLOG_POST_ERROR)));
    }

    @DisplayName("존재하지 않는 포스트 번호 또는 포스트명을 사용하는, 블로그 포스트 없애기")
    @Test
    void notFoundPostNumberOrNameCompanyPostRid() throws Exception {
        // given & when
        when(blogPostService.findPostByNumberOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_BLOG_POST_URL, NUMBER_OR_NAME, ""))
                .andExpectAll(view().name(REMOVE_BLOG_POST_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_BLOG_POST_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_BLOG_POST_URL, NUMBER_OR_NAME, "1"))
                .andExpectAll(view().name(REMOVE_BLOG_POST_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_BLOG_POST_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_BLOG_POST_URL, NUMBER_OR_NAME, INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_BLOG_POST_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_BLOG_POST_ERROR)));
    }
}
