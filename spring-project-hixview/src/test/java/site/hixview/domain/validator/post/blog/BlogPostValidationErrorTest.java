package site.hixview.domain.validator.post.blog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;
import site.hixview.support.context.RealControllerAndValidatorContext;
import site.hixview.support.util.BlogPostTestUtils;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestPath.ADD_BLOG_POST_PATH;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;
import static site.hixview.util.ControllerUtils.decodeWithUTF8;

@RealControllerAndValidatorContext
class BlogPostValidationErrorTest implements BlogPostTestUtils {

    private static final Logger log = LoggerFactory.getLogger(BlogPostValidationErrorTest.class);
    @Autowired
    MessageSource messageSource;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogPostService blogPostService;

    @DisplayName("미래의 기사 입력일을 사용하는 블로그 포스트 추가 유효성 검증")
    @Test
    void futureDateBlogPostAdd() throws Exception {
        // given & when
        BlogPostDto postDtoFuture = createTestBlogPostCompanyDto();
        postDtoFuture.setYear(2099);
        postDtoFuture.setMonth(12);
        postDtoFuture.setDays(31);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(ADD_BLOG_POST_PATH, postDtoFuture))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(ADD_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);
    }

    @DisplayName("중복 포스트명을 사용하는 블로그 포스트 추가")
    @Test
    void duplicatedNameBlogPostAdd() throws Exception {
        // given
        BlogPost post = testBlogPostCompany;
        String duplicatedName = post.getName();
        when(blogPostService.findPostByName(duplicatedName)).thenReturn(Optional.of(post));
        when(blogPostService.findPostByLink(post.getLink())).thenReturn(Optional.empty());
        when(blogPostService.registerPost(post)).thenReturn(post);

        BlogPostDto postDtoDuplicatedName = createTestBlogPostEconomyDto();
        postDtoDuplicatedName.setName(duplicatedName);

        // when
        blogPostService.registerPost(post);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(
                        postWithBlogPostDto(ADD_BLOG_POST_PATH, postDtoDuplicatedName)).andExpectAll(
                        jsonPath(LAYOUT_PATH).value(ADD_PROCESS_LAYOUT),
                        jsonPath(IS_BEAN_VALIDATION_ERROR).value(false))
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(NAME)))
                .isEqualTo(messageSource.getMessage("Exist.post.name", null, Locale.getDefault()));
    }

    @DisplayName("중복 포스트 링크를 사용하는 블로그 포스트 추가")
    @Test
    void duplicatedLinkBlogPostAdd() throws Exception {
        // given
        BlogPost post = testBlogPostCompany;
        String duplicatedLink = post.getLink();
        when(blogPostService.findPostByName(post.getName())).thenReturn(Optional.empty());
        when(blogPostService.findPostByLink(duplicatedLink)).thenReturn(Optional.of(post));
        when(blogPostService.registerPost(post)).thenReturn(post);

        BlogPostDto postDtoDuplicatedLink = createTestBlogPostEconomyDto();
        postDtoDuplicatedLink.setLink(duplicatedLink);

        // when
        blogPostService.registerPost(post);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(
                        postWithBlogPostDto(ADD_BLOG_POST_PATH, postDtoDuplicatedLink)).andExpectAll(
                        jsonPath(LAYOUT_PATH).value(ADD_PROCESS_LAYOUT),
                        jsonPath(IS_BEAN_VALIDATION_ERROR).value(false)
                )
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        log.info(fieldErrorMap.toString());
        assertThat(decodeWithUTF8(fieldErrorMap.get(LINK)))
                .isEqualTo(messageSource.getMessage("Exist.post.link", null, Locale.getDefault()));
    }

    @DisplayName("미래의 기사 입력일을 사용하는 블로그 포스트 변경 유효성 검증")
    @Test
    void futureDateBlogPostModify() throws Exception {
        // given & when
        BlogPostDto postDtoFuture = createTestBlogPostCompanyDto();
        postDtoFuture.setYear(2099);
        postDtoFuture.setMonth(12);
        postDtoFuture.setDays(31);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPostDto(modifyBlogPostFinishUrl, postDtoFuture))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(UPDATE_PROCESS_LAYOUT);
        assertThat(jsonMap.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);
    }

    @DisplayName("포스트명까지 변경을 시도하는, 블로그 포스트 변경")
    @Test
    void changeNameBlogPostModify() throws Exception {
        // given
        when(blogPostService.findPostByName(testBlogPostEconomy.getName())).thenReturn(Optional.empty());
        when(blogPostService.findPostByLink(testBlogPostCompany.getLink())).thenReturn(Optional.of(testBlogPostCompany));
        when(blogPostService.registerPost(testBlogPostCompany)).thenReturn(testBlogPostCompany);

        // when
        BlogPost post = blogPostService.registerPost(testBlogPostCompany);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPost(modifyBlogPostFinishUrl,
                BlogPost.builder().blogPost(post).name(testBlogPostEconomy.getName()).build())).andExpectAll(
                jsonPath(LAYOUT_PATH).value(UPDATE_PROCESS_LAYOUT),
                jsonPath(IS_BEAN_VALIDATION_ERROR).value(false)).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        log.info(fieldErrorMap.toString());
        assertThat(decodeWithUTF8(fieldErrorMap.get(NAME)))
                .isEqualTo(messageSource.getMessage("NotFound.post.name", null, Locale.getDefault()));
    }

    @DisplayName("포스트 링크까지 변경을 시도하는, 블로그 포스트 변경")
    @Test
    void changeLinkBlogPostModify() throws Exception {
        // given
        when(blogPostService.findPostByName(testBlogPostCompany.getName())).thenReturn(Optional.of(testBlogPostCompany));
        when(blogPostService.findPostByLink(testBlogPostEconomy.getLink())).thenReturn(Optional.empty());
        when(blogPostService.registerPost(testBlogPostCompany)).thenReturn(testBlogPostCompany);

        // when
        BlogPost post = blogPostService.registerPost(testBlogPostCompany);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithBlogPost(modifyBlogPostFinishUrl,
                BlogPost.builder().blogPost(post).link(testBlogPostEconomy.getLink()).build())).andExpectAll(
                jsonPath(LAYOUT_PATH).value(UPDATE_PROCESS_LAYOUT),
                jsonPath(IS_BEAN_VALIDATION_ERROR).value(false)).andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        log.info(fieldErrorMap.toString());
        assertThat(decodeWithUTF8(fieldErrorMap.get(LINK)))
                .isEqualTo(messageSource.getMessage("NotFound.post.link", null, Locale.getDefault()));
    }
}
