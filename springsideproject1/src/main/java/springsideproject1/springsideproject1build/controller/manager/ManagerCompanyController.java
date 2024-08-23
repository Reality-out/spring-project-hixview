package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.entity.company.Company;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;
import springsideproject1.springsideproject1build.domain.entity.company.Country;
import springsideproject1.springsideproject1build.domain.entity.company.Scale;
import springsideproject1.springsideproject1build.domain.service.CompanyService;

import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_COMPANY_WITH_THAT_CODE;
import static springsideproject1.springsideproject1build.util.MainUtils.decodeUTF8;
import static springsideproject1.springsideproject1build.util.MainUtils.encodeUTF8;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.COMPANY;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.*;

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
    public String processAddCompany(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
        model.addAttribute(COMPANY, new CompanyDto());
        model.addAttribute("countries", Country.values());
        model.addAttribute("scales", Scale.values());
        return ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
    }

    @PostMapping(ADD_SINGLE_COMPANY_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddCompany(RedirectAttributes redirect, @ModelAttribute CompanyDto companyDto) {
        companyService.registerCompany(Company.builder().companyDto(companyDto).build());
        redirect.addAttribute(NAME, encodeUTF8(companyDto.getName()));
        return URL_REDIRECT_PREFIX + ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute(VALUE, decodeUTF8(name));

        return MANAGER_ADD_VIEW + VIEW_SINGLE_FINISH_SUFFIX;
    }

    /**
     * See
     */
    @GetMapping(SELECT_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeCompanies(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_PATH);
        model.addAttribute("companies", companyService.findCompanies());
        return MANAGER_SELECT_VIEW + "companiesPage";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String initiateModifyCompany(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
        return UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyCompany(@RequestParam String codeOrName, Model model) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(codeOrName);

        if (companyOrEmpty.isEmpty()) {
            throw new IllegalStateException(NO_COMPANY_WITH_THAT_CODE);
        } else {
            CompanyDto company = companyOrEmpty.orElseThrow().toCompanyDto();
            model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
            model.addAttribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX);
            model.addAttribute(COMPANY, company);
            model.addAttribute("countries", Country.values());
            model.addAttribute("scales", Scale.values());
        }
        return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitModifyCompany(RedirectAttributes redirect, @ModelAttribute CompanyDto companyDto) {
        companyService.correctCompany(Company.builder().companyDto(companyDto).build());
        redirect.addAttribute(NAME, encodeUTF8(companyDto.getName()));
        return URL_REDIRECT_PREFIX + UPDATE_COMPANY_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishModifyCompany(@RequestParam String name, Model model) {
        model.addAttribute(VALUE, decodeUTF8(name));
        return MANAGER_UPDATE_VIEW + VIEW_FINISH_SUFFIX;
    }

    /**
     * Get rid of
     */
    @GetMapping(REMOVE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidCompany(Model model) {
        model.addAttribute(DATA_TYPE_ENGLISH, COMPANY);
        model.addAttribute(REMOVE_KEY, "codeOrName");
        return MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(REMOVE_COMPANY_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitRidCompany(RedirectAttributes redirect, @RequestParam String codeOrName) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(codeOrName);

        if (companyOrEmpty.isEmpty()) {
            throw new IllegalStateException(NO_COMPANY_WITH_THAT_CODE);
        } else {
            Company company = companyOrEmpty.orElseThrow();
            companyService.removeCompany(company.getCode());
            redirect.addAttribute(NAME, encodeUTF8(company.getName()));
            return URL_REDIRECT_PREFIX + REMOVE_COMPANY_URL + URL_FINISH_SUFFIX;
        }
    }

    @GetMapping(REMOVE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidCompany(@RequestParam String name, Model model) {
        model.addAttribute(VALUE, decodeUTF8(name));
        return MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX;
    }
}