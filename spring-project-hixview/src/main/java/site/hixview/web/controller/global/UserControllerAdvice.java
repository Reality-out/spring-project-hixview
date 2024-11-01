package site.hixview.web.controller.global;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import site.hixview.web.controller.trad.UserCompanyController;
import site.hixview.web.controller.trad.UserMainController;
import site.hixview.web.controller.trad.UserMemberController;

import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestPath.CHECK_PATH;
import static site.hixview.domain.vo.user.RequestPath.COMPANY_SEARCH_PATH;

@ControllerAdvice(assignableTypes = {UserMainController.class, UserMemberController.class, UserCompanyController.class})
public class UserControllerAdvice {

    @ModelAttribute
    public void addURL(Model model) {
        model.addAttribute("companySearchURL", COMPANY_SEARCH_PATH);
        model.addAttribute("checkURL", CHECK_PATH);
    }

    @ModelAttribute(LAYOUT_PATH)
    public String addLayoutPath() {
        return BASIC_LAYOUT;
    }
}
