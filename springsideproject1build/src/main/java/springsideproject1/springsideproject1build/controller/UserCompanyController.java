package springsideproject1.springsideproject1build.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserCompanyController {

    @GetMapping("/company")
    public String companySubPage() {
        return "user/company/companySubPage";
    }

    @GetMapping("/company/samsungelectronics")
    public String companySamsungElectronicsSubPage() {
        return "user/company/variouscompanies/samsungelectronics";
    }

    @GetMapping("/company/hyundai")
    public String companyHyundai() {
        return "user/company/variouscompanies/hyundai";
    }
}
