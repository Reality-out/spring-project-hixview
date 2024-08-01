package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.service.CompanyArticleService;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static springsideproject1.springsideproject1build.utility.MainUtility.decodeUTF8;
import static springsideproject1.springsideproject1build.config.constant.FOLDER_PATH_CONFIG.FINISH_ADD_COMPANY_ARTICLE_PATH;
import static springsideproject1.springsideproject1build.config.constant.FOLDER_PATH_CONFIG.PROCESS_ADD_COMPANY_ARTICLE_PATH;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;

@Controller
@RequiredArgsConstructor
public class ManagerCompanyArticleController {

    @Autowired
    private final CompanyArticleService articleService;

    /**
     * Add - Single
     */
    @GetMapping(ADD_SINGLE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddSingleCompanyArticle(Model model) {
        model.addAttribute("layoutPath", PROCESS_ADD_COMPANY_ARTICLE_PATH);
        return ADD_COMPANY_ARTICLE_VIEW_NAME + "singleProcessPage";
    }

    @GetMapping(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddSingleCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute("layoutPath", FINISH_ADD_COMPANY_ARTICLE_PATH);
        model.addAttribute("name", name);
        return ADD_COMPANY_ARTICLE_VIEW_NAME + "singleFinishPage";
    }

    @PostMapping(ADD_SINGLE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddSingleCompanyArticle(RedirectAttributes redirect,
                                                String name, String press, String subjectCompany,
                                                String link, int year, int month, int date, Integer importance) {
        redirect.addAttribute("name", name);
        articleService.joinArticle(new CompanyArticle.CompanyArticleBuilder().name(name).press(press).subjectCompany(subjectCompany)
                .link(link).date(LocalDate.of(year, month, date)).importance(importance).build());
        return URL_REDIRECT_PREFIX + ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    /**
     * Add - Multiple
     */
    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddCompanyArticleUsingString(Model model) {
        model.addAttribute("layoutPath", PROCESS_ADD_COMPANY_ARTICLE_PATH);
        return ADD_COMPANY_ARTICLE_VIEW_NAME + "multipleProcessStringPage";
    }

    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompanyArticleUsingString(@RequestParam List<String> nameList, Model model) {
        model.addAttribute("layoutPath", FINISH_ADD_COMPANY_ARTICLE_PATH);
        model.addAttribute("nameList", decodeUTF8(nameList));
        return ADD_COMPANY_ARTICLE_VIEW_NAME + "multipleFinishStringPage";
    }

    @PostMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddCompanyArticleUsingString(RedirectAttributes redirect, @RequestParam String subjectCompany,
                                                     @RequestParam String articleString, @RequestParam String linkString) {
        redirect.addAttribute("nameList",
                articleService.joinArticlesWithString(subjectCompany, articleString, linkString));
        return URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX;
    }

    /**
     * Remove - Single
     */
    @GetMapping(REMOVE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRemoveCompanyArticle(Model model) {
        model.addAttribute("dataTypeKor", "기사");
        model.addAttribute("dataTypeEng", "article");
        model.addAttribute("key", "name");
        return MANAGER_REMOVE_VIEW_NAME + VIEW_NAME_PROCESS_SUFFIX;
    }

    @GetMapping(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishRemoveCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute("dataTypeKor", "기사");
        model.addAttribute("key", "제목");
        model.addAttribute("value", URLDecoder.decode(name, StandardCharsets.UTF_8));
        return MANAGER_REMOVE_VIEW_NAME + VIEW_NAME_FINISH_SUFFIX;
    }

    @PostMapping(REMOVE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitRemoveSingleArticle(RedirectAttributes redirect, @RequestParam String name) {
        redirect.addAttribute("name", URLEncoder.encode(name, StandardCharsets.UTF_8));
        return URL_REDIRECT_PREFIX + REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }
}