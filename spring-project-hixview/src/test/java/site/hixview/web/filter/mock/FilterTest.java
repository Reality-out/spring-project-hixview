package site.hixview.web.filter.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.*;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.entity.company.dto.CompanyDto;
import site.hixview.domain.entity.home.ArticleMain;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.entity.home.dto.ArticleMainDto;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.*;
import site.hixview.domain.validation.validator.*;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.RequestPath.*;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

@OnlyRealControllerContext
class FilterTest implements ArticleTestUtils, BlogPostTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private IndustryArticleService industryArticleService;

    @Autowired
    private EconomyArticleService economyArticleService;

    @Autowired
    private ArticleMainService articleMainService;

    @Autowired
    private BlogPostService blogPostService;
    
    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyArticleAddSimpleValidator companyArticleAddSimpleValidator;

    @Autowired
    private CompanyArticleModifyValidator companyArticleModifyValidator;

    @Autowired
    private IndustryArticleAddSimpleValidator industryArticleAddSimpleValidator;

    @Autowired
    private IndustryArticleModifyValidator industryArticleModifyValidator;

    @Autowired
    private EconomyArticleAddSimpleValidator economyArticleAddSimpleValidator;

    @Autowired
    private EconomyArticleModifyValidator economyArticleModifyValidator;

    @Autowired
    private ArticleMainAddValidator articleMainAddValidator;

    @Autowired
    private ArticleMainModifyValidator articleMainModifyValidator;

    @Autowired
    private BlogPostAddValidator blogPostAddValidator;

    @Autowired
    private BlogPostModifyValidator blogPostModifyValidator;

    @Autowired
    private CompanyAddValidator companyAddValidator;

    @Autowired
    private CompanyModifyValidator companyModifyValidator;

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
        String redirectUrl = fromPath(ADD_SINGLE_COMPANY_ARTICLE_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(commonName)).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace, articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_PATH, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectUrl));
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
        doNothing().when(companyArticleModifyValidator).validate(any(), any());

        CompanyArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        CompanyArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        CompanyArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getValue());
        CompanyArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());

        String redirectUrl = fromPath(UPDATE_COMPANY_ARTICLE_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);
        companyArticleService.registerArticle(beforeModifyArticle);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace, articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrlPattern(redirectUrl));
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
            mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_PATH, articleDto))
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
        doNothing().when(industryArticleModifyValidator).validate(any(), any());

        IndustryArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        IndustryArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        IndustryArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getValue());
        IndustryArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());

        String redirectUrl = fromPath(modifyIndustryArticleFinishUrl).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString();

        // when
        industryArticleService.registerArticle(beforeModifyArticle);

        // then
        for (IndustryArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                    .andExpectAll(status().isSeeOther(), jsonPath(NAME).value(encodeWithUTF8(article.toDto().getName())));
        }
    }

    @DisplayName("경제 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    void economyArticleDtoSupportFilterAddTest() throws Exception {
        // given & when
        EconomyArticle article = testEconomyArticle;
        when(economyArticleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(economyArticleService.registerArticle(argThat(Objects::nonNull))).thenReturn(article);
        doNothing().when(economyArticleService).removeArticleByName(article.getName());
        doNothing().when(economyArticleAddSimpleValidator).validate(any(), any());

        EconomyArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        EconomyArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        EconomyArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getValue());
        EconomyArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());
        String commonName = article.toDto().getName();

        // then
        for (EconomyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithEconomyArticleDto(ADD_SINGLE_ECONOMY_ARTICLE_PATH, articleDto))
                    .andExpectAll(status().isSeeOther(), jsonPath(NAME).value(encodeWithUTF8(article.toDto().getName())));
            economyArticleService.removeArticleByName(commonName);
        }
    }

    @DisplayName("경제 기사 변경에 대한 기사 지원 필터 테스트")
    @Test
    void economyArticleDtoSupportFilterModifyTest() throws Exception {
        // given
        EconomyArticle beforeModifyArticle = testEconomyArticle;
        EconomyArticle article = EconomyArticle.builder().article(testNewEconomyArticle)
                .name(beforeModifyArticle.getName()).link(beforeModifyArticle.getLink()).build();
        when(economyArticleService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(economyArticleService.findArticleByLink(article.getLink())).thenReturn(Optional.of(article));
        when(economyArticleService.registerArticle(testEconomyArticle)).thenReturn(article);
        doNothing().when(economyArticleService).correctArticle(article);
        doNothing().when(economyArticleModifyValidator).validate(any(), any());

        EconomyArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        EconomyArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        EconomyArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getValue());
        EconomyArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());

        String redirectUrl = fromPath(modifyEconomyArticleFinishUrl).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString();

        // when
        economyArticleService.registerArticle(beforeModifyArticle);

        // then
        for (EconomyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithEconomyArticleDto(modifyEconomyArticleFinishUrl, articleDto))
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
        ArticleMainDto articleDtoShortImagePath = createTestCompanyArticleMainDto();
        articleDtoShortImagePath.setImagePath("company/samsung_display_001");

        String redirectUrl = fromPath(ADD_ARTICLE_MAIN_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(articleDtoOriginal.getName()))
                .build().toUriString();

        // then
        for (ArticleMainDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace, articleDtoKoreanClassification, articleDtoLowerCase, articleDtoShortImagePath)){
            mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_PATH, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectUrl));

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
        doNothing().when(articleMainModifyValidator).validate(any(), any());

        ArticleMainDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        ArticleMainDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        ArticleMainDto articleDtoKoreanClassification = createTestCompanyArticleMainDto();
        articleDtoKoreanClassification.setClassification(testCompanyArticleMain.getClassification().getValue());
        ArticleMainDto articleDtoLowerCase = createTestCompanyArticleMainDto();
        articleDtoLowerCase.setClassification(articleDtoLowerCase.getClassification().toLowerCase());
        ArticleMainDto articleDtoShortImagePath = createTestCompanyArticleMainDto();
        articleDtoShortImagePath.setImagePath("company/samsung_display_001");

        String redirectUrl = fromPath(UPDATE_ARTICLE_MAIN_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(article.getName())).build().toUriString();

        // when
        articleMainService.registerArticle(beforeModifyArticle);

        // then
        for (ArticleMainDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace, articleDtoKoreanClassification, articleDtoLowerCase, articleDtoShortImagePath)) {
            mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectUrl));
        }
    }

    @DisplayName("추가에 대한 블로그 포스트 지원 필터 테스트")
    @Test
    void blogPostDtoSupportFilterAddTest() throws Exception {
        // given & when
        BlogPost post = testBlogPostCompany;
        String name = post.getName();
        when(blogPostService.findPostByName(name)).thenReturn(Optional.empty());
        when(blogPostService.findPostByLink(post.getLink())).thenReturn(Optional.empty());
        when(blogPostService.registerPost(argThat(Objects::nonNull))).thenReturn(post);
        when(companyService.findCompanyByName(name)).thenReturn(
                Optional.of(Company.builder().company(samsungElectronics).name(name).build()));
        doNothing().when(blogPostService).removePostByName(name);
        doNothing().when(blogPostAddValidator).validate(any(), any());

        BlogPostDto postDtoOriginal = post.toDto();
        BlogPostDto postDtoLeftSpace = post.toDto();
        postDtoLeftSpace.setName(" " + postDtoLeftSpace.getName());
        BlogPostDto postDtoRightSpace = post.toDto();
        postDtoRightSpace.setName(postDtoRightSpace.getName() + " ");
        BlogPostDto postDtoKoreanClassification = post.toDto();
        postDtoKoreanClassification.setClassification(testBlogPostCompany.getClassification().getValue());
        BlogPostDto postDtoLowerCase = post.toDto();
        postDtoLowerCase.setClassification(postDtoLowerCase.getClassification().toLowerCase());
        BlogPostDto postDtoShortTargetImagePath = post.toDto();
        postDtoShortTargetImagePath.setTargetImagePath("company/logo/inno_wireless");

        // then
        for (BlogPostDto postDto : List.of(postDtoLeftSpace, postDtoRightSpace, postDtoKoreanClassification, postDtoLowerCase, postDtoShortTargetImagePath)){
            mockMvc.perform(postWithBlogPostDto(ADD_BLOG_POST_PATH, postDto))
                    .andExpectAll(status().isSeeOther(), jsonPath(NAME).value(encodeWithUTF8(name)));

            blogPostService.removePostByName(postDtoOriginal.getName());
        }
    }

    @DisplayName("변경에 대한 블로그 포스트 지원 필터 테스트")
    @Test
    void blogPostDtoSupportFilterModifyTest() throws Exception {
        // given
        BlogPost beforeModifyPost = testBlogPostCompany;
        BlogPost post = BlogPost.builder().blogPostDto(testBlogPostEconomy.toDto()).name(beforeModifyPost.getName()).build();
        String name = post.getName();
        when(blogPostService.findPostByName(name)).thenReturn(Optional.of(post));
        when(blogPostService.findPostByLink(post.getLink())).thenReturn(Optional.of(post));
        when(blogPostService.registerPost(testBlogPostCompany)).thenReturn(post);
        doNothing().when(blogPostService).correctPost(post);
        doNothing().when(blogPostModifyValidator).validate(any(), any());

        BlogPostDto postDtoLeftSpace = post.toDto();
        postDtoLeftSpace.setName(" " + postDtoLeftSpace.getName());
        BlogPostDto postDtoRightSpace = post.toDto();
        postDtoRightSpace.setName(postDtoRightSpace.getName() + " ");
        BlogPostDto postDtoKoreanClassification = post.toDto();
        postDtoKoreanClassification.setClassification(testBlogPostCompany.getClassification().getValue());
        BlogPostDto postDtoLowerCase = post.toDto();
        postDtoLowerCase.setClassification(postDtoLowerCase.getClassification().toLowerCase());
        BlogPostDto postDtoShortTargetImagePath = post.toDto();
        postDtoShortTargetImagePath.setTargetImagePath("company/logo/inno_wireless");

        // when
        blogPostService.registerPost(beforeModifyPost);

        // then
        for (BlogPostDto postDto : List.of(postDtoLeftSpace, postDtoRightSpace, postDtoKoreanClassification, postDtoLowerCase, postDtoShortTargetImagePath)) {
            mockMvc.perform(postWithBlogPostDto(modifyBlogPostFinishUrl, postDto))
                    .andExpectAll(status().isSeeOther(), jsonPath(NAME).value(encodeWithUTF8(name)));
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
        companyDtoKorean.setListedCountry(ListedCountry.valueOf(companyDtoKorean.getListedCountry()).getValue());
        companyDtoKorean.setScale(Scale.valueOf(companyDtoKorean.getScale()).getValue());
        companyDtoKorean.setFirstCategory(FirstCategory.valueOf(companyDtoKorean.getFirstCategory()).getValue());
        companyDtoKorean.setSecondCategory(SecondCategory.valueOf(companyDtoKorean.getSecondCategory()).getValue());

        CompanyDto companyDtoLowercase = company.toDto();
        companyDtoLowercase.setListedCountry(ListedCountry.valueOf(companyDtoLowercase.getListedCountry()).name().toLowerCase());
        companyDtoLowercase.setScale(Scale.valueOf(companyDtoLowercase.getScale()).name().toLowerCase());
        companyDtoLowercase.setFirstCategory(FirstCategory.valueOf(companyDtoLowercase.getFirstCategory()).name().toLowerCase());
        companyDtoLowercase.setSecondCategory(SecondCategory.valueOf(companyDtoLowercase.getSecondCategory()).name().toLowerCase());

        String redirectUrl = fromPath(ADD_SINGLE_COMPANY_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(company.toDto().getName())).build().toUriString();

        // then
        for (CompanyDto companyDto : List.of(companyDtoKorean, companyDtoLowercase)) {
            mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_PATH, companyDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectUrl));

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
        doNothing().when(companyModifyValidator).validate(any(), any());

        CompanyDto companyDtoKorean = company.toDto();
        companyDtoKorean.setListedCountry(ListedCountry.valueOf(companyDtoKorean.getListedCountry()).getValue());
        companyDtoKorean.setScale(Scale.valueOf(companyDtoKorean.getScale()).getValue());
        companyDtoKorean.setFirstCategory(FirstCategory.valueOf(companyDtoKorean.getFirstCategory()).getValue());
        companyDtoKorean.setSecondCategory(SecondCategory.valueOf(companyDtoKorean.getSecondCategory()).getValue());

        CompanyDto companyDtoLowercase = company.toDto();
        companyDtoLowercase.setListedCountry(ListedCountry.valueOf(companyDtoLowercase.getListedCountry()).name().toLowerCase());
        companyDtoLowercase.setScale(Scale.valueOf(companyDtoLowercase.getScale()).name().toLowerCase());
        companyDtoLowercase.setFirstCategory(FirstCategory.valueOf(companyDtoLowercase.getFirstCategory()).name().toLowerCase());
        companyDtoLowercase.setSecondCategory(SecondCategory.valueOf(companyDtoLowercase.getSecondCategory()).name().toLowerCase());

        String redirectUrl = fromPath(modifyCompanyFinishUrl).queryParam(NAME, encodeWithUTF8(beforeModifyCompany.getName())).build().toUriString();

        // when
        companyService.registerCompany(beforeModifyCompany);
        String commonName = beforeModifyCompany.getName();

        // then
        for (CompanyDto companyDto : List.of(companyDtoKorean, companyDtoLowercase)) {
            mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectUrl));
        }
    }
}
