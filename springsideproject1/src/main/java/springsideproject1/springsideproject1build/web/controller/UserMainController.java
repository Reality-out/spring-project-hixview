package springsideproject1.springsideproject1build.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.entity.member.Member;
import springsideproject1.springsideproject1build.domain.entity.member.MemberDto;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;
import springsideproject1.springsideproject1build.domain.service.ArticleMainService;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.IndustryArticleService;
import springsideproject1.springsideproject1build.domain.service.MemberService;
import springsideproject1.springsideproject1build.util.ControllerUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Member.MEMBER;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionMessage.NO_COMPANY_ARTICLE_WITH_THAT_CONDITION;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionMessage.NO_INDUSTRY_ARTICLE_WITH_THAT_CONDITION;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.FINISH_URL;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.REDIRECT_URL;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.FINISH_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.PROCESS_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.Word.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.user.Layout.BASIC_LAYOUT;
import static springsideproject1.springsideproject1build.domain.vo.user.RequestUrl.FIND_ID_URL;
import static springsideproject1.springsideproject1build.domain.vo.user.RequestUrl.MEMBERSHIP_URL;
import static springsideproject1.springsideproject1build.domain.vo.user.ViewName.*;

@Controller
@RequiredArgsConstructor
public class UserMainController {

    @Autowired
    private final MemberService memberService;

    @Autowired
    private final CompanyArticleService companyArticleService;

    @Autowired
    private final IndustryArticleService industryArticleService;

    @Autowired
    private final ArticleMainService articleMainService;

    /**
     * Main
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String processUserMainPage(Model model) {
        Optional<CompanyArticle> latestCompanyArticleOrEmpty = companyArticleService.findLatestArticles().stream()
                .filter(article -> articleMainService.findArticleByName(article.getName()).isPresent()).findFirst();
        if (latestCompanyArticleOrEmpty.isEmpty()) {
            throw new NotFoundException(NO_COMPANY_ARTICLE_WITH_THAT_CONDITION);
        }

        Optional<IndustryArticle> latestIndustryArticleOrEmpty = industryArticleService.findLatestArticles().stream()
                .filter(article -> articleMainService.findArticleByName(article.getName()).isPresent()).findFirst();
        if (latestIndustryArticleOrEmpty.isEmpty()) {
            throw new NotFoundException(NO_INDUSTRY_ARTICLE_WITH_THAT_CONDITION);
        }

        CompanyArticle latestCompanyArticle = latestCompanyArticleOrEmpty.orElseThrow();
        IndustryArticle latestIndustryArticle = latestIndustryArticleOrEmpty.orElseThrow();
        model.addAttribute(LAYOUT_PATH, BASIC_LAYOUT);
        model.addAttribute("latestCompanyArticle", latestCompanyArticle);
        model.addAttribute("latestIndustryArticle", latestIndustryArticle);
        model.addAttribute("latestCompanyArticleMain",
                articleMainService.findArticleByName(latestCompanyArticle.getName()).orElseThrow());
        model.addAttribute("latestIndustryArticleMain",
                articleMainService.findArticleByName(latestIndustryArticle.getName()).orElseThrow());
        return USER_HOME_VIEW;
    }

    /**
     * Login
     */
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String processLoginPage(Model model) {
        model.addAttribute("membership", MEMBERSHIP_URL);
        model.addAttribute("findId", FIND_ID_URL);
        return LOGIN_VIEW + "login-page";
    }

    /**
     * Find ID
     */
    @GetMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processFindIdPage(Model model) {
        model.addAttribute(MEMBER, new MemberDto());
        return FIND_ID_VIEW + PROCESS_VIEW;
    }

    @PostMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitFindIdPage(RedirectAttributes redirect, @ModelAttribute MemberDto memberDto) {
        Member member = Member.builder().memberDto(memberDto).build();
        redirect.addAttribute("idList", ControllerUtils.encodeWithUTF8(memberService.findMembersByNameAndBirth(
                member.getName(), member.getBirth()).stream().map(Member::getId).collect(Collectors.toList())));
        return REDIRECT_URL + FIND_ID_URL + FINISH_URL;
    }

    @GetMapping(FIND_ID_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishFindIdPage(Model model, @RequestParam List<String> idList) {
        model.addAttribute("idList", ControllerUtils.decodeWithUTF8(idList));
        return FIND_ID_VIEW + FINISH_VIEW;
    }
}