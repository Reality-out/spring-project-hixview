package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.hixview.domain.service.CompanyService;

import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.name.ViewName.VIEW_SUB;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SUB_URL;
import static site.hixview.domain.vo.user.ViewName.COMPANY_VIEW;

@Controller
@RequiredArgsConstructor
public class UserCompanyController {

    private final Logger log = LoggerFactory.getLogger(UserCompanyController.class);
    private final CompanyService companyService;

    @ModelAttribute(LAYOUT_PATH)
    public String layoutPath() {
        return BASIC_LAYOUT;
    }

    /**
     * Main
     */
    @GetMapping(COMPANY_SUB_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processCompanySubPage(Model model) {
        return COMPANY_VIEW + VIEW_SUB;
    }

    /**
     * Search
     */
    @GetMapping(COMPANY_SEARCH_URL + "{code}")
    public String processCompanyShowPage(@PathVariable(name = CODE) String code, Model model) {
        model.addAttribute(COMPANY, companyService.findCompanyByCode(code).orElseThrow());
        return COMPANY_VIEW + VIEW_SHOW;
    }
}
