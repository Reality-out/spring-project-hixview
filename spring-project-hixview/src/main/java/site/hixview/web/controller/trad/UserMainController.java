package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.entity.member.dto.MemberDto;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.service.*;
import site.hixview.util.ControllerUtils;

import java.util.List;
import java.util.stream.Collectors;

import static site.hixview.domain.vo.ExceptionMessage.*;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.Word.MEMBER;
import static site.hixview.domain.vo.name.ViewName.*;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestUrl.*;
import static site.hixview.domain.vo.user.ViewName.*;

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
        model.addAttribute(LAYOUT_PATH, BASIC_LAYOUT);
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
    @GetMapping(LOGIN_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processLoginPage(Model model) {
        model.addAttribute("membership", MEMBERSHIP_URL);
        model.addAttribute("findId", FIND_ID_URL);
        return LOGIN_VIEW + VIEW_SHOW;
    }

    /**
     * Find ID
     */
    @GetMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processFindIdPage(Model model) {
        model.addAttribute(MEMBER, new MemberDto());
        return FIND_ID_VIEW + VIEW_PROCESS;
    }

    @PostMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitFindIdPage(RedirectAttributes redirect, @ModelAttribute MemberDto memberDto) {
        Member member = Member.builder().memberDto(memberDto).build();
        redirect.addAttribute("idList", ControllerUtils.encodeWithUTF8(memberService.findMembersByNameAndBirthday(
                member.getName(), member.getBirthday()).stream().map(Member::getId).collect(Collectors.toList())));
        return REDIRECT_URL + FIND_ID_URL + FINISH_URL;
    }

    @GetMapping(FIND_ID_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishFindIdPage(Model model, @RequestParam List<String> idList) {
        model.addAttribute("idList", ControllerUtils.decodeWithUTF8(idList));
        return FIND_ID_VIEW + VIEW_FINISH;
    }
}