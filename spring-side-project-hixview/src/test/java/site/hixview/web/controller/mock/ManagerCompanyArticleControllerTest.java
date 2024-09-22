package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.config.annotation.MockConcurrentWebMvcTest;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.CompanyArticleAddComplexValidator;
import site.hixview.domain.validation.validator.CompanyArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.CompanyArticleEntryDateValidator;
import site.hixview.domain.validation.validator.CompanyArticleModifyValidator;
import site.hixview.util.test.CompanyArticleTestUtils;
import site.hixview.util.test.CompanyTestUtils;

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

@MockConcurrentWebMvcTest
class ManagerCompanyArticleControllerTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyArticleService articleService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyArticleEntryDateValidator companyArticleEntryDateValidator;

    @Autowired
    private CompanyArticleAddComplexValidator companyArticleAddComplexValidator;
    
    @Autowired
    private CompanyArticleAddSimpleValidator companyArticleAddSimpleValidator;

    @Autowired
    private CompanyArticleModifyValidator companyArticleModifyValidator;

    @DisplayName("기업 기사 추가 페이지 접속")
    @Test
    void accessCompanyArticleAdd() throws Exception {
        mockMvc.perform(get(ADD_SINGLE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("기업 기사 추가 완료 페이지 접속")
    @Test
    void accessCompanyArticleAddFinish() throws Exception {
        // given
        CompanyArticle article = testCompanyArticle;
        when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        when(companyService.findCompanyByName(article.getSubjectCompany())).thenReturn(Optional.of(samsungElectronics));
        doNothing().when(companyArticleAddSimpleValidator).validate(any(), any());

        CompanyArticleDto articleDto = article.toDto();
        String redirectedURL = fromPath(ADD_SINGLE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, articleDto.getName()).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_FINISH),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute(VALUE, articleDto.getName()));

        assertThat(articleService.findArticleByName(articleDto.getName()).orElseThrow().toDto())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(articleDto);
    }

    @DisplayName("기업 기사들 조회 페이지 접속")
    @Test
    void accessCompanyArticlesInquiry() throws Exception {
        // given & when
        List<CompanyArticle> storedList = List.of(testCompanyArticle, testNewCompanyArticle);
        when(articleService.findArticles()).thenReturn(storedList);
        when(articleService.registerArticles(testCompanyArticle, testNewCompanyArticle)).thenReturn(storedList);

        List<CompanyArticle> articleList = articleService.registerArticles(testCompanyArticle, testNewCompanyArticle);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "company-articles-page"))
                .andReturn().getModelAndView()).getModelMap().get("articles"))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("기업 기사 변경 페이지 접속")
    @Test
    void accessCompanyArticleModify() throws Exception {
        mockMvc.perform(get(UPDATE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT));
    }

    @DisplayName("기업 기사 변경 페이지 검색")
    @Test
    void searchCompanyArticleModify() throws Exception {
        // given
        CompanyArticle article = testCompanyArticle;
        when(articleService.findArticleByNumberOrName(String.valueOf(article.getNumber()))).thenReturn(Optional.of(article));
        when(articleService.findArticleByNumberOrName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(article)).thenReturn(article);

        // when
        article = articleService.registerArticle(article);

        // then
        for (String str : List.of(String.valueOf(article.getNumber()), article.getName())) {
            assertThat(requireNonNull(mockMvc.perform(postWithSingleParam(
                            UPDATE_COMPANY_ARTICLE_URL, "numberOrName", str))
                    .andExpectAll(status().isOk(),
                            view().name(modifyCompanyArticleProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute("updateUrl", modifyCompanyArticleFinishUrl))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(article.toDto());
        }
    }

    @DisplayName("기업 기사 변경 완료 페이지 접속")
    @Test
    void accessCompanyArticleModifyFinish() throws Exception {
        // given
        CompanyArticle article = CompanyArticle.builder().article(testNewCompanyArticle)
                .name(testCompanyArticle.getName()).link(testCompanyArticle.getLink()).build();
        when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(testCompanyArticle)).thenReturn(article);
        when(companyService.findCompanyByName(article.getSubjectCompany())).thenReturn(Optional.of(samsungElectronics));
        doNothing().when(articleService).correctArticle(article);

        String redirectedURL = fromPath(UPDATE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, article.getName()).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);
        articleService.registerArticle(testCompanyArticle);
        String commonName = testCompanyArticle.getName();
        CompanyArticleDto articleDto = article.toDto();

        // then
        mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
                        model().attribute(VALUE, commonName));

        assertThat(articleService.findArticleByName(commonName).orElseThrow().toDto())
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기업 기사 없애기 페이지 접속")
    @Test
    void accessCompanyArticleRid() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_ARTICLE_URL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_ARTICLE_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT));
    }

    @DisplayName("기업 기사 없애기 완료 페이지 접속")
    @Test
    void accessCompanyArticleRidFinish() throws Exception {
        // given
        CompanyArticle article = CompanyArticle.builder().article(testCompanyArticle).number(1L).build();
        when(articleService.findArticles()).thenReturn(emptyList());
        when(articleService.findArticleByNumber(article.getNumber())).thenReturn(Optional.of(article));
        when(articleService.findArticleByNumberOrName(String.valueOf(article.getNumber()))).thenReturn(Optional.of(article));
        when(articleService.findArticleByNumberOrName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        doNothing().when(articleService).removeArticleByName(article.getName());

        String name = article.getName();
        String redirectedURL = fromPath(REMOVE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, name).build().toUriString();

        // when
        Long number = articleService.registerArticle(article).getNumber();

        // then
        for (String str : List.of(String.valueOf(number), name)) {
            mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, "numberOrName", str))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

            articleService.registerArticle(article);
        }
        articleService.removeArticleByName(name);

        mockMvc.perform(getWithNoParam(redirectedURL))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_ARTICLE_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(VALUE, name));

        assertThat(articleService.findArticles()).isEmpty();
    }
}