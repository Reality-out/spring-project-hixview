package site.hixview.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.MANAGER_HOME_VIEW;

@Controller
@RequiredArgsConstructor
public class ManagerMainController {

    /**
     * Main
     */
    @GetMapping("/manager")
    @ResponseStatus(HttpStatus.OK)
    public String processManagerMainPage(Model model) {
        model.addAttribute("addSingleCompanyArticle", ADD_SINGLE_COMPANY_ARTICLE_URL);
        model.addAttribute("updateCompanyArticle", UPDATE_COMPANY_ARTICLE_URL);
        model.addAttribute("removeCompanyArticle", REMOVE_COMPANY_ARTICLE_URL);
        model.addAttribute("addCompanyArticlesWithString", ADD_COMPANY_ARTICLE_WITH_STRING_URL);
        model.addAttribute("selectCompanyArticles", SELECT_COMPANY_ARTICLE_URL);

        model.addAttribute("addSingleIndustryArticle", ADD_SINGLE_INDUSTRY_ARTICLE_URL);
        model.addAttribute("updateIndustryArticle", UPDATE_INDUSTRY_ARTICLE_URL);
        model.addAttribute("removeIndustryArticle", REMOVE_INDUSTRY_ARTICLE_URL);
        model.addAttribute("addIndustryArticlesWithString", ADD_INDUSTRY_ARTICLE_WITH_STRING_URL);
        model.addAttribute("selectIndustryArticles", SELECT_INDUSTRY_ARTICLE_URL);

        model.addAttribute("addArticleMain", ADD_ARTICLE_MAIN_URL);
        model.addAttribute("updateArticleMain", UPDATE_ARTICLE_MAIN_URL);
        model.addAttribute("removeArticleMain", REMOVE_ARTICLE_MAIN_URL);
        model.addAttribute("selectArticleMains", SELECT_ARTICLE_MAIN_URL);

        model.addAttribute("addSingleCompany", ADD_SINGLE_COMPANY_URL);
        model.addAttribute("updateCompany", UPDATE_COMPANY_URL);
        model.addAttribute("removeCompany", REMOVE_COMPANY_URL);
        model.addAttribute("selectCompanies", SELECT_COMPANY_URL);

        model.addAttribute("selectMembers", SELECT_MEMBER_URL);
        return MANAGER_HOME_VIEW;
    }
}
