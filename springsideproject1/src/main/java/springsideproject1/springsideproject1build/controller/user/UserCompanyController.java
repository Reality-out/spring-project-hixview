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
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.service.CompanyService;

import static springsideproject1.springsideproject1build.Utility.isNumeric;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class UserCompanyController {

    @Autowired
    private final CompanyService companyService;

    /*
     * GetMapping
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String companySubPage() {
        return "user/company/subPage";
    }

    @GetMapping("/{nameOrCode}")
    @ResponseStatus(HttpStatus.OK)
    public String companyLookUp(
            @PathVariable String nameOrCode, Model model) {

        Company company = (isNumeric(nameOrCode)) ?
                companyService.SearchOneCompanyByCode(nameOrCode).get() : companyService.SearchOneCompanyByName(nameOrCode).get();

        model.addAttribute("companyName", company.getName());
        model.addAttribute("companyCode", company.getCode());
        return "user/company/showCompanyPage";
    }
}