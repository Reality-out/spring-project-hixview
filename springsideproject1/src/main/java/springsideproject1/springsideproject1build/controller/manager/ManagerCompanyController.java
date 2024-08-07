package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.company.Company;
import springsideproject1.springsideproject1build.domain.company.CompanyDto;
import springsideproject1.springsideproject1build.service.CompanyService;

import java.util.Optional;

import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_COMPANY_WITH_THAT_CODE;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.ConstantUtility.*;
import static springsideproject1.springsideproject1build.utility.MainUtility.decodeUTF8;
import static springsideproject1.springsideproject1build.utility.MainUtility.encodeUTF8;

@Controller
@RequiredArgsConstructor
public class ManagerCompanyController {

    @ModelAttribute(DATA_TYPE_KOREAN)
    public String dataTypeKor() {
        return "기업";
    }

    @ModelAttribute(KEY)
    public String key() {
        return "기업명";
    }

    @Autowired
    private final CompanyService companyService;

    /**
     * Add - Single
     */
    @GetMapping(ADD_SINGLE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddSingleCompany(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
        model.addAttribute(COMPANY, new CompanyDto());
        return ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
    }

    @GetMapping(ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddSingleCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute(VALUE, decodeUTF8(name));

        return MANAGER_ADD_VIEW + VIEW_SINGLE_FINISH_SUFFIX;
    }

    @PostMapping(ADD_SINGLE_COMPANY_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddSingleCompany(RedirectAttributes redirect, @ModelAttribute CompanyDto companyDto) {
        companyService.joinCompany(Company.builder().companyDto(companyDto).build());
        redirect.addAttribute(NAME, encodeUTF8(companyDto.getName()));
        return URL_REDIRECT_PREFIX + ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX;
    }

    /**
     * Select
     */
    @GetMapping(SELECT_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String selectCompany(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_PATH);
        model.addAttribute("companies", companyService.findCompanies());
        return MANAGER_SELECT_VIEW + "companiesPage";
    }

    /**
     * Update
     */
    @GetMapping(UPDATE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String initiateUpdateCompany(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
        return UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processUpdateCompany(@RequestParam String nameOrCode, Model model) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(nameOrCode);

        if (companyOrEmpty.isEmpty()) {
            throw new IllegalStateException(NO_COMPANY_WITH_THAT_CODE);
        } else {
            CompanyDto company = companyOrEmpty.get().toCompanyDto();
            model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
            model.addAttribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX);
            model.addAttribute(COMPANY, company);
        }
        return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitUpdateCompany(RedirectAttributes redirect, @ModelAttribute CompanyDto companyDto) {
        companyService.renewCompany(Company.builder().companyDto(companyDto).build());
        redirect.addAttribute(NAME, encodeUTF8(companyDto.getName()));
        return URL_REDIRECT_PREFIX + UPDATE_COMPANY_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishUpdateCompany(@RequestParam String name, Model model) {
        model.addAttribute(VALUE, decodeUTF8(name));
        return MANAGER_UPDATE_VIEW + VIEW_FINISH_SUFFIX;
    }

    /**
     * Remove
     */
    @GetMapping(REMOVE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRemoveCompany(Model model) {
        model.addAttribute(DATA_TYPE_ENGLISH, COMPANY);
        model.addAttribute(REMOVE_KEY, "nameOrCode");
        return MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @GetMapping(REMOVE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishRemoveCompany(@RequestParam String name, Model model) {
        model.addAttribute(VALUE, decodeUTF8(name));
        return MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX;
    }

    @PostMapping(REMOVE_COMPANY_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitRemoveCompany(RedirectAttributes redirect, @RequestParam String nameOrCode) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(nameOrCode);

        if (companyOrEmpty.isEmpty()) {
            throw new IllegalStateException(NO_COMPANY_WITH_THAT_CODE);
        } else {
            Company company = companyOrEmpty.get();
            companyService.removeCompany(company.getCode());
            redirect.addAttribute(NAME, encodeUTF8(company.getName()));
            return URL_REDIRECT_PREFIX + REMOVE_COMPANY_URL + URL_FINISH_SUFFIX;
        }
    }
}