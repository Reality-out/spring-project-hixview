package springsideproject1.springsideproject1build.controller.manager;

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
import springsideproject1.springsideproject1build.domain.entity.company.Company;
import springsideproject1.springsideproject1build.domain.entity.company.CompanyDto;
import springsideproject1.springsideproject1build.domain.entity.company.Country;
import springsideproject1.springsideproject1build.domain.entity.company.Scale;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.domain.validator.field.CompanyDtoCodeValidator;
import springsideproject1.springsideproject1build.domain.validator.field.CompanyDtoConstraintValidator;
import springsideproject1.springsideproject1build.domain.validator.field.CompanyDtoNameValidator;

import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_COMPANY_WITH_THAT_CODE_OR_NAME;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.COMPANY;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.NUMBER_REGEX_PATTERN;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.VALUE;
import static springsideproject1.springsideproject1build.util.MainUtils.decodeWithUTF8;
import static springsideproject1.springsideproject1build.util.MainUtils.encodeWithUTF8;

@Controller
@RequiredArgsConstructor
public class ManagerCompanyController {

    private final CompanyService companyService;

    private final CompanyDtoConstraintValidator fieldValidator;
    private final CompanyDtoCodeValidator codeValidator;
    private final CompanyDtoNameValidator nameValidator;

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
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddCompany(@ModelAttribute(COMPANY) @Validated CompanyDto companyDto,
                                   BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (processBindingError(bindingResult, ADD_PROCESS_PATH, model) ||
                (processValidationErrorAdd(companyDto, bindingResult, model)))
            return ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
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
            log.error(ERRORS_ARE, NO_COMPANY_WITH_THAT_CODE_OR_NAME);
            model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
            model.addAttribute(ERROR, NOT_FOUND_COMPANY_ERROR);
            return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
        }
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
        model.addAttribute("updateUrl", UPDATE_COMPANY_URL + URL_FINISH_SUFFIX);
        model.addAttribute(COMPANY, companyOrEmpty.orElseThrow().toDto());
        model.addAttribute("countries", Country.values());
        model.addAttribute("scales", Scale.values());
        return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_COMPANY_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitModifyCompany(@ModelAttribute(COMPANY) @Validated CompanyDto companyDto,
                                      BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (processBindingError(bindingResult, UPDATE_PROCESS_PATH, model) ||
                (processValidationErrorModify(companyDto, bindingResult, model)))
            return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
        companyService.correctCompany(Company.builder().companyDto(companyDto).build());
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
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitRidCompany(@RequestParam String codeOrName, RedirectAttributes redirect, Model model) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(codeOrName);
        if (companyOrEmpty.isEmpty()) {
            log.error(ERRORS_ARE, NO_COMPANY_WITH_THAT_CODE_OR_NAME);
            model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_PATH);
            model.addAttribute(ERROR, NOT_FOUND_COMPANY_ERROR);
            return REMOVE_COMPANY_VIEW + VIEW_PROCESS_SUFFIX;
        }
        if (NUMBER_REGEX_PATTERN.matcher(codeOrName).matches()) {
            redirect.addAttribute(NAME, encodeWithUTF8(
                    companyService.findCompanyByCode(codeOrName).orElseThrow().getName()));
        } else {
            redirect.addAttribute(NAME, encodeWithUTF8(codeOrName));
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
    private boolean processBindingError(BindingResult bindingResult, String layoutPath, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), layoutPath, BEAN_VALIDATION_ERROR, model);
            return true;
        }
        return false;
    }

    private boolean processValidationErrorAdd(CompanyDto companyDto, BindingResult bindingResult, Model model) {
        fieldValidator.validate(companyDto, bindingResult);
        codeValidator.validate(companyDto, bindingResult);
        nameValidator.validate(companyDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, null, model);
            return true;
        }
        return false;
    }

    private boolean processValidationErrorModify(CompanyDto companyDto, BindingResult bindingResult, Model model) {
        fieldValidator.validate(companyDto, bindingResult);
        if (companyService.findCompanyByCode(companyDto.getCode()).isEmpty()) {
            bindingResult.rejectValue("code", "NotExist.company.code");
        }
        if (companyService.findCompanyByName(companyDto.getName()).isEmpty()) {
            bindingResult.rejectValue("name", "NotExist.company.name");
        }
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_PATH, null, model);
            return true;
        }
        return false;
    }

    private void finishForRollback(String logMessage, String layoutPath, String error, Model model) {
        log.error(ERRORS_ARE, logMessage);
        model.addAttribute(LAYOUT_PATH, layoutPath);
        model.addAttribute("countries", Country.values());
        model.addAttribute("scales", Scale.values());
        model.addAttribute(ERROR, error);
    }
}