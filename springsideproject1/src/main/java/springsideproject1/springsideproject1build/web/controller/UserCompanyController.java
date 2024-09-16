package springsideproject1.springsideproject1build.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.service.CompanyService;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Company.COMPANY;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionString.NOT_EXIST_COMPANY_ERROR;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionString.NOT_FOUND_COMPANY_ERROR;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.REDIRECT_URL;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.SHOW_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.SUB_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.Word.ERROR;
import static springsideproject1.springsideproject1build.domain.vo.Word.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.user.Layout.BASIC_LAYOUT;
import static springsideproject1.springsideproject1build.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;
import static springsideproject1.springsideproject1build.domain.vo.user.RequestUrl.COMPANY_SUB_URL;
import static springsideproject1.springsideproject1build.domain.vo.user.ViewName.COMPANY_VIEW;

@Controller
@RequiredArgsConstructor
public class UserCompanyController {

    @Autowired
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
        model.addAttribute("companySearch", COMPANY_SEARCH_URL);
        return COMPANY_VIEW + SUB_VIEW;
    }

    /**
     * Select
     */
    @GetMapping(value = {COMPANY_SEARCH_URL, COMPANY_SEARCH_URL + "{codeOrName}"})
    public Object processCompanyLookUpPage(@PathVariable(name = "codeOrName", required = false) String codeOrName,
                                           RedirectAttributes redirect, Model model) {
        if (codeOrName == null) {
            redirect.addFlashAttribute(ERROR, NOT_EXIST_COMPANY_ERROR);
            return REDIRECT_URL + COMPANY_SUB_URL;
        }
        if (companyService.findCompanyByCodeOrName(codeOrName).isEmpty()) {
            redirect.addFlashAttribute(ERROR, NOT_FOUND_COMPANY_ERROR);
            return REDIRECT_URL + COMPANY_SUB_URL;
        }
        model.addAttribute(COMPANY, companyService.findCompanyByCodeOrName(codeOrName).orElseThrow());
        return COMPANY_VIEW + SHOW_VIEW;
    }
}
