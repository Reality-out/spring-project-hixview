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
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.service.HomeService;

import java.util.Optional;

import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.name.ViewName.VIEW_SUB;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestPath.COMPANY_SEARCH_PATH;
import static site.hixview.domain.vo.user.RequestPath.COMPANY_SUB_PATH;
import static site.hixview.domain.vo.user.ViewName.COMPANY_VIEW;

@Controller
@RequiredArgsConstructor
public class UserCompanyController {
    private final HomeService homeService;
    private final CompanyService companyService;

    private final Logger log = LoggerFactory.getLogger(UserCompanyController.class);

    @ModelAttribute(LAYOUT_PATH)
    public String layoutPath() {
        return BASIC_LAYOUT;
    }

    /**
     * Main
     */
    @GetMapping(COMPANY_SUB_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String processCompanySubPage(Model model) {
        return COMPANY_VIEW + VIEW_SUB;
    }

    /**
     * Search
     */
    @GetMapping(value = {COMPANY_SEARCH_PATH, COMPANY_SEARCH_PATH + "{code}"})
    public String processCompanyShowPage(@PathVariable(name = CODE, required = false) String code, Model model) {
        Optional<Company> companyByCode = companyService.findCompanyByCode(code);
        if (companyByCode.isEmpty()) {
            model.addAttribute(COMPANY, companyService.findCompanyByName(homeService.findUsableLatestCompanyArticles().getFirst().getSubjectCompany()).orElseThrow());
        } else {
            model.addAttribute(COMPANY, companyByCode.orElseThrow());
        }
        return COMPANY_VIEW + VIEW_SHOW;
    }
}
