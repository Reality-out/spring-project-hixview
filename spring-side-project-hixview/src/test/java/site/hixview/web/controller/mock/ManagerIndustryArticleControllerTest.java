package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.config.annotation.MockConcurrentConfig;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.IndustryArticleDto;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.domain.validation.validator.IndustryArticleAddComplexValidator;
import site.hixview.domain.validation.validator.IndustryArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.IndustryArticleEntryDateValidator;
import site.hixview.domain.validation.validator.IndustryArticleModifyValidator;
import site.hixview.util.test.IndustryArticleTestUtils;

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
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE;
import static site.hixview.domain.vo.name.EntityName.Article.NUMBER;
import static site.hixview.domain.vo.name.ViewName.*;

@MockConcurrentConfig
class ManagerIndustryArticleControllerTest implements IndustryArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndustryArticleService articleService;

    @Autowired
    private IndustryArticleEntryDateValidator industryArticleEntryDateValidator;

    @Autowired
    private IndustryArticleAddComplexValidator industryArticleAddComplexValidator;

    @Autowired
    private IndustryArticleAddSimpleValidator industryArticleAddSimpleValidator;

    @Autowired
    private IndustryArticleModifyValidator industryArticleModifyValidator;

    @DisplayName("산업 기사 추가 페이지 접속")
    @Test
    void accessIndustryArticleAdd() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_INDUSTRY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("산업 기사 추가 완료 페이지 접속")
    @Test
    void accessIndustryArticleAddFinish() throws Exception {
        // given & when
        IndustryArticle article = testIndustryArticle;
        when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        doNothing().when(industryArticleAddSimpleValidator).validate(any(), any());

        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        String redirectedURL = fromPath(ADD_SINGLE_INDUSTRY_ARTICLE_URL + FINISH_URL).queryParam(NAME, articleDto.getName()).build().toUriString();

        // then
        mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_FINISH),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute(VALUE, articleDto.getName()));

        assertThat(articleService.findArticleByName(articleDto.getName()).orElseThrow().toDto())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(articleDto);
    }

    @DisplayName("산업 기사들 조회 페이지 접속")
    @Test
    void accessIndustryArticlesInquiry() throws Exception {
        // given & when
        List<IndustryArticle> storedList = List.of(testIndustryArticle, testNewIndustryArticle);
        when(articleService.findArticles()).thenReturn(storedList);
        when(articleService.registerArticles(testIndustryArticle, testNewIndustryArticle)).thenReturn(storedList);

        List<IndustryArticle> articleList = articleService.registerArticles(testIndustryArticle, testNewIndustryArticle);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_INDUSTRY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "industry-articles-page"))
                .andReturn().getModelAndView()).getModelMap().get("articles"))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("산업 기사 변경 페이지 접속")
    @Test
    void accessIndustryArticleModify() throws Exception {
        mockMvc.perform(get(UPDATE_INDUSTRY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT));
    }

    @DisplayName("산업 기사 변경 페이지 검색")
    @Test
    void searchIndustryArticleModify() throws Exception {
        // given
        IndustryArticle article = testIndustryArticle;
        when(articleService.findArticleByNumberOrName(String.valueOf(article.getNumber()))).thenReturn(Optional.of(article));
        when(articleService.findArticleByNumberOrName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(article)).thenReturn(article);

        // when
        article = articleService.registerArticle(article);

        // then
        for (String str : List.of(String.valueOf(article.getNumber()), article.getName())) {
            assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                            UPDATE_INDUSTRY_ARTICLE_URL, "numberOrName", str))
                    .andExpectAll(status().isOk(),
                            view().name(modifyIndustryArticleProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute("updateUrl", modifyIndustryArticleFinishUrl))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(article.toDto());
        }
    }

    @DisplayName("산업 기사 변경 완료 페이지 접속")
    @Test
    void accessIndustryArticleModifyFinish() throws Exception {
        // given
        IndustryArticle article = IndustryArticle.builder().article(testNewIndustryArticle)
                .name(testIndustryArticle.getName()).link(testIndustryArticle.getLink()).build();
        when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(testIndustryArticle)).thenReturn(article);
        doNothing().when(articleService).correctArticle(article);

        String redirectedURL = fromPath(UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL).queryParam(NAME, article.getName()).build().toUriString();

        // when
        articleService.registerArticle(testIndustryArticle);
        String commonName = testIndustryArticle.getName();
        IndustryArticleDto articleDto = article.toDto();

        // then
        mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
                        model().attribute(VALUE, commonName));

        assertThat(articleService.findArticleByName(commonName).orElseThrow().toDto())
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("산업 기사 없애기 페이지 접속")
    @Test
    void accessIndustryArticleRid() throws Exception {
        mockMvc.perform(get(REMOVE_INDUSTRY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("산업 기사 없애기 완료 페이지 접속")
    @Test
    void accessIndustryArticleRidFinish() throws Exception {
        // given
        IndustryArticle article = IndustryArticle.builder().article(testIndustryArticle).number(1L).build();
        when(articleService.findArticles()).thenReturn(emptyList());
        when(articleService.findArticleByNumber(article.getNumber())).thenReturn(Optional.of(article));
        when(articleService.findArticleByNumberOrName(String.valueOf(article.getNumber()))).thenReturn(Optional.of(article));
        when(articleService.findArticleByNumberOrName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        doNothing().when(articleService).removeArticleByName(article.getName());

        String name = article.getName();
        String redirectedURL = fromPath(REMOVE_INDUSTRY_ARTICLE_URL + FINISH_URL).queryParam(NAME, name).build().toUriString();

        // when
        Long number = articleService.registerArticle(article).getNumber();

        // then
        for (String str : List.of(String.valueOf(number), name)) {
            mockMvc.perform(postWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL, "numberOrName", str))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

            articleService.registerArticle(article);
        }
        articleService.removeArticleByName(name);

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(VALUE, name));

        assertThat(articleService.findArticles()).isEmpty();
    }
}