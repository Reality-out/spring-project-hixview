package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static springsideproject1.springsideproject1build.vo.REQUEST_URL.*;

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

        model.addAttribute("addSingleCompany", ADD_SINGLE_COMPANY_URL);
        model.addAttribute("updateCompany", UPDATE_COMPANY_URL);
        model.addAttribute("removeCompany", REMOVE_COMPANY_URL);
        model.addAttribute("selectCompanies", SELECT_COMPANY_URL);

        model.addAttribute("removeMember", REMOVE_MEMBER_URL);
        model.addAttribute("selectMembers", SELECT_MEMBER_URL);
        return "manager/mainPage";
    }
}
