package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.Scale;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.entity.company.dto.CompanyDto;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.CompanyAddValidator;
import site.hixview.domain.validation.validator.CompanyModifyValidator;
import site.hixview.util.ControllerUtils;

import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.ExceptionMessage.NO_COMPANY_WITH_THAT_CODE_OR_NAME;
import static site.hixview.domain.vo.Regex.NUMBER_PATTERN;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.domain.vo.name.ExceptionName.BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_COMPANY_ERROR;
import static site.hixview.domain.vo.name.ViewName.*;
import static site.hixview.util.ControllerUtils.decodeWithUTF8;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

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
        model.addAttribute(LISTED_COUNTRIES, Country.values());
        model.addAttribute(SCALES, Scale.values());
        return ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS;
    }

    @PostMapping(ADD_SINGLE_COMPANY_URL)
    public String submitAddCompany(@ModelAttribute(COMPANY) @Validated CompanyDto companyDto,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            return ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS;
        }

        addValidator.validate(companyDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, null, model);
            return ADD_COMPANY_VIEW + VIEW_SINGLE_PROCESS;
        }

        companyService.registerCompany(Company.builder().companyDto(companyDto).build());
        return REDIRECT_URL + fromPath(ADD_SINGLE_COMPANY_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(companyDto.getName())).build().toUriString();
    }

    @GetMapping(ADD_SINGLE_COMPANY_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute("repeatUrl", ADD_SINGLE_COMPANY_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_COMPANY_VIEW + VIEW_SINGLE_FINISH;
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
        model.addAttribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT);
        return UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS;
    }

    @PostMapping(UPDATE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyCompany(@RequestParam String codeOrName, Model model) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(codeOrName);
        if (companyOrEmpty.isEmpty()) {
            finishForRollback(NO_COMPANY_WITH_THAT_CODE_OR_NAME, UPDATE_PROCESS_LAYOUT, NOT_FOUND_COMPANY_ERROR, model);
            return UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
        model.addAttribute("updateUrl", UPDATE_COMPANY_URL + FINISH_URL);
        model.addAttribute(COMPANY, companyOrEmpty.orElseThrow().toDto());
        model.addAttribute(LISTED_COUNTRIES, Country.values());
        model.addAttribute(SCALES, Scale.values());
        return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS;
    }

    @PostMapping(UPDATE_COMPANY_URL + FINISH_URL)
    public String submitModifyCompany(@ModelAttribute(COMPANY) @Validated CompanyDto companyDto,
                                      BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_URL + FINISH_URL);
            return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS;
        }

        modifyValidator.validate(companyDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, null, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_URL + FINISH_URL);
            return UPDATE_COMPANY_VIEW + VIEW_AFTER_PROCESS;
        }

        companyService.correctCompany(Company.builder().companyDto(companyDto).build());
        return REDIRECT_URL + fromPath(UPDATE_COMPANY_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(companyDto.getName())).build().toUriString();
    }

    @GetMapping(UPDATE_COMPANY_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishModifyCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT);
        model.addAttribute("repeatUrl", UPDATE_COMPANY_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_COMPANY_VIEW + VIEW_FINISH;
    }

    /**
     * Get rid of
     */
    @GetMapping(REMOVE_COMPANY_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidCompany(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT);
        return REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS;
    }

    @PostMapping(REMOVE_COMPANY_URL)
    public String submitRidCompany(@RequestParam String codeOrName, Model model) {
        Optional<Company> companyOrEmpty = companyService.findCompanyByCodeOrName(codeOrName);
        if (companyOrEmpty.isEmpty()) {
            finishForRollback(NO_COMPANY_WITH_THAT_CODE_OR_NAME, REMOVE_PROCESS_LAYOUT, NOT_FOUND_COMPANY_ERROR, model);
            return REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS;
        }

        if (NUMBER_PATTERN.matcher(codeOrName).matches()) {
            codeOrName = companyService.findCompanyByCode(codeOrName).orElseThrow().getName();
        }
        companyService.removeCompanyByCode(companyService.findCompanyByName(codeOrName).orElseThrow().getCode());
        return REDIRECT_URL + fromPath(REMOVE_COMPANY_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(codeOrName)).build().toUriString();
    }

    @GetMapping(REMOVE_COMPANY_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidCompany(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT);
        model.addAttribute("repeatUrl", REMOVE_COMPANY_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_COMPANY_URL_VIEW + VIEW_FINISH;
    }

    /**
     * Other private methods
     */
    // Handle Error
    private void finishForRollback(String logMessage, String layoutPath, String error, Model model) {
        ControllerUtils.finishForRollback(logMessage, layoutPath, error, model);
        model.addAttribute(LISTED_COUNTRIES, Country.values());
        model.addAttribute(SCALES, Scale.values());
    }
}