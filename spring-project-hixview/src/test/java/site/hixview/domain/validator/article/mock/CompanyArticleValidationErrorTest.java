package site.hixview.domain.validator.article.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.support.context.RealControllerAndValidatorContext;
import site.hixview.support.util.CompanyArticleTestUtils;
import site.hixview.support.util.CompanyTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestPath.ADD_SINGLE_COMPANY_ARTICLE_PATH;

@RealControllerAndValidatorContext
class CompanyArticleValidationErrorTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyArticleService articleService;

    @Autowired
    private CompanyService companyService;

    private ResultActions expectAddProcessStatusViewLayoutPathError(ResultActions resultActions) throws Exception {
        return resultActions.andExpectAll(status().isOk(),
                view().name(addSingleCompanyArticleProcessPage),
                model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                model().attribute(ERROR, (String) null));
    }

    private ResultActions expectUpdateProcessStatusViewLayoutPathError(ResultActions resultActions) throws Exception {
        return resultActions.andExpectAll(status().isOk(),
                view().name(modifyCompanyArticleProcessPage),
                model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                model().attribute(ERROR, (String) null));
    }

    @DisplayName("미래의 기사 입력일을 사용하는 기업 기사 추가 유효성 검증")
    @Test
    public void futureDateCompanyArticleAdd() throws Exception {
        CompanyArticleDto articleDtoFuture = createTestCompanyArticleDto();
        articleDtoFuture.setYear(2099);
        articleDtoFuture.setMonth(12);
        articleDtoFuture.setDays(31);

        assertThat(requireNonNull(expectAddProcessStatusViewLayoutPathError(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_PATH, articleDtoFuture)))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDtoFuture);
    }

    @DisplayName("기사 입력일이 유효하지 않은 기업 기사 추가 유효성 검증")
    @Test
    public void invalidDateCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        assertThat(requireNonNull(expectAddProcessStatusViewLayoutPathError(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_PATH, articleDto)))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("중복 기사명 또는 기사 링크를 사용하는 기업 기사 추가")
    @Test
    public void duplicatedNameOrLinkCompanyArticleAdd() throws Exception {
        // given
        CompanyArticle article = testCompanyArticle;
        when(articleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
        when(articleService.registerArticle(article)).thenReturn(article);
        doNothing().when(companyService).registerCompany(samsungElectronics);

        CompanyArticleDto articleDtoDuplicatedName = createTestNewCompanyArticleDto();
        articleDtoDuplicatedName.setName(article.getName());
        CompanyArticleDto articleDtoDuplicatedLink = createTestNewCompanyArticleDto();
        articleDtoDuplicatedLink.setLink(article.getLink());

        // when
        articleService.registerArticle(article);
        companyService.registerCompany(samsungElectronics);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoDuplicatedName, articleDtoDuplicatedLink)) {
            assertThat(requireNonNull(expectAddProcessStatusViewLayoutPathError(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_PATH, articleDto)))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(articleDto);
        }
    }

    @DisplayName("대상 기업이 추가되지 않은 기업 기사 추가")
    @Test
    public void notRegisteredSubjectCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        when(articleService.findArticleByName(articleDto.getName())).thenReturn(Optional.empty());
        when(articleService.findArticleByLink(articleDto.getLink())).thenReturn(Optional.empty());
        when(companyService.findCompanyByName(articleDto.getSubjectCompany())).thenReturn(Optional.empty());

        // then
        assertThat(requireNonNull(expectAddProcessStatusViewLayoutPathError(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_PATH, articleDto)))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("미래의 기사 입력일을 사용하는 기업 기사 변경 유효성 검증")
    @Test
    public void futureDateCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDtoFuture = createTestCompanyArticleDto();
        articleDtoFuture.setYear(2099);
        articleDtoFuture.setMonth(12);
        articleDtoFuture.setDays(31);

        // then
        assertThat(requireNonNull(expectUpdateProcessStatusViewLayoutPathError(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDtoFuture)))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDtoFuture);
    }

    @DisplayName("기사 입력일이 유효하지 않은 기업 기사 변경")
    @Test
    public void invalidDateCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        assertThat(requireNonNull(expectUpdateProcessStatusViewLayoutPathError(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto)))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기사명 또는 기사 링크까지 변경을 시도하는, 기업 기사 변경")
    @Test
    public void changeNameOrLinkCompanyArticleModify() throws Exception {
        // given
        when(articleService.findArticleByName(testCompanyArticle.getName())).thenReturn(Optional.empty());
        when(articleService.findArticleByLink(testCompanyArticle.getLink())).thenReturn(Optional.empty());
        when(articleService.registerArticle(testCompanyArticle)).thenReturn(testCompanyArticle);
        CompanyArticle article = articleService.registerArticle(testCompanyArticle);

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(expectUpdateProcessStatusViewLayoutPathError(mockMvc.perform(postWithCompanyArticle(modifyCompanyArticleFinishUrl,
                CompanyArticle.builder().article(article).name(testNewCompanyArticle.getName()).build()))));

        requireNonNull(expectUpdateProcessStatusViewLayoutPathError(mockMvc.perform(postWithCompanyArticle(modifyCompanyArticleFinishUrl,
                CompanyArticle.builder().article(article).link(testNewCompanyArticle.getLink()).build()))));
    }

    @DisplayName("대상 기업이 추가되지 않은 기업 기사 변경")
    @Test
    public void notRegisteredSubjectCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        when(articleService.findArticleByName(articleDto.getName())).thenReturn(Optional.empty());
        when(articleService.findArticleByLink(articleDto.getLink())).thenReturn(Optional.empty());
        when(companyService.findCompanyByName(articleDto.getSubjectCompany())).thenReturn(Optional.empty());

        // then
        assertThat(requireNonNull(expectUpdateProcessStatusViewLayoutPathError(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto)))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
