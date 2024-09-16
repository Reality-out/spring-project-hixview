package springsideproject1.springsideproject1build.web.controller;

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
import springsideproject1.springsideproject1build.domain.validation.validator.CompanyAddValidator;
import springsideproject1.springsideproject1build.domain.validation.validator.CompanyModifyValidator;
import springsideproject1.springsideproject1build.util.ControllerUtils;

import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Company.COMPANY;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionMessage.NO_COMPANY_WITH_THAT_CODE_OR_NAME;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionString.BEAN_VALIDATION_ERROR;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionString.NOT_FOUND_COMPANY_ERROR;
import static springsideproject1.springsideproject1build.domain.vo.Regex.NUMBER_PATTERN;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.FINISH_URL;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.REDIRECT_URL;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.*;
import static springsideproject1.springsideproject1build.domain.vo.Word.*;
import static springsideproject1.springsideproject1build.domain.vo.manager.Layout.*;
import static springsideproject1.springsideproject1build.domain.vo.manager.RequestUrl.*;
import static springsideproject1.springsideproject1build.domain.vo.manager.ViewName.*;
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
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        model.addAttribute(COMPANY, new CompanyDto());
        model.addAttribute("countries", Country.values());
        model.addAttribute("scales", Scale.values());
        return ADD_COMPANY_VIEW + SINGLE_PROCESS_VIEW;
    }

    @PostMapping(ADD_SINGLE_COMPANY_URL)
    public String submitAddCompany(@ModelAttribute(COMPANY) @Validated CompanyDto companyDto,
                                   BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            return ADD_COMPANY_VIEW + SINGLE_PROCESS_VIEW;
        }

        addValidator.validate(companyDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, null, model);
            return ADD_COMPANY_VIEW + SINGLE_PROCESS_VIEW;
        }

        companyService.registerCompany(Company.builder().companyDto(companyDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(companyDto.getName()));
        return REDIRECT_URL + ADD_SINGLE_COMPANY_URL + FINISH_URL;
    }

    @GetMapping(ADD_SINGLE_COMPANY_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_COMPANY_VIEW + SINGLE_FINISH_VIEW;
    }

    /**
     * See
     */
    @GetMapping(SELECT_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeCompanies(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute("companies", companyService.findCompanies());
        return SELECT_VIEW + "companies-page";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String initiateModifyCompany(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
        return UPDATE_COMPANY_VIEW + BEFORE_PROCESS_VIEW;
    }

    @PostMapping(UPDATE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyCompany(@RequestParam String codeOrName, Model model) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(codeOrName);
        if (companyOrEmpty.isEmpty()) {
            finishForRollback(NO_COMPANY_WITH_THAT_CODE_OR_NAME, UPDATE_PROCESS_LAYOUT, NOT_FOUND_COMPANY_ERROR, model);
            return UPDATE_COMPANY_VIEW + BEFORE_PROCESS_VIEW;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
        model.addAttribute("updateUrl", UPDATE_COMPANY_URL + FINISH_URL);
        model.addAttribute(COMPANY, companyOrEmpty.orElseThrow().toDto());
        model.addAttribute("countries", Country.values());
        model.addAttribute("scales", Scale.values());
        return UPDATE_COMPANY_VIEW + AFTER_PROCESS_VIEW;
    }

    @PostMapping(UPDATE_COMPANY_URL + FINISH_URL)
    public String submitModifyCompany(@ModelAttribute(COMPANY) @Validated CompanyDto companyDto,
                                      BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_URL + FINISH_URL);
            return UPDATE_COMPANY_VIEW + AFTER_PROCESS_VIEW;
        }

        modifyValidator.validate(companyDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, null, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_URL + FINISH_URL);
            return UPDATE_COMPANY_VIEW + AFTER_PROCESS_VIEW;
        }
        redirect.addAttribute(NAME, encodeWithUTF8(companyDto.getName()));
        return REDIRECT_URL + UPDATE_COMPANY_URL + FINISH_URL;
    }

    @GetMapping(UPDATE_COMPANY_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishModifyCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_COMPANY_VIEW + FINISH_VIEW;
    }

    /**
     * Get rid of
     */
    @GetMapping(REMOVE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidCompany(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT);
        return REMOVE_COMPANY_URL_VIEW + PROCESS_VIEW;
    }

    @PostMapping(REMOVE_COMPANY_URL)
    public String submitRidCompany(@RequestParam String codeOrName, RedirectAttributes redirect, Model model) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(codeOrName);
        if (companyOrEmpty.isEmpty()) {
            finishForRollback(NO_COMPANY_WITH_THAT_CODE_OR_NAME, REMOVE_PROCESS_LAYOUT, NOT_FOUND_COMPANY_ERROR, model);
            return REMOVE_COMPANY_URL_VIEW + PROCESS_VIEW;
        }

        if (NUMBER_PATTERN.matcher(codeOrName).matches()) {
            redirect.addAttribute(NAME, encodeWithUTF8(
                    companyService.findCompanyByCode(codeOrName).orElseThrow().getName()));
            companyService.removeCompanyByCode(codeOrName);
        } else {
            redirect.addAttribute(NAME, encodeWithUTF8(codeOrName));
            companyService.removeCompanyByCode(companyService.findCompanyByName(codeOrName).orElseThrow().getCode());
        }
        return REDIRECT_URL + REMOVE_COMPANY_URL + FINISH_URL;
    }

    @GetMapping(REMOVE_COMPANY_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_COMPANY_URL_VIEW + FINISH_VIEW;
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