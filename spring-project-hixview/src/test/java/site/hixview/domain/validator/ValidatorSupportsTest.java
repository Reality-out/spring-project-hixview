package site.hixview.domain.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.entity.company.dto.CompanyDto;
import site.hixview.domain.entity.home.dto.ArticleMainDto;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.entity.member.dto.MembershipDto;
import site.hixview.domain.validation.validator.*;
import site.hixview.support.context.RealControllerAndValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

@RealControllerAndValidatorContext
class ValidatorSupportsTest {

    // ArticleMain
    @Autowired
    private ArticleMainAddValidator articleMainAddValidator;

    @Autowired
    private ArticleMainModifyValidator articleMainModifyValidator;

    // BlogPost
    @Autowired
    private BlogPostAddValidator blogPostAddValidator;

    @Autowired
    private BlogPostModifyValidator blogPostModifyValidator;

    // Company
    @Autowired
    private CompanyAddValidator companyAddValidator;

    @Autowired
    private CompanyModifyValidator companyModifyValidator;

    // CompanyArticle
    @Autowired
    private CompanyArticleAddComplexValidator companyArticleAddComplexValidator;

    @Autowired
    private CompanyArticleAddSimpleValidator companyArticleAddSimpleValidator;

    @Autowired
    private CompanyArticleEntryDateValidator companyArticleEntryDateValidator;

    @Autowired
    private CompanyArticleModifyValidator companyArticleModifyValidator;

    // IndustryArticle
    @Autowired
    private IndustryArticleAddComplexValidator industryArticleAddComplexValidator;

    @Autowired
    private IndustryArticleAddSimpleValidator industryArticleAddSimpleValidator;

    @Autowired
    private IndustryArticleEntryDateValidator industryArticleEntryDateValidator;

    @Autowired
    private IndustryArticleModifyValidator industryArticleModifyValidator;

    // EconomyArticle
    @Autowired
    private EconomyArticleAddComplexValidator economyArticleAddComplexValidator;

    @Autowired
    private EconomyArticleAddSimpleValidator economyArticleAddSimpleValidator;

    @Autowired
    private EconomyArticleEntryDateValidator economyArticleEntryDateValidator;

    @Autowired
    private EconomyArticleModifyValidator economyArticleModifyValidator;

    // Membership
    @Autowired
    private MembershipValidator membershipValidator;

    @DisplayName("검증자 supports 이용 가능 클래스 테스트")
    @Test
    public void validatorSupportsTest() {
        // ArticleMain
        assertThat(articleMainAddValidator.supports(ArticleMainDto.class)).isEqualTo(true);
        assertThat(articleMainModifyValidator.supports(ArticleMainDto.class)).isEqualTo(true);

        // BlogPost
        assertThat(blogPostAddValidator.supports(BlogPostDto.class)).isEqualTo(true);
        assertThat(blogPostModifyValidator.supports(BlogPostDto.class)).isEqualTo(true);

        // Company
        assertThat(companyAddValidator.supports(CompanyDto.class)).isEqualTo(true);
        assertThat(companyModifyValidator.supports(CompanyDto.class)).isEqualTo(true);

        // CompanyArticle
        assertThat(companyArticleAddComplexValidator.supports(CompanyArticleDto.class)).isEqualTo(true);
        assertThat(companyArticleAddSimpleValidator.supports(CompanyArticleDto.class)).isEqualTo(true);
        assertThat(companyArticleEntryDateValidator.supports(CompanyArticleDto.class)).isEqualTo(true);
        assertThat(companyArticleModifyValidator.supports(CompanyArticleDto.class)).isEqualTo(true);

        // IndustryArticle
        assertThat(industryArticleAddComplexValidator.supports(IndustryArticleDto.class)).isEqualTo(true);
        assertThat(industryArticleAddSimpleValidator.supports(IndustryArticleDto.class)).isEqualTo(true);
        assertThat(industryArticleEntryDateValidator.supports(IndustryArticleDto.class)).isEqualTo(true);
        assertThat(industryArticleModifyValidator.supports(IndustryArticleDto.class)).isEqualTo(true);

        // EconomyArticle
        assertThat(economyArticleAddComplexValidator.supports(EconomyArticleDto.class)).isEqualTo(true);
        assertThat(economyArticleAddSimpleValidator.supports(EconomyArticleDto.class)).isEqualTo(true);
        assertThat(economyArticleEntryDateValidator.supports(EconomyArticleDto.class)).isEqualTo(true);
        assertThat(economyArticleModifyValidator.supports(EconomyArticleDto.class)).isEqualTo(true);

        // Membership
        assertThat(membershipValidator.supports(MembershipDto.class)).isEqualTo(true);
    }
}
