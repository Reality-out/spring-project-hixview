package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.BlogPostAddValidator;
import site.hixview.domain.validation.validator.BlogPostModifyValidator;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.BlogPostTestUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestPath.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.domain.vo.name.ViewName.*;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

@OnlyRealControllerContext
class ManagerBlogPostControllerTest implements BlogPostTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private BlogPostAddValidator blogPostAddValidator;

    @Autowired
    private BlogPostModifyValidator blogPostModifyValidator;

    @DisplayName("블로그 포스트 추가 페이지 접속")
    @Test
    void accessBlogPostAdd() throws Exception {
        mockMvc.perform(get(ADD_BLOG_POST_PATH))
                .andExpectAll(status().isOk(),
                        view().name(addBlogPostProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attributeExists(POST));
    }

    @DisplayName("블로그 포스트 추가 완료 페이지 접속")
    @Test
    void accessBlogPostAddFinish() throws Exception {
        // given & when
        BlogPost post = testBlogPostCompany;
        String name = post.getName();
        String redirectUrl = ADD_BLOG_POST_PATH + FINISH_PATH;
        when(blogPostService.findPostByName(name)).thenReturn(Optional.of(post));
        when(blogPostService.registerPost(argThat(Objects::nonNull))).thenReturn(post);
        doNothing().when(blogPostAddValidator).validate(any(), any());

        BlogPostDto postDto = post.toDto();

        // then
        mockMvc.perform(postWithBlogPostDto(ADD_BLOG_POST_PATH, postDto))
                .andExpectAll(status().isSeeOther(),
                        header().string(HttpHeaders.LOCATION, redirectUrl),
                        jsonPath(NAME).value(encodeWithUTF8(name)),
                        jsonPath(REDIRECT_PATH).value(redirectUrl));

        mockMvc.perform(getWithNoParam(fromPath(redirectUrl).queryParam(NAME, encodeWithUTF8(name))
                .build().toUriString()))
                .andExpectAll(status().isOk(),
                        view().name(ADD_BLOG_POST_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, ADD_BLOG_POST_PATH),
                        model().attribute(VALUE, name));

        assertThat(blogPostService.findPostByName(name).orElseThrow().toDto())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(postDto);
    }

    @DisplayName("블로그 포스트들 조회 페이지 접속")
    @Test
    void accessBlogPostsInquiry() throws Exception {
        // given & when
        List<BlogPost> storedList = List.of(testBlogPostCompany, testBlogPostEconomy);
        when(blogPostService.findPosts()).thenReturn(storedList);
        when(blogPostService.registerPosts(testBlogPostCompany, testBlogPostEconomy)).thenReturn(storedList);

        List<BlogPost> postList = blogPostService.registerPosts(testBlogPostCompany, testBlogPostEconomy);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_BLOG_POST_PATH))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "blog-posts-page"))
                .andReturn().getModelAndView()).getModelMap().get(BLOG_POSTS))
                .usingRecursiveComparison()
                .isEqualTo(postList);
    }

    @DisplayName("포스트의 유효한 타겟 이미지 경로 확인 페이지 접속")
    @Test
    void accessPostValidTargetImagePathCheck() throws Exception {
        // given
        List<BlogPost> storedList = List.of(testBlogPostCompany, testBlogPostEconomy);
        when(blogPostService.findPosts()).thenReturn(storedList);
        when(blogPostService.registerPosts(testBlogPostCompany, testBlogPostEconomy)).thenReturn(storedList);

        // when
        List<BlogPost> postList = blogPostService.registerPosts(testBlogPostCompany, testBlogPostEconomy);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(CHECK_TARGET_IMAGE_PATH_BLOG_POST_PATH))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_CHECK_IMAGE_PATH_VIEW + "blog-posts-page"))
                .andReturn().getModelAndView()).getModelMap().get(BLOG_POSTS))
                .usingRecursiveComparison()
                .isEqualTo(emptyList());
    }

    @DisplayName("포스트의 유효하지 않은 타겟 이미지 경로 확인 페이지 접속")
    @Test
    void accessPostInvalidTargetImagePathCheck() throws Exception {
        // given
        BlogPost testBlogPost1 = BlogPost.builder().blogPost(testBlogPostCompany).targetImagePath(INVALID_VALUE + "0").build();
        BlogPost testBlogPost2 = BlogPost.builder().blogPost(testBlogPostEconomy).targetImagePath(INVALID_VALUE + "1").build();
        List<BlogPost> storedList = List.of(testBlogPost1, testBlogPost2);
        when(blogPostService.findPosts()).thenReturn(storedList);
        when(blogPostService.registerPosts(testBlogPost1, testBlogPost2)).thenReturn(storedList);

        // when
        List<BlogPost> postList = blogPostService.registerPosts(testBlogPost1, testBlogPost2);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(CHECK_TARGET_IMAGE_PATH_BLOG_POST_PATH))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_CHECK_IMAGE_PATH_VIEW + "blog-posts-page"))
                .andReturn().getModelAndView()).getModelMap().get(BLOG_POSTS))
                .usingRecursiveComparison()
                .isEqualTo(storedList);
    }

    @DisplayName("블로그 포스트 변경 페이지 접속")
    @Test
    void accessBlogPostModify() throws Exception {
        mockMvc.perform(get(UPDATE_BLOG_POST_PATH))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_BLOG_POST_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT));
    }

    @DisplayName("블로그 포스트 변경 페이지 검색")
    @Test
    void searchBlogPostModify() throws Exception {
        // given & when
        BlogPost post = testBlogPostCompany;
        when(blogPostService.findPostByNumberOrName(String.valueOf(post.getNumber()))).thenReturn(Optional.of(post));
        when(blogPostService.findPostByNumberOrName(post.getName())).thenReturn(Optional.of(post));
        when(blogPostService.registerPost(post)).thenReturn(post);

        Long number = blogPostService.registerPost(post).getNumber();

        // then
        for (String str : List.of(String.valueOf(number), post.getName())) {
            assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                            UPDATE_BLOG_POST_PATH, NUMBER_OR_NAME, str))
                    .andExpectAll(status().isOk(),
                            view().name(modifyBlogPostProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute(UPDATE_PATH, modifyBlogPostFinishUrl))
                    .andReturn().getModelAndView()).getModelMap().get(POST))
                    .usingRecursiveComparison()
                    .isEqualTo(post.toDto());
        }
    }

    @DisplayName("블로그 포스트 변경 완료 페이지 접속")
    @Test
    void accessBlogPostModifyFinish() throws Exception {
        // given
        BlogPost post = BlogPost.builder().blogPost(testBlogPostEconomy).name(testBlogPostCompany.getName()).build();
        String name = post.getName();
        String redirectUrl = UPDATE_BLOG_POST_PATH + FINISH_PATH;
        when(blogPostService.findPostByName(name)).thenReturn(Optional.of(post));
        when(blogPostService.registerPost(testBlogPostCompany)).thenReturn(post);
        doNothing().when(blogPostService).correctPost(post);

        String commonName = testBlogPostCompany.getName();

        // when
        blogPostService.registerPost(testBlogPostCompany);

        // then
        mockMvc.perform(postWithBlogPostDto(modifyBlogPostFinishUrl, post.toDto()))
                .andExpectAll(status().isSeeOther(),
                        header().string(HttpHeaders.LOCATION, redirectUrl),
                        jsonPath(NAME).value(encodeWithUTF8(name)),
                        jsonPath(REDIRECT_PATH).value(redirectUrl));

        mockMvc.perform(getWithNoParam(fromPath(redirectUrl).queryParam(NAME, encodeWithUTF8(name)).build().toUriString()))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_BLOG_POST_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, UPDATE_BLOG_POST_PATH),
                        model().attribute(VALUE, commonName));

        assertThat(blogPostService.findPostByName(commonName).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(post);
    }

    @DisplayName("블로그 포스트 없애기 페이지 접속")
    @Test
    void accessBlogPostRid() throws Exception {
        mockMvc.perform(get(REMOVE_BLOG_POST_PATH))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_BLOG_POST_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("블로그 포스트 없애기 완료 페이지 접속")
    @Test
    void accessBlogPostRidFinish() throws Exception {
        // given & when
        BlogPost post = BlogPost.builder().blogPost(testBlogPostCompany).number(1L).build();
        when(blogPostService.findPosts()).thenReturn(emptyList());
        when(blogPostService.findPostByNumber(post.getNumber())).thenReturn(Optional.of(post));
        when(blogPostService.findPostByNumberOrName(String.valueOf(post.getNumber()))).thenReturn(Optional.of(post));
        when(blogPostService.findPostByNumberOrName(post.getName())).thenReturn(Optional.of(post));
        when(blogPostService.registerPost(argThat(Objects::nonNull))).thenReturn(post);
        doNothing().when(blogPostService).removePostByName(post.getName());

        Long number = blogPostService.registerPost(post).getNumber();
        String name = post.getName();
        System.out.println(String.valueOf(number) + ' ' + name);
        String redirectUrl = fromPath(REMOVE_BLOG_POST_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(name)).build().toUriString();

        // then
        for (String str : List.of(String.valueOf(number), name)) {
            mockMvc.perform(postWithSingleParam(REMOVE_BLOG_POST_PATH, NUMBER_OR_NAME, str))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectUrl));

            blogPostService.registerPost(post);
        }

        mockMvc.perform(getWithNoParam(redirectUrl))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_BLOG_POST_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, REMOVE_BLOG_POST_PATH),
                        model().attribute(VALUE, name));

        assertThat(blogPostService.findPosts()).isEmpty();
    }
}