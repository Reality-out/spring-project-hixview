package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.service.*;

import java.util.List;

import static site.hixview.domain.vo.ExceptionMessage.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.user.RequestPath.*;
import static site.hixview.domain.vo.user.ViewName.LOGIN_VIEW;
import static site.hixview.domain.vo.user.ViewName.USER_HOME_VIEW;

@Controller
@RequiredArgsConstructor
public class UserMainController {
    private final HomeService homeService;
    private final MemberService memberService;
    private final CompanyArticleService companyArticleService;
    private final IndustryArticleService industryArticleService;
    private final EconomyArticleService economyArticleService;
    private final ArticleMainService articleMainService;

    /**
     * Main
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String processUserMainPage(Model model) {
        List<CompanyArticle> latestCompanyArticleList = homeService.findUsableLatestCompanyArticles();
        if (latestCompanyArticleList.isEmpty()) {
            throw new NotFoundException(NO_COMPANY_ARTICLE_WITH_THAT_CONDITION);
        }
        List<IndustryArticle> latestIndustryArticleList = homeService.findUsableLatestIndustryArticles();
        if (latestIndustryArticleList.isEmpty()) {
            throw new NotFoundException(NO_INDUSTRY_ARTICLE_WITH_THAT_CONDITION);
        }
        List<EconomyArticle> latestDomesticEconomyArticleList = homeService.findUsableLatestDomesticEconomyArticles();
        if (latestDomesticEconomyArticleList.isEmpty()) {
            throw new NotFoundException(NO_DOMESTIC_ECONOMY_ARTICLE_WITH_THAT_CONDITION);
        }
        List<EconomyArticle> latestForeignEconomyArticleOrEmpty = homeService.findUsableLatestForeignEconomyArticles();
        if (latestForeignEconomyArticleOrEmpty.isEmpty()) {
            throw new NotFoundException(NO_FOREIGN_ECONOMY_ARTICLE_WITH_THAT_CONDITION);
        }
        CompanyArticle latestCompanyArticle = latestCompanyArticleList.getFirst();
        IndustryArticle latestIndustryArticle = latestIndustryArticleList.getFirst();
        EconomyArticle latestDomesticEconomyArticle = latestDomesticEconomyArticleList.getFirst();
        EconomyArticle latestForeignEconomyArticle = latestForeignEconomyArticleOrEmpty.getFirst();
        model.addAttribute("latestCompanyArticle", latestCompanyArticle);
        model.addAttribute("latestIndustryArticle", latestIndustryArticle);
        model.addAttribute("latestDomesticEconomyArticle", latestDomesticEconomyArticle);
        model.addAttribute("latestForeignEconomyArticle", latestForeignEconomyArticle);
        model.addAttribute("latestCompanyArticleMain",
                articleMainService.findArticleByName(latestCompanyArticle.getName()).orElseThrow());
        model.addAttribute("latestIndustryArticleMain",
                articleMainService.findArticleByName(latestIndustryArticle.getName()).orElseThrow());
        model.addAttribute("latestDomesticEconomyArticleMain",
                articleMainService.findArticleByName(latestDomesticEconomyArticle.getName()).orElseThrow());
        model.addAttribute("latestForeignEconomyArticleMain",
                articleMainService.findArticleByName(latestForeignEconomyArticle.getName()).orElseThrow());
        return USER_HOME_VIEW;
    }

    /**
     * Login
     */
    @GetMapping(LOGIN_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String processLoginPage(Model model) {
        model.addAttribute("membership", MEMBERSHIP_PATH);
        model.addAttribute("findId", FIND_ID_PATH);
        return LOGIN_VIEW + VIEW_SHOW;
    }
}