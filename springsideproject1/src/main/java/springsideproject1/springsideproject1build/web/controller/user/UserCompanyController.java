package springsideproject1.springsideproject1build.web.controller.user;

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

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.COMPANY;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.vo.VIEW_NAME.*;

@Controller
@RequiredArgsConstructor
public class UserCompanyController {

    @Autowired
    private final CompanyService companyService;

    @ModelAttribute(LAYOUT_PATH)
    public String layoutPath() {
        return BASIC_LAYOUT_PATH;
    }

    /**
     * Main
     */
    @GetMapping(COMPANY_SUB_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processCompanySubPage(Model model) {
        model.addAttribute("companySearch", COMPANY_SEARCH_URL);
        return USER_COMPANY_VIEW + VIEW_SUB_SUFFIX;
    }

    /**
     * Select
     */
    @GetMapping(value = {COMPANY_SEARCH_URL, COMPANY_SEARCH_URL + "{codeOrName}"})
    public Object processCompanyLookUpPage(@PathVariable(name = "codeOrName", required = false) String codeOrName,
                                           RedirectAttributes redirect, Model model) {
        if (codeOrName == null) {
            redirect.addFlashAttribute(ERROR, NOT_EXIST_COMPANY_ERROR);
            return URL_REDIRECT_PREFIX + COMPANY_SUB_URL;
        }
        if (companyService.findCompanyByCodeOrName(codeOrName).isEmpty()) {
            redirect.addFlashAttribute(ERROR, NOT_FOUND_COMPANY_ERROR);
            return URL_REDIRECT_PREFIX + COMPANY_SUB_URL;
        }
        model.addAttribute(COMPANY, companyService.findCompanyByCodeOrName(codeOrName).orElseThrow());
        return USER_COMPANY_VIEW + VIEW_SHOW_SUFFIX;
    }
}
