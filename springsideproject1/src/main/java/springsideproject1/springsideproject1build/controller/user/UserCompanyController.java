package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.service.CompanyService;

import static springsideproject1.springsideproject1build.vo.CLASS.COMPANY;
import static springsideproject1.springsideproject1build.vo.LAYOUT.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.vo.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.vo.VIEW_NAME.*;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class UserCompanyController {

    @Autowired
    private final CompanyService companyService;

    /**
     * Main
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String processCompanySubPage(Model model) {
        model.addAttribute(LAYOUT_PATH, BASIC_LAYOUT_PATH);
        return USER_COMPANY_VIEW + VIEW_SUB_SUFFIX;
    }

    /**
     * Select
     */
    @GetMapping("/{codeOrName}")
    @ResponseStatus(HttpStatus.OK)
    public String processCompanyLookUpPage(@PathVariable String codeOrName, Model model) {
        model.addAttribute(LAYOUT_PATH, BASIC_LAYOUT_PATH);
        model.addAttribute(COMPANY, companyService.findCompanyByCodeOrName(codeOrName).orElseThrow());
        return USER_COMPANY_VIEW + VIEW_SHOW_SUFFIX;
    }
}
