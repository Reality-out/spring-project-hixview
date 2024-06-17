package springsideproject1.springsideproject1build.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.service.CompanyService;

import java.util.Optional;

import static springsideproject1.springsideproject1build.Utility.*;

@Controller
@RequiredArgsConstructor
public class UserCompanyController {
    private final CompanyService companyService;

    @GetMapping("/company")
    public String companySubPage() {
        return "user/company/subPage";
    }

    @GetMapping("/company/companies")
    public String companyLookUp(
            @RequestParam("companyCode") Long companyCode, Model model
    ) {
        model.addAttribute("companyName", companyService.SearchOneCompanyByCode(companyCode).get().getName());
        return "user/company/showCompany";
    }

    @GetMapping("/company/companies/search")
    public String companySearch(
            @RequestParam("nameOrCode") String nameOrCode
    ) {
        Optional<Company> company;
        if (isNumeric(nameOrCode)) {
            company = companyService.SearchOneCompanyByCode(Long.valueOf(nameOrCode));
        } else {
            company = companyService.SearchOneCompanyByName(nameOrCode);
        }
        return "redirect:/company/companies?companyCode=" + company.get().getCode();
    }
}
