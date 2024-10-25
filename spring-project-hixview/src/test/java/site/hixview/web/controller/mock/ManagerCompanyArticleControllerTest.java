package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.CompanyArticleAddComplexValidator;
import site.hixview.domain.validation.validator.CompanyArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.CompanyArticleEntryDateValidator;
import site.hixview.domain.validation.validator.CompanyArticleModifyValidator;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.CompanyArticleTestUtils;
import site.hixview.support.util.CompanyTestUtils;

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
        mockMvc.perform(get(ADD_SINGLE_COMPANY_ARTICLE_PATH))
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
        String redirectPath = fromPath(ADD_SINGLE_COMPANY_ARTICLE_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(articleDto.getName())).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_PATH, articleDto))
                .andExpectAll(status().isFound(), redirectedUrl(redirectPath));

        mockMvc.perform(getWithNoParam(redirectPath))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_FINISH),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, ADD_SINGLE_COMPANY_ARTICLE_PATH),
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
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_COMPANY_ARTICLE_PATH))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "company-articles-page"))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLES))
                .usingRecursiveComparison()
                .isEqualTo(articleList);
    }

    @DisplayName("기업 기사 변경 페이지 접속")
    @Test
    void accessCompanyArticleModify() throws Exception {
        mockMvc.perform(get(UPDATE_COMPANY_ARTICLE_PATH))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT));
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
                            UPDATE_COMPANY_ARTICLE_PATH, NUMBER_OR_NAME, str))
                    .andExpectAll(status().isOk(),
                            view().name(modifyCompanyArticleProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute(UPDATE_PATH, modifyCompanyArticleFinishUrl))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(article.toDto());
        }
    }

    @DisplayName("기업 기사 변경 완료 페이지 접속")
    @Test
    void accessCompanyArticleModifyFinish() throws Exception {
        // given
        CompanyArticle beforeModifyArticle = testCompanyArticle;
        CompanyArticle article = CompanyArticle.builder().article(testNewCompanyArticle)
                .name(beforeModifyArticle.getName()).link(beforeModifyArticle.getLink()).build();
        when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(beforeModifyArticle)).thenReturn(article);
        when(companyService.findCompanyByName(article.getSubjectCompany())).thenReturn(Optional.of(samsungElectronics));
        doNothing().when(articleService).correctArticle(article);

        String redirectPath = fromPath(UPDATE_COMPANY_ARTICLE_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);
        articleService.registerArticle(beforeModifyArticle);
        String commonName = beforeModifyArticle.getName();
        CompanyArticleDto articleDto = article.toDto();

        // then
        mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(status().isFound(), redirectedUrl(redirectPath));

        mockMvc.perform(getWithNoParam(redirectPath))
                .andExpectAll(status().isOk(),
                        view().name(UPDATE_COMPANY_ARTICLE_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, UPDATE_COMPANY_ARTICLE_PATH),
                        model().attribute(VALUE, commonName));

        assertThat(articleService.findArticleByName(commonName).orElseThrow().toDto())
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기업 기사 없애기 페이지 접속")
    @Test
    void accessCompanyArticleRid() throws Exception {
        mockMvc.perform(get(REMOVE_COMPANY_ARTICLE_PATH))
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
        String redirectPath = fromPath(REMOVE_COMPANY_ARTICLE_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(name)).build().toUriString();

        // when
        Long number = articleService.registerArticle(article).getNumber();

        // then
        for (String str : List.of(String.valueOf(number), name)) {
            mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_PATH, NUMBER_OR_NAME, str))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectPath));

            articleService.registerArticle(article);
        }
        articleService.removeArticleByName(name);

        mockMvc.perform(getWithNoParam(redirectPath))
                .andExpectAll(status().isOk(),
                        view().name(REMOVE_COMPANY_URL_ARTICLE_VIEW + VIEW_FINISH),
                        model().attribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT),
                        model().attribute(REPEAT_PATH, REMOVE_COMPANY_ARTICLE_PATH),
                        model().attribute(VALUE, name));

        assertThat(articleService.findArticles()).isEmpty();
    }
}