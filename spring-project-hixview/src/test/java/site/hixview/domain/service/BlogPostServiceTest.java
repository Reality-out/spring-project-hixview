package site.hixview.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.repository.BlogPostRepository;
import site.hixview.support.context.OnlyRealServiceContext;
import site.hixview.support.util.BlogPostTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_BLOG_POST_NAME;
import static site.hixview.domain.vo.ExceptionMessage.NO_BLOG_POST_WITH_THAT_NAME;

@OnlyRealServiceContext
class BlogPostServiceTest implements BlogPostTestUtils {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @DisplayName("블로그 포스트들 동시 등록")
    @Test
    public void registerBlogPostsTest() {
        // given
        BlogPost firstPost = testBlogPostCompany;
        BlogPost secondPost = testBlogPostEconomy;
        when(blogPostRepository.getPosts()).thenReturn(List.of(firstPost, secondPost));
        when(blogPostRepository.getPostByName(any())).thenReturn(Optional.empty());
        when(blogPostRepository.savePost(firstPost)).thenReturn(1L);
        when(blogPostRepository.savePost(secondPost)).thenReturn(2L);

        // when
        blogPostService.registerPosts(firstPost, secondPost);

        // then
    }

    @DisplayName("블로그 포스트 등록")
    @Test
    public void registerBlogPostTest() {
        // given
        BlogPost post = BlogPost.builder().post(testBlogPostCompany).number(1L).build();
        when(blogPostRepository.getPosts()).thenReturn(List.of(post));
        when(blogPostRepository.getPostByName(post.getName())).thenReturn(Optional.empty());
        when(blogPostRepository.savePost(post)).thenReturn(1L);

        // when
        post = blogPostService.registerPost(post);

        // then
        assertThat(blogPostService.findPosts()).isEqualTo(List.of(post));
    }

    @DisplayName("블로그 포스트 중복 이름으로 등록")
    @Test
    public void registerDuplicatedBlogPostWithSameNameTest() {
        // given
        BlogPost firstRegisteredPost = testBlogPostCompany;
        when(blogPostRepository.getPostByName(firstRegisteredPost.getName()))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(firstRegisteredPost));
        when(blogPostRepository.savePost(firstRegisteredPost)).thenReturn(1L);

        // when
        AlreadyExistException e = assertThrows(AlreadyExistException.class,
                () -> blogPostService.registerPosts(firstRegisteredPost,
                        BlogPost.builder().post(firstRegisteredPost).name(testBlogPostCompany.getName()).build()));

        // then
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_BLOG_POST_NAME);
    }

    @DisplayName("블로그 포스트 존재하지 않는 이름으로 수정")
    @Test
    public void correctBlogPostWithFaultNameTest() {
        // given
        when(blogPostRepository.getPostByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> blogPostService.correctPost(testBlogPostCompany));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_BLOG_POST_WITH_THAT_NAME);
    }

    @DisplayName("블로그 포스트 제거")
    @Test
    public void removeBlogPostTest() {
        // given
        BlogPost post = testBlogPostCompany;
        when(blogPostRepository.getPosts()).thenReturn(Collections.emptyList());
        when(blogPostRepository.getPostByName(post.getName()))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(post));
        when(blogPostRepository.savePost(post)).thenReturn(1L);
        doNothing().when(blogPostRepository).deletePostByName(post.getName());

        blogPostService.registerPost(post);

        // when
        blogPostService.removePostByName(post.getName());

        // then
        assertThat(blogPostService.findPosts()).isEmpty();
    }

    @DisplayName("블로그 포스트 존재하지 않는 이름으로 제거")
    @Test
    public void removeBlogPostByFaultNameTest() {
        // given
        when(blogPostRepository.getPostByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> blogPostService.removePostByName(INVALID_VALUE));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_BLOG_POST_WITH_THAT_NAME);
    }
}