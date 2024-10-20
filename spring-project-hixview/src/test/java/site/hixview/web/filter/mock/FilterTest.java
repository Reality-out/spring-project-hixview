package site.hixview.web.filter.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.*;
import site.hixview.domain.entity.article.ArticleMain;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.dto.ArticleMainDto;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.entity.company.dto.CompanyDto;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.domain.validation.validator.ArticleMainAddValidator;
import site.hixview.domain.validation.validator.CompanyAddValidator;
import site.hixview.domain.validation.validator.CompanyArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.IndustryArticleAddSimpleValidator;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.ArticleMainTestUtils;
import site.hixview.support.util.CompanyArticleTestUtils;
import site.hixview.support.util.CompanyTestUtils;
import site.hixview.support.util.IndustryArticleTestUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.user.RequestUrl.*;
import static site.hixview.domain.vo.user.ViewName.*;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

@OnlyRealControllerContext
class FilterTest implements CompanyArticleTestUtils, IndustryArticleTestUtils, ArticleMainTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private IndustryArticleService industryArticleService;

    @Autowired
    private ArticleMainService articleMainService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyArticleAddSimpleValidator companyArticleAddSimpleValidator;

    @Autowired
    private IndustryArticleAddSimpleValidator industryArticleAddSimpleValidator;

    @Autowired
    private ArticleMainAddValidator articleMainAddValidator;

    @Autowired
    private CompanyAddValidator companyAddValidator;

    @DisplayName("URL 맨 끝 슬래시 제거 필터 테스트")
    @Test
    void handleUrlLastSlashFilterTest() throws Exception {
        mockMvc.perform(getWithNoParam(LOGIN_URL + "/"))
                .andExpectAll(status().isOk(), view().name(LOGIN_VIEW + VIEW_SHOW));
        mockMvc.perform(getWithNoParam(FIND_ID_URL + "/"))
                .andExpectAll(status().isOk(), view().name(FIND_ID_VIEW + VIEW_PROCESS));
        mockMvc.perform(getWithNoParam(MEMBERSHIP_URL + "/"))
                .andExpectAll(status().isOk(), view().name(MEMBERSHIP_VIEW + VIEW_PROCESS));
    }

    @DisplayName("기업 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    void companyArticleDtoSupportFilterAddTest() throws Exception {
        // given
        CompanyArticle article = testCompanyArticle;
        when(companyArticleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(companyArticleService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        when(companyService.findCompanyByName(article.getSubjectCompany())).thenReturn(Optional.of(samsungElectronics));
        doNothing().when(companyArticleService).removeArticleByName(article.getName());
        doNothing().when(companyArticleAddSimpleValidator).validate(any(), any());

        CompanyArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        CompanyArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        CompanyArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getValue());
        CompanyArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());

        String commonName = article.getName();
        String redirectedURL = fromPath(ADD_SINGLE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(commonName)).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace, articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));
            companyArticleService.removeArticleByName(commonName);
        }
    }

    @DisplayName("기업 기사 변경에 대한 기사 지원 필터 테스트")
    @Test
    void companyArticleDtoSupportFilterModifyTest() throws Exception {
        // given
        CompanyArticle beforeModifyArticle = testCompanyArticle;
        CompanyArticle article = CompanyArticle.builder().article(testNewCompanyArticle)
                .name(beforeModifyArticle.getName()).link(beforeModifyArticle.getLink()).build();
        when(companyArticleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(companyArticleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
        when(companyArticleService.registerArticle(testCompanyArticle)).thenReturn(article);
        when(companyService.findCompanyByName(article.getSubjectCompany())).thenReturn(Optional.of(samsungElectronics));
        doNothing().when(companyArticleService).correctArticle(article);

        CompanyArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        CompanyArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        CompanyArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getValue());
        CompanyArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());

        String redirectedURL = fromPath(UPDATE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);
        companyArticleService.registerArticle(beforeModifyArticle);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace, articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrlPattern(redirectedURL));
        }
    }

    @DisplayName("산업 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    void industryArticleDtoSupportFilterAddTest() throws Exception {
        // given & when
        IndustryArticle article = testIndustryArticle;
        when(industryArticleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(industryArticleService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        doNothing().when(industryArticleService).removeArticleByName(article.getName());
        doNothing().when(industryArticleAddSimpleValidator).validate(any(), any());

        IndustryArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        IndustryArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        IndustryArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getValue());
        IndustryArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());
        String commonName = article.toDto().getName();

        // then
        for (IndustryArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                    .andExpectAll(status().isSeeOther(), jsonPath(NAME).value(encodeWithUTF8(article.toDto().getName())));
            industryArticleService.removeArticleByName(commonName);
        }
    }

    @DisplayName("산업 기사 변경에 대한 기사 지원 필터 테스트")
    @Test
    void industryArticleDtoSupportFilterModifyTest() throws Exception {
        // given
        IndustryArticle beforeModifyArticle = testIndustryArticle;
        IndustryArticle article = IndustryArticle.builder().article(testNewIndustryArticle)
                .name(beforeModifyArticle.getName()).link(beforeModifyArticle.getLink()).build();
        when(industryArticleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(industryArticleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
        when(industryArticleService.registerArticle(testIndustryArticle)).thenReturn(article);
        doNothing().when(industryArticleService).correctArticle(article);

        IndustryArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        IndustryArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        IndustryArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getValue());
        IndustryArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());

        String redirectedURL = fromPath(modifyIndustryArticleFinishUrl).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString();

        // when
        industryArticleService.registerArticle(beforeModifyArticle);

        // then
        for (IndustryArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                    .andExpectAll(status().isSeeOther(), jsonPath(NAME).value(encodeWithUTF8(article.toDto().getName())));
        }
    }

    @DisplayName("추가에 대한 기사 메인 지원 필터 테스트")
    @Test
    void articleMainDtoSupportFilterAddTest() throws Exception {
        // given & when
        ArticleMain article = testCompanyArticleMain;
        String name = article.getName();
        when(articleMainService.findArticleByName(name)).thenReturn(Optional.of(article));
        when(articleMainService.findArticleByImagePath(article.getImagePath())).thenReturn(Optional.empty());
        when(articleMainService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        doNothing().when(articleMainService).removeArticleByName(article.getName());
        doNothing().when(articleMainAddValidator).validate(any(), any());

        ArticleMainDto articleDtoOriginal = createTestCompanyArticleMainDto();
        ArticleMainDto articleDtoLeftSpace = createTestCompanyArticleMainDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        ArticleMainDto articleDtoRightSpace = createTestCompanyArticleMainDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        ArticleMainDto articleDtoKoreanClassification = createTestCompanyArticleMainDto();
        articleDtoKoreanClassification.setClassification(testCompanyArticleMain.getClassification().getValue());
        ArticleMainDto articleDtoLowerCase = createTestCompanyArticleMainDto();
        articleDtoLowerCase.setClassification(articleDtoLowerCase.getClassification().toLowerCase());

        String redirectedURL = fromPath(ADD_ARTICLE_MAIN_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(articleDtoOriginal.getName()))
                .build().toUriString();

        // then
        for (ArticleMainDto articleDto : List.of(
                articleDtoLeftSpace, articleDtoRightSpace, articleDtoKoreanClassification, articleDtoLowerCase)){
            mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

            articleMainService.removeArticleByName(articleDtoOriginal.getName());
        }
    }

    @DisplayName("변경에 대한 기사 메인 지원 필터 테스트")
    @Test
    void articleMainDtoSupportFilterModifyTest() throws Exception {
        // given
        ArticleMain beforeModifyArticle = testCompanyArticleMain;
        ArticleMain article = ArticleMain.builder().article(testNewCompanyArticleMain).name(beforeModifyArticle.getName()).build();
        when(articleMainService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleMainService.registerArticle(testCompanyArticleMain)).thenReturn(article);
        doNothing().when(articleMainService).correctArticle(article);

        ArticleMainDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        ArticleMainDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        ArticleMainDto articleDtoKoreanClassification = createTestCompanyArticleMainDto();
        articleDtoKoreanClassification.setClassification(testCompanyArticleMain.getClassification().getValue());
        ArticleMainDto articleDtoLowerCase = createTestCompanyArticleMainDto();
        articleDtoLowerCase.setClassification(articleDtoLowerCase.getClassification().toLowerCase());

        String redirectedURL = fromPath(UPDATE_ARTICLE_MAIN_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString();

        // when
        articleMainService.registerArticle(beforeModifyArticle);

        // then
        for (ArticleMainDto articleDto : List.of(
                articleDtoLeftSpace, articleDtoRightSpace, articleDtoKoreanClassification, articleDtoLowerCase)) {
            mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));
        }
    }

    @DisplayName("추가에 대한 기업 지원 필터 테스트")
    @Test
    void companyDtoSupportFilterAddTest() throws Exception {
        // given & when
        Company company = samsungElectronics;
        when(companyService.findCompanyByName(company.getName())).thenReturn(Optional.of(company));
        doNothing().when(companyService).registerCompany(argThat(Objects::nonNull));
        doNothing().when(companyService).removeCompanyByCode(company.getCode());
        doNothing().when(companyAddValidator).validate(any(), any());

        CompanyDto companyDtoKorean = company.toDto();
        companyDtoKorean.setCountry(Country.valueOf(companyDtoKorean.getCountry()).getValue());
        companyDtoKorean.setScale(Scale.valueOf(companyDtoKorean.getScale()).getValue());
        companyDtoKorean.setFirstCategory(FirstCategory.valueOf(companyDtoKorean.getFirstCategory()).getValue());
        companyDtoKorean.setSecondCategory(SecondCategory.valueOf(companyDtoKorean.getSecondCategory()).getValue());

        CompanyDto companyDtoLowercase = company.toDto();
        companyDtoLowercase.setCountry(Country.valueOf(companyDtoLowercase.getCountry()).name().toLowerCase());
        companyDtoLowercase.setScale(Scale.valueOf(companyDtoLowercase.getScale()).name().toLowerCase());
        companyDtoLowercase.setFirstCategory(FirstCategory.valueOf(companyDtoLowercase.getFirstCategory()).name().toLowerCase());
        companyDtoLowercase.setSecondCategory(SecondCategory.valueOf(companyDtoLowercase.getSecondCategory()).name().toLowerCase());

        String redirectedURL = fromPath(ADD_SINGLE_COMPANY_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(company.toDto().getName())).build().toUriString();

        // then
        for (CompanyDto companyDto : List.of(companyDtoKorean, companyDtoLowercase)) {
            mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

            companyService.removeCompanyByCode(companyDto.getCode());
        }
    }

    @DisplayName("변경에 대한 기업 지원 필터 테스트")
    @Test
    void companyDtoSupportFilterModifyTest() throws Exception {
        // given
        Company beforeModifyCompany = samsungElectronics;
        Company company = Company.builder().company(skHynix)
                .name(beforeModifyCompany.getName()).code(beforeModifyCompany.getCode()).build();
        when(companyService.findCompanyByCode(company.getCode())).thenReturn(Optional.of(company));
        when(companyService.findCompanyByName(company.getName())).thenReturn(Optional.of(company));
        doNothing().when(companyService).registerCompany(samsungElectronics);
        doNothing().when(companyService).correctCompany(company);

        CompanyDto companyDtoKorean = company.toDto();
        companyDtoKorean.setCountry(Country.valueOf(companyDtoKorean.getCountry()).getValue());
        companyDtoKorean.setScale(Scale.valueOf(companyDtoKorean.getScale()).getValue());
        companyDtoKorean.setFirstCategory(FirstCategory.valueOf(companyDtoKorean.getFirstCategory()).getValue());
        companyDtoKorean.setSecondCategory(SecondCategory.valueOf(companyDtoKorean.getSecondCategory()).getValue());

        CompanyDto companyDtoLowercase = company.toDto();
        companyDtoLowercase.setCountry(Country.valueOf(companyDtoLowercase.getCountry()).name().toLowerCase());
        companyDtoLowercase.setScale(Scale.valueOf(companyDtoLowercase.getScale()).name().toLowerCase());
        companyDtoLowercase.setFirstCategory(FirstCategory.valueOf(companyDtoLowercase.getFirstCategory()).name().toLowerCase());
        companyDtoLowercase.setSecondCategory(SecondCategory.valueOf(companyDtoLowercase.getSecondCategory()).name().toLowerCase());

        String redirectedURL = fromPath(modifyCompanyFinishUrl).queryParam(NAME, encodeWithUTF8(beforeModifyCompany.getName())).build().toUriString();

        // when
        companyService.registerCompany(beforeModifyCompany);
        String commonName = beforeModifyCompany.getName();

        // then
        for (CompanyDto companyDto : List.of(companyDtoKorean, companyDtoLowercase)) {
            mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));
        }
    }
}
