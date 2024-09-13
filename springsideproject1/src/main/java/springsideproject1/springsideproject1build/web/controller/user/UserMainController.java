package springsideproject1.springsideproject1build.web.controller.user;

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

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_COMPANY_ARTICLE_WITH_THAT_CONDITION;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_INDUSTRY_ARTICLE_WITH_THAT_CONDITION;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.MEMBER;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.vo.VIEW_NAME.*;

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
        model.addAttribute(LAYOUT_PATH, BASIC_LAYOUT_PATH);
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
        return USER_LOGIN_VIEW + "login-page";
    }

    /**
     * Find ID
     */
    @GetMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processFindIdPage(Model model) {
        model.addAttribute(MEMBER, new MemberDto());
        return USER_FIND_ID_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitFindIdPage(RedirectAttributes redirect, @ModelAttribute MemberDto memberDto) {
        Member member = Member.builder().memberDto(memberDto).build();
        redirect.addAttribute("idList", ControllerUtils.encodeWithUTF8(memberService.findMembersByNameAndBirth(
                member.getName(), member.getBirth()).stream().map(Member::getId).collect(Collectors.toList())));
        return URL_REDIRECT_PREFIX + FIND_ID_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(FIND_ID_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishFindIdPage(Model model, @RequestParam List<String> idList) {
        model.addAttribute("idList", ControllerUtils.decodeWithUTF8(idList));
        return USER_FIND_ID_VIEW + VIEW_FINISH_SUFFIX;
    }
}