package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.home.ArticleMain;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.ArticleMainAddValidator;
import site.hixview.domain.validation.validator.ArticleMainModifyValidator;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.ArticleMainTestUtils;

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
class ManagerArticleMainControllerTest implements ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleMainService articleMainService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ArticleMainAddValidator articleMainAddValidator;

    @Autowired
    private ArticleMainModifyValidator articleMainModifyValidator;

    @DisplayName("기사 메인 추가 페이지 접속")
    @Test
    void accessArticleMainAdd() throws Exception {
        mockMvc.perform(get(ADD_ARTICLE_MAIN_PATH))
                .andExpectAll(status().isOk(),
                        view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(CLASSIFICATIONS, Classification.values()),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("기사 메인 추가 완료 페이지 접속")
    @Test
    void accessArticleMainAddFinish() throws Exception {
        // given & when
        ArticleMain article = testCompanyArticleMain;
        String name = article.getName();
        when(articleMainService.findArticleByName(name)).thenReturn(Optional.of(article));
        when(articleMainService.findArticleByImagePath(article.getImagePath())).thenReturn(Optional.empty());
        when(articleMainService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        doNothing().when(articleMainAddValidator).validate(any(), any());

        String redirectUrl = fromPath(ADD_ARTICLE_MAIN_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(name))
                .build().toUriString();

        // then
        mockMvc.perform(postWithArticleMain(ADD_ARTICLE_MAIN_PATH, article))
                .andExpectAll(status().isFound(), redirectedUrl(redirectUrl));

        mockMvc.perform(getWithNoParam(redirectUrl))
                .andExpectAll(status().isOk(),
                        view().name(ADD_ARTICLE_MAIN_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, ADD_ARTICLE_MAIN_PATH),
                        model().attribute(VALUE, name));

        assertThat(articleMainService.findArticleByName(name).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("기사 메인들 조회 페이지 접속")
    @Test
    void accessArticleMainsInquiry() throws Exception {
        // given & when
        List<ArticleMain> storedList = List.of(testCompanyArticleMain, testNewCompanyArticleMain);
        when(articleMainService.findArticles()).thenReturn(storedList);
        when(articleMainService.registerArticles(testCompanyArticleMain, testNewCompanyArticleMain)).thenReturn(storedList);

        List<ArticleMain> articleList = articleMainService.registerArticles(testCompanyArticleMain, testNewCompanyArticleMain);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_ARTICLE_MAIN_PATH))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "article-mains-page"))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE_MAINS))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("기사 메인의 유효한 이미지 경로 확인 페이지 접속")
    @Test
    void accessArticleMainValidImagePathCheck() throws Exception {
        // given
        List<ArticleMain> storedList = List.of(testCompanyArticleMain, testNewCompanyArticleMain);
        when(articleMainService.findArticles()).thenReturn(storedList);
        when(articleMainService.registerArticles(testCompanyArticleMain, testNewCompanyArticleMain)).thenReturn(storedList);

        // when
        List<ArticleMain> articleList = articleMainService.registerArticles(testCompanyArticleMain, testNewCompanyArticleMain);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(CHECK_IMAGE_PATH_ARTICLE_MAIN_PATH))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_CHECK_IMAGE_PATH_VIEW + "article-mains-page"))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE_MAINS))
                .usingRecursiveComparison()
                .isEqualTo(emptyList());
    }

    @DisplayName("기사 메인의 유효하지 않은 이미지 경로 확인 페이지 접속")
    @Test
    void accessArticleMainInvalidImagePathCheck() throws Exception {
        // given
        ArticleMain testArticleMain1 = ArticleMain.builder().article(testCompanyArticleMain).imagePath(INVALID_VALUE + "0").build();
        ArticleMain testArticleMain2 = ArticleMain.builder().article(testNewCompanyArticleMain).imagePath(INVALID_VALUE + "1").build();
        List<ArticleMain> storedList = List.of(testArticleMain1, testArticleMain2);
        when(articleMainService.findArticles()).thenReturn(storedList);
        when(articleMainService.registerArticles(testArticleMain1, testArticleMain2)).thenReturn(storedList);

        // when
        List<ArticleMain> articleList = articleMainService.registerArticles(testArticleMain1, testArticleMain2);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(CHECK_IMAGE_PATH_ARTICLE_MAIN_PATH))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_CHECK_IMAGE_PATH_VIEW + "article-mains-page"))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE_MAINS))
                .usingRecursiveComparison()
                .isEqualTo(storedList);
    }

    @DisplayName("기사 메인 변경 페이지 접속")
    @Test
    void accessArticleMainModify() throws Exception {
        mockMvc.perform(get(UPDATE_ARTICLE_MAIN_PATH))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT));
    }

    @DisplayName("기사 메인 변경 페이지 검색")
    @Test
    void searchArticleMainModify() throws Exception {
        // given & when
        ArticleMain article = testCompanyArticleMain;
        when(articleMainService.findArticleByNumberOrName(String.valueOf(article.getNumber()))).thenReturn(Optional.of(article));
        when(articleMainService.findArticleByNumberOrName(article.getName())).thenReturn(Optional.of(article));
        when(articleMainService.registerArticle(article)).thenReturn(article);

        Long number = articleMainService.registerArticle(article).getNumber();

        // then
        for (String str : List.of(String.valueOf(number), article.getName())) {
            assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                            UPDATE_ARTICLE_MAIN_PATH, NUMBER_OR_NAME, str))
                    .andExpectAll(status().isOk(),
                            view().name(modifyArticleMainProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute(UPDATE_PATH, modifyArticleMainFinishUrl),
                            model().attribute(CLASSIFICATIONS, Classification.values()))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(article.toDto());
        }
    }

    @DisplayName("기사 메인 변경 완료 페이지 접속")
    @Test
    void accessArticleMainModifyFinish() throws Exception {
        // given
        ArticleMain article = ArticleMain.builder().article(testNewCompanyArticleMain).name(testCompanyArticleMain.getName()).build();
        when(articleMainService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleMainService.registerArticle(testCompanyArticleMain)).thenReturn(article);
        doNothing().when(articleMainService).correctArticle(article);

        String commonName = testCompanyArticleMain.getName();
        String redirectUrl = fromPath(UPDATE_ARTICLE_MAIN_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString();

        // when
        articleMainService.registerArticle(testCompanyArticleMain);

        // then
        mockMvc.perform(postWithArticleMain(modifyArticleMainFinishUrl, article))
                .andExpectAll(status().isFound(), redirectedUrl(redirectUrl));

        mockMvc.perform(getWithNoParam(redirectUrl))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, UPDATE_ARTICLE_MAIN_PATH),
                        model().attribute(VALUE, commonName));

        assertThat(articleMainService.findArticleByName(commonName).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("기사 메인 없애기 페이지 접속")
    @Test
    void accessArticleMainRid() throws Exception {
        mockMvc.perform(get(REMOVE_ARTICLE_MAIN_PATH))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("기사 메인 없애기 완료 페이지 접속")
    @Test
    void accessArticleMainRidFinish() throws Exception {
        // given & when
        ArticleMain article = ArticleMain.builder().article(testCompanyArticleMain).number(1L).build();
        when(articleMainService.findArticles()).thenReturn(emptyList());
        when(articleMainService.findArticleByNumber(article.getNumber())).thenReturn(Optional.of(article));
        when(articleMainService.findArticleByNumberOrName(String.valueOf(article.getNumber()))).thenReturn(Optional.of(article));
        when(articleMainService.findArticleByNumberOrName(article.getName())).thenReturn(Optional.of(article));
        when(articleMainService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        doNothing().when(articleMainService).removeArticleByName(article.getName());

        Long number = articleMainService.registerArticle(article).getNumber();
        String name = article.getName();
        System.out.println(String.valueOf(number) + ' ' + name);
        String redirectUrl = fromPath(REMOVE_ARTICLE_MAIN_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(name)).build().toUriString();

        // then
        for (String str : List.of(String.valueOf(number), name)) {
            mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_PATH, NUMBER_OR_NAME, str))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectUrl));

            articleMainService.registerArticle(article);
        }

        mockMvc.perform(getWithNoParam(redirectUrl))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, REMOVE_ARTICLE_MAIN_PATH),
                        model().attribute(VALUE, name));

        assertThat(articleMainService.findArticles()).isEmpty();
    }
}