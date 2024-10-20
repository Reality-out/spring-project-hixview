package site.hixview.web.controller.global;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import site.hixview.web.controller.trad.UserCompanyController;
import site.hixview.web.controller.trad.UserMainController;
import site.hixview.web.controller.trad.UserMemberController;

import static site.hixview.domain.vo.user.RequestUrl.CHECK_URL;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;

@ControllerAdvice(assignableTypes = {UserMainController.class, UserMemberController.class, UserCompanyController.class})
public class BasicNavigatorController {

    @ModelAttribute
    public void addURL(Model model) {
        model.addAttribute("companySearchURL", COMPANY_SEARCH_URL);
        model.addAttribute("checkURL", CHECK_URL);
    }
}
