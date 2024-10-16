package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.hixview.domain.service.CompanyService;

import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.name.EntityName.Company.COMPANY;
import static site.hixview.domain.vo.name.ExceptionName.NOT_EXIST_COMPANY_ERROR;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_COMPANY_ERROR;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.name.ViewName.VIEW_SUB;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SUB_URL;
import static site.hixview.domain.vo.user.ViewName.COMPANY_VIEW;

@Controller
@RequiredArgsConstructor
public class UserCompanyController {

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
        return COMPANY_VIEW + VIEW_SUB;
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
        return COMPANY_VIEW + VIEW_SHOW;
    }
}
