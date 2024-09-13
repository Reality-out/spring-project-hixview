package springsideproject1.springsideproject1build.web.controller.manager;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.entity.Country;
import springsideproject1.springsideproject1build.domain.entity.Scale;
import springsideproject1.springsideproject1build.domain.entity.company.Company;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.domain.validation.validator.company.CompanyAddValidator;
import springsideproject1.springsideproject1build.domain.validation.validator.company.CompanyModifyValidator;
import springsideproject1.springsideproject1build.util.ControllerUtils;

import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_COMPANY_WITH_THAT_CODE_OR_NAME;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.BEAN_VALIDATION_ERROR;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.NOT_FOUND_COMPANY_ERROR;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.COMPANY;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.vo.REGEX.NUMBER_REGEX_PATTERN;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.vo.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.vo.WORD.NAME;
import static springsideproject1.springsideproject1build.domain.vo.WORD.VALUE;
import static springsideproject1.springsideproject1build.util.ControllerUtils.decodeWithUTF8;
import static springsideproject1.springsideproject1build.util.ControllerUtils.encodeWithUTF8;

@Controller
@RequiredArgsConstructor
public class ManagerCompanyController {

    private final CompanyService companyService;

    private final CompanyAddValidator addValidator;
    private final CompanyModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerCompanyController.class);

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
    public String submitAddCompany(@ModelAttribute(COMPANY) @Validated CompanyDto companyDto,
                                   BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, BEAN_VALIDATION_ERROR, model);
            return ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
        }

        addValidator.validate(companyDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, null, model);
            return ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
        }

        companyService.registerCompany(Company.builder().companyDto(companyDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(companyDto.getName()));
        return URL_REDIRECT_PREFIX + ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_COMPANY_VIEW + VIEW_SINGLE_FINISH_SUFFIX;
    }

    /**
     * See
     */
    @GetMapping(SELECT_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeCompanies(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_PATH);
        model.addAttribute("companies", companyService.findCompanies());
        return MANAGER_SELECT_VIEW + "companies-page";
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
            finishForRollback(NO_COMPANY_WITH_THAT_CODE_OR_NAME, UPDATE_PROCESS_PATH, NOT_FOUND_COMPANY_ERROR, model);
            return UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
        model.addAttribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX);
        model.addAttribute(COMPANY, companyOrEmpty.orElseThrow().toDto());
        model.addAttribute("countries", Country.values());
        model.addAttribute("scales", Scale.values());
        return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX)
    public String submitModifyCompany(@ModelAttribute(COMPANY) @Validated CompanyDto companyDto,
                                      BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_PATH, BEAN_VALIDATION_ERROR, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX);
            return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
        }

        modifyValidator.validate(companyDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_PATH, null, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX);
            return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
        }
        redirect.addAttribute(NAME, encodeWithUTF8(companyDto.getName()));
        return URL_REDIRECT_PREFIX + UPDATE_COMPANY_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishModifyCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_COMPANY_VIEW + VIEW_FINISH_SUFFIX;
    }

    /**
     * Get rid of
     */
    @GetMapping(REMOVE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidCompany(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_PATH);
        return REMOVE_COMPANY_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(REMOVE_COMPANY_URL)
    public String submitRidCompany(@RequestParam String codeOrName, RedirectAttributes redirect, Model model) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(codeOrName);
        if (companyOrEmpty.isEmpty()) {
            finishForRollback(NO_COMPANY_WITH_THAT_CODE_OR_NAME, REMOVE_PROCESS_PATH, NOT_FOUND_COMPANY_ERROR, model);
            return REMOVE_COMPANY_VIEW + VIEW_PROCESS_SUFFIX;
        }

        if (NUMBER_REGEX_PATTERN.matcher(codeOrName).matches()) {
            redirect.addAttribute(NAME, encodeWithUTF8(
                    companyService.findCompanyByCode(codeOrName).orElseThrow().getName()));
            companyService.removeCompanyByCode(codeOrName);
        } else {
            redirect.addAttribute(NAME, encodeWithUTF8(codeOrName));
            companyService.removeCompanyByCode(companyService.findCompanyByName(codeOrName).orElseThrow().getCode());
        }
        return URL_REDIRECT_PREFIX + REMOVE_COMPANY_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(REMOVE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_COMPANY_VIEW + VIEW_FINISH_SUFFIX;
    }

    /**
     * Other private methods
     */
    // Handle Error
    private void finishForRollback(String logMessage, String layoutPath, String error, Model model) {
        ControllerUtils.finishForRollback(logMessage, layoutPath, error, model);
        model.addAttribute("countries", Country.values());
        model.addAttribute("scales", Scale.values());
    }
}