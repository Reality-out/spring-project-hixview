package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static site.hixview.domain.vo.manager.RequestPath.*;
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
        model.addAttribute("addSingleCompanyArticle", ADD_SINGLE_COMPANY_ARTICLE_PATH);
        model.addAttribute("updateCompanyArticle", UPDATE_COMPANY_ARTICLE_PATH);
        model.addAttribute("removeCompanyArticle", REMOVE_COMPANY_ARTICLE_PATH);
        model.addAttribute("addCompanyArticlesWithString", ADD_COMPANY_ARTICLE_WITH_STRING_PATH);
        model.addAttribute("selectCompanyArticles", SELECT_COMPANY_ARTICLE_PATH);

        model.addAttribute("addSingleIndustryArticle", ADD_SINGLE_INDUSTRY_ARTICLE_PATH);
        model.addAttribute("updateIndustryArticle", UPDATE_INDUSTRY_ARTICLE_PATH);
        model.addAttribute("removeIndustryArticle", REMOVE_INDUSTRY_ARTICLE_PATH);
        model.addAttribute("addIndustryArticlesWithString", ADD_INDUSTRY_ARTICLE_WITH_STRING_PATH);
        model.addAttribute("selectIndustryArticles", SELECT_INDUSTRY_ARTICLE_PATH);

        model.addAttribute("addSingleEconomyArticle", ADD_SINGLE_ECONOMY_ARTICLE_PATH);
        model.addAttribute("updateEconomyArticle", UPDATE_ECONOMY_ARTICLE_PATH);
        model.addAttribute("removeEconomyArticle", REMOVE_ECONOMY_ARTICLE_PATH);
        model.addAttribute("addEconomyArticlesWithString", ADD_ECONOMY_ARTICLE_WITH_STRING_PATH);
        model.addAttribute("selectEconomyArticles", SELECT_ECONOMY_ARTICLE_PATH);

        model.addAttribute("addArticleMain", ADD_ARTICLE_MAIN_PATH);
        model.addAttribute("updateArticleMain", UPDATE_ARTICLE_MAIN_PATH);
        model.addAttribute("removeArticleMain", REMOVE_ARTICLE_MAIN_PATH);
        model.addAttribute("selectArticleMains", SELECT_ARTICLE_MAIN_PATH);
        model.addAttribute("checkImagePathArticleMains", CHECK_IMAGE_PATH_ARTICLE_MAIN_PATH);

        model.addAttribute("addBlogPost", ADD_BLOG_POST_PATH);
        model.addAttribute("updateBlogPost", UPDATE_BLOG_POST_PATH);
        model.addAttribute("removeBlogPost", REMOVE_BLOG_POST_PATH);
        model.addAttribute("selectBlogPosts", SELECT_BLOG_POST_PATH);
        model.addAttribute("checkTargetImagePathBlogPosts", CHECK_TARGET_IMAGE_PATH_BLOG_POST_PATH);

        model.addAttribute("addSingleCompany", ADD_SINGLE_COMPANY_PATH);
        model.addAttribute("updateCompany", UPDATE_COMPANY_PATH);
        model.addAttribute("removeCompany", REMOVE_COMPANY_PATH);
        model.addAttribute("selectCompanies", SELECT_COMPANY_PATH);

        model.addAttribute("selectMembers", SELECT_MEMBER_PATH);
        return MANAGER_HOME_VIEW;
    }
}
