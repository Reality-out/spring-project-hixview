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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_ARTICLE_WITH_THAT_NAME;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.MainUtility.decodeUTF8;
import static springsideproject1.springsideproject1build.utility.MainUtility.encodeUTF8;

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
        return ADD_COMPANY_ARTICLE_VIEW + "singleProcessPage";
    }

    @GetMapping(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddSingleCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute("layoutPath", FINISH_ADD_COMPANY_ARTICLE_PATH);
        model.addAttribute("name", decodeUTF8(name));
        return ADD_COMPANY_ARTICLE_VIEW + "singleFinishPage";
    }

    @PostMapping(ADD_SINGLE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddSingleCompanyArticle(RedirectAttributes redirect,
                                                String name, String press, String subjectCompany,
                                                String link, Integer year, Integer month, Integer date, Integer importance) {
        redirect.addAttribute("name", encodeUTF8(name));
        articleService.joinArticle(CompanyArticle.builder().name(name).press(press).subjectCompany(subjectCompany)
                .link(link).date(LocalDate.of(year, month, date)).importance(importance).build());
        return URL_REDIRECT_PREFIX + ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    /**
     * Add - Multiple
     */
    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddCompanyArticlesUsingString(Model model) {
        model.addAttribute("layoutPath", PROCESS_ADD_COMPANY_ARTICLE_PATH);
        return ADD_COMPANY_ARTICLE_VIEW + "multipleProcessStringPage";
    }

    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompanyArticlesUsingString(@RequestParam List<String> nameList, Model model) {
        model.addAttribute("layoutPath", FINISH_ADD_COMPANY_ARTICLE_PATH);
        model.addAttribute("nameList", decodeUTF8(nameList));
        return ADD_COMPANY_ARTICLE_VIEW + "multipleFinishStringPage";
    }

    @PostMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddCompanyArticlesUsingString(RedirectAttributes redirect, @RequestParam String subjectCompany,
                                                      @RequestParam String articleString, @RequestParam String linkString) {
        redirect.addAttribute("nameList", encodeUTF8(articleService.joinArticlesWithString(
                subjectCompany, articleString, linkString).stream().map(CompanyArticle::getName).collect(Collectors.toList())));
        return URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX;
    }

    /**
     * Update - Single
     */
    @GetMapping(UPDATE_COMPANY_ARTICLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public String initiateUpdateCompanyArticle(Model model) {
        model.addAttribute("layoutPath", PROCESS_UPDATE_COMPANY_ARTICLE_PATH);
		return UPDATE_COMPANY_ARTICLE_VIEW + "before" + VIEW_PASCAL_PROCESS_SUFFIX;
	}

    @PostMapping(UPDATE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processUpdateSingleArticle(Model model, @RequestParam String name) {
        Optional<CompanyArticle> articleOrEmpty = articleService.findArticleByName(name);
        if (articleOrEmpty.isEmpty()) {
            throw new IllegalStateException(NO_ARTICLE_WITH_THAT_NAME);
        } else {
            CompanyArticle article = articleOrEmpty.get();
            model.addAttribute("layoutPath", PROCESS_UPDATE_COMPANY_ARTICLE_PATH);
            model.addAttribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX);
            model.addAttribute("article", article);
            model.addAttribute("year", article.getDate().getYear());
            model.addAttribute("month", article.getDate().getMonthValue());
            model.addAttribute("date", article.getDate().getDayOfMonth());
        }
        return UPDATE_COMPANY_ARTICLE_VIEW + "after" + VIEW_PASCAL_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitUpdateSingleArticle(RedirectAttributes redirect,
                                            String name, String press, String subjectCompany,
                                            String link, Integer year, Integer month, Integer date, Integer importance) {
        articleService.updateArticle(CompanyArticle.builder().name(name).press(press).subjectCompany(subjectCompany)
                .link(link).date(LocalDate.of(year, month, date)).importance(importance).build());
        redirect.addAttribute("name", encodeUTF8(name));
        return URL_REDIRECT_PREFIX + UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
	@ResponseStatus(HttpStatus.OK)
	public String finishUpdateCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute("layoutPath", FINISH_UPDATE_COMPANY_ARTICLE_PATH);
        model.addAttribute("name", decodeUTF8(name));
		return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX;
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
        return MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @GetMapping(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishRemoveCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute("dataTypeKor", "기사");
        model.addAttribute("key", "제목");
        model.addAttribute("value", decodeUTF8(name));
        return MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX;
    }

    @PostMapping(REMOVE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitRemoveSingleArticle(RedirectAttributes redirect, @RequestParam String name) {
        redirect.addAttribute("name", encodeUTF8(name));
        return URL_REDIRECT_PREFIX + REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }
}