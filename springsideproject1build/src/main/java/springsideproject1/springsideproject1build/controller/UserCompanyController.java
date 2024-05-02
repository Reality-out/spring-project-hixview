package springsideproject1.springsideproject1build.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserCompanyController {

    @GetMapping("/company")
    public String companySubPage() {
        return "user/company/companySubPage";
    }

    @GetMapping("/company/companies")
    public String companyLookUp(
            @RequestParam("companyName") String companyName, Model model
    ) {
        model.addAttribute("companyName", companyName);
        return "user/company/companyShowSubPage";
    }

    @GetMapping("/company/hyundai")
    public String companyHyundai() {
        return "user/company/variouscompanies/hyundai";
    }
}
