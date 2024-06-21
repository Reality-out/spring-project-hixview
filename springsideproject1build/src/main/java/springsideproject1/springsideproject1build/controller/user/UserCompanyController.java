package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.service.CompanyService;

import java.util.Optional;

import static springsideproject1.springsideproject1build.Utility.*;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class UserCompanyController {
    private final CompanyService companyService;

    @GetMapping
    public String companySubPage() {
        return "user/company/subPage";
    }

    @GetMapping("/companies")
    public String companyLookUp(
            @RequestParam("nameOrCode") String nameOrCode, Model model
    ) {
        Optional<Company> company;
        if (isNumeric(nameOrCode)) {
            company = companyService.SearchOneCompanyByCode(Long.valueOf(nameOrCode));
        } else {
            company = companyService.SearchOneCompanyByName(nameOrCode);
        }
        model.addAttribute("companyName", company.get().getName());
        return "user/company/showCompany";
    }
}
