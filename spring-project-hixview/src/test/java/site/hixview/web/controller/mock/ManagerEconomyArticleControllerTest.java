package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.service.EconomyArticleService;
import site.hixview.domain.validation.validator.EconomyArticleAddComplexValidator;
import site.hixview.domain.validation.validator.EconomyArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.EconomyArticleEntryDateValidator;
import site.hixview.domain.validation.validator.EconomyArticleModifyValidator;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.EconomyArticleTestUtils;

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
class ManagerEconomyArticleControllerTest implements EconomyArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EconomyArticleService articleService;

    @Autowired
    private EconomyArticleEntryDateValidator economyArticleEntryDateValidator;

    @Autowired
    private EconomyArticleAddComplexValidator economyArticleAddComplexValidator;

    @Autowired
    private EconomyArticleAddSimpleValidator economyArticleAddSimpleValidator;

    @Autowired
    private EconomyArticleModifyValidator economyArticleModifyValidator;

    @DisplayName("경제 기사 추가 페이지 접속")
    @Test
    void accessEconomyArticleAdd() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_ECONOMY_ARTICLE_PATH))
                .andExpectAll(status().isOk(),
                        view().name(addSingleEconomyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("경제 기사 추가 완료 페이지 접속")
    @Test
    void accessEconomyArticleAddFinish() throws Exception {
        // given & when
        EconomyArticle articleOneEconomyContent = testEconomyArticle;
        List<String> targetEconomyContents = articleOneEconomyContent.getTargetEconomyContents();
        String commonEconomyContent = targetEconomyContents.getFirst();
        EconomyArticle articleTwoEconomyContents = EconomyArticle.builder()
                .article(testEconomyArticle).targetEconomyContents(targetEconomyContents).build();
        String redirectPath = ADD_SINGLE_ECONOMY_ARTICLE_PATH + FINISH_PATH;
        doNothing().when(economyArticleAddSimpleValidator).validate(any(), any());

        // then
        for (EconomyArticle article : List.of(articleOneEconomyContent, articleTwoEconomyContents)) {
            String name = article.toDto().getName();
            when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
            when(articleService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
            doNothing().when(articleService).removeArticleByName(name);

            mockMvc.perform(postWithEconomyArticleDto(ADD_SINGLE_ECONOMY_ARTICLE_PATH, article.toDto()))
                    .andExpectAll(status().isSeeOther(),
                            jsonPath(NAME).value(encodeWithUTF8(name)),
                            jsonPath(REDIRECT_PATH).value(redirectPath));

            mockMvc.perform(getWithNoParam(fromPath(redirectPath).queryParam(NAME, encodeWithUTF8(name)).build().toUriString()))
                    .andExpectAll(status().isOk(),
                            view().name(ADD_ECONOMY_ARTICLE_VIEW + VIEW_SINGLE_FINISH),
                            model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                            model().attribute(REPEAT_PATH, ADD_SINGLE_ECONOMY_ARTICLE_PATH),
                            model().attribute(VALUE, name));

            assertThat(articleService.findArticleByName(name).orElseThrow()).isEqualTo(article);

            articleService.removeArticleByName(name);
        }
    }

    @DisplayName("경제 기사들 조회 페이지 접속")
    @Test
    void accessEconomyArticlesInquiry() throws Exception {
        // given & when
        List<EconomyArticle> storedList = List.of(testEconomyArticle, testNewEconomyArticle);
        when(articleService.findArticles()).thenReturn(storedList);
        when(articleService.registerArticles(testEconomyArticle, testNewEconomyArticle)).thenReturn(storedList);

        List<EconomyArticle> articleList = articleService.registerArticles(testEconomyArticle, testNewEconomyArticle);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_ECONOMY_ARTICLE_PATH))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "economy-articles-page"))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLES)).isEqualTo(articleList);
    }

    @DisplayName("경제 기사 변경 페이지 접속")
    @Test
    void accessEconomyArticleModify() throws Exception {
        mockMvc.perform(get(UPDATE_ECONOMY_ARTICLE_PATH))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT));
    }

    @DisplayName("경제 기사 변경 페이지 검색")
    @Test
    void searchEconomyArticleModify() throws Exception {
        // given
        EconomyArticle article = testEconomyArticle;
        when(articleService.findArticleByNumberOrName(String.valueOf(article.getNumber()))).thenReturn(Optional.of(article));
        when(articleService.findArticleByNumberOrName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(article)).thenReturn(article);

        // when
        article = articleService.registerArticle(article);

        // then
        for (String str : List.of(String.valueOf(article.getNumber()), article.getName())) {
            assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                            UPDATE_ECONOMY_ARTICLE_PATH, NUMBER_OR_NAME, str))
                    .andExpectAll(status().isOk(),
                            view().name(modifyEconomyArticleProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute(UPDATE_PATH, modifyEconomyArticleFinishUrl))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(article.toDto());
        }
    }

    @DisplayName("경제 기사 변경 완료 페이지 접속")
    @Test
    void accessEconomyArticleModifyFinish() throws Exception {
        // given & when
        EconomyArticle articleOneEconomyContent = testEconomyArticle;
        List<String> targetEconomyContents = articleOneEconomyContent.getTargetEconomyContents();
        String commonEconomyContent = targetEconomyContents.getFirst();
        EconomyArticle articleTwoEconomyContents = EconomyArticle.builder()
                .article(testEconomyArticle).targetEconomyContents(targetEconomyContents).build();
        String redirectPath = modifyEconomyArticleFinishUrl;

        // then
        for (EconomyArticle article : List.of(articleOneEconomyContent, articleTwoEconomyContents)) {
            when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
            when(articleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
            when(articleService.registerArticle(article)).thenReturn(article);
            doNothing().when(articleService).correctArticle(article);

            articleService.registerArticle(article);
            String commonName = article.getName();
            EconomyArticleDto articleDto = article.toDto();

            // then
            mockMvc.perform(postWithEconomyArticleDto(redirectPath, articleDto))
                    .andExpectAll(status().isSeeOther(),
                            jsonPath(NAME).value(encodeWithUTF8(articleDto.getName())),
                            jsonPath(REDIRECT_PATH).value(redirectPath));

            mockMvc.perform(getWithNoParam(fromPath(redirectPath).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString()))
                    .andExpectAll(status().isOk(),
                            view().name(UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_FINISH),
                            model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
                            model().attribute(REPEAT_PATH, UPDATE_ECONOMY_ARTICLE_PATH),
                            model().attribute(VALUE, commonName));

            assertThat(articleService.findArticleByName(commonName).orElseThrow()).isEqualTo(article);
        }
    }

    @DisplayName("경제 기사 없애기 페이지 접속")
    @Test
    void accessEconomyArticleRid() throws Exception {
        mockMvc.perform(get(REMOVE_ECONOMY_ARTICLE_PATH))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_ECONOMY_ARTICLE_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("경제 기사 없애기 완료 페이지 접속")
    @Test
    void accessEconomyArticleRidFinish() throws Exception {
        // given
        EconomyArticle article = EconomyArticle.builder().article(testEconomyArticle).number(1L).build();
        when(articleService.findArticles()).thenReturn(emptyList());
        when(articleService.findArticleByNumber(article.getNumber())).thenReturn(Optional.of(article));
        when(articleService.findArticleByNumberOrName(String.valueOf(article.getNumber()))).thenReturn(Optional.of(article));
        when(articleService.findArticleByNumberOrName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        doNothing().when(articleService).removeArticleByName(article.getName());

        String name = article.getName();
        String redirectPath = fromPath(REMOVE_ECONOMY_ARTICLE_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(name)).build().toUriString();

        // when
        Long number = articleService.registerArticle(article).getNumber();

        // then
        for (String str : List.of(String.valueOf(number), name)) {
            mockMvc.perform(postWithSingleParam(REMOVE_ECONOMY_ARTICLE_PATH, NUMBER_OR_NAME, str))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectPath));

            articleService.registerArticle(article);
        }
        articleService.removeArticleByName(name);

        mockMvc.perform(getWithNoParam(redirectPath))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_ECONOMY_ARTICLE_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, REMOVE_ECONOMY_ARTICLE_PATH),
                        model().attribute(VALUE, name));

        assertThat(articleService.findArticles()).isEmpty();
    }
}