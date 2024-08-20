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
import springsideproject1.springsideproject1build.domain.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.article.CompanyArticleDtoNoNumber;
import springsideproject1.springsideproject1build.error.AlreadyExistException;
import springsideproject1.springsideproject1build.error.NotMatchException;
import springsideproject1.springsideproject1build.service.CompanyArticleService;
import springsideproject1.springsideproject1build.service.CompanyService;
import springsideproject1.springsideproject1build.validation.validator.CompanyArticleDtoNoNumberValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static springsideproject1.springsideproject1build.error.constant.EXCEPTION_MESSAGE.*;
import static springsideproject1.springsideproject1build.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT.*;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.utility.WordUtils.*;
import static springsideproject1.springsideproject1build.utility.MainUtils.decodeUTF8;
import static springsideproject1.springsideproject1build.utility.MainUtils.encodeUTF8;

@Controller
@RequiredArgsConstructor
public class ManagerCompanyArticleController {

    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    private final CompanyArticleDtoNoNumberValidator companyArticleDtoNoNumberValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerCompanyArticleController.class);

    @ModelAttribute(DATA_TYPE_KOREAN)
    public String dataTypeKor() {
        return "기사";
    }

    @ModelAttribute(KEY)
    public String key() {
        return "기사명";
    }

    private final String nameListString = "nameList";

    /**
     * Add - Single
     */
    @GetMapping(ADD_SINGLE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddCompanyArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
        model.addAttribute(ARTICLE, new CompanyArticleDtoNoNumber());
        return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
    }

    @PostMapping(ADD_SINGLE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddCompanyArticle(@ModelAttribute("article") @Validated CompanyArticleDtoNoNumber articleDto,
                                          BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {                                            // Bean Validation
            log.error(ERRORS_ARE, bindingResult.getAllErrors());
            model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
            model.addAttribute(ERROR, BEAN_VALIDATION_ERROR);
            return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
        }

        companyArticleDtoNoNumberValidator.validate(articleDto, bindingResult);     // Custom Validation
        if (companyService.findCompanyByName(articleDto.getSubjectCompany()).isEmpty()) {
            bindingResult.rejectValue("subjectCompany", "NotFound.CompanyArticle.subjectCompany");
        }

        if (bindingResult.hasErrors()) {
            log.error(ERRORS_ARE, bindingResult.getAllErrors());
            model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
            return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
        }

        try {
            articleService.registerArticle(CompanyArticle.builder().articleDtoNoNumber(articleDto).build());
            redirect.addAttribute(NAME, encodeUTF8(articleDto.getName()));
            return URL_REDIRECT_PREFIX + ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
        } catch (AlreadyExistException e) {
            log.error(ERRORS_ARE, e.getMessage());
            model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
            model.addAttribute(ERROR, EXIST_COMPANY_ARTICLE_ERROR);
            return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
        }
    }

    @GetMapping(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute(VALUE, decodeUTF8(name));
        return MANAGER_ADD_VIEW + VIEW_SINGLE_FINISH_SUFFIX;
    }

    /**
     * Add - Multiple
     */
    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddCompanyArticlesWithString(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
        return ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage";
    }

    @PostMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddCompanyArticlesWithString(@RequestParam String subjectCompany, @RequestParam String articleString,
                                                     @RequestParam String linkString, RedirectAttributes redirect, Model model) {
        if (companyService.findCompanyByName(subjectCompany).isEmpty()) {
            log.error(ERRORS_ARE, NO_COMPANY_WITH_THAT_NAME);
            model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
            model.addAttribute(ERROR, NOT_FOUND_COMPANY_ERROR);
            return ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage";
        }
        try {
            redirect.addAttribute(nameListString, encodeUTF8(articleService.registerArticlesWithString(
                    subjectCompany, articleString, linkString).stream().map(CompanyArticle::getName).collect(Collectors.toList())));
            return URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX;
        } catch (NotMatchException e) {
            log.error(ERRORS_ARE, e.getMessage());
            model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
            model.addAttribute(ERROR, NOT_MATCHING_LINK_ERROR);
            return ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage";
        }
    }

    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompanyArticlesWithString(@RequestParam List<String> nameList, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute(nameListString, decodeUTF8(nameList));

        return MANAGER_ADD_VIEW + "multipleFinishPage";
    }

    /**
     * See
     */
    @GetMapping(SELECT_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeCompanyArticles(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_PATH);
        model.addAttribute("articles", articleService.findArticles());
        return MANAGER_SELECT_VIEW + "companyArticlesPage";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_COMPANY_ARTICLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public String initiateModifyCompanyArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
		return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
	}

    @PostMapping(UPDATE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyCompanyArticle(@RequestParam String numberOrName, Model model) {
        Optional<CompanyArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            log.error(ERRORS_ARE, NO_ARTICLE_WITH_THAT_NUMBER_OR_NAME);
            model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
            model.addAttribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR);
            return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
        }
        CompanyArticleDto article = articleOrEmpty.get().toDto();
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
        model.addAttribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX);
        model.addAttribute(ARTICLE, article);
        return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitModifyCompanyArticle(RedirectAttributes redirect, @ModelAttribute CompanyArticleDto articleDto) {
        articleService.correctArticle(CompanyArticle.builder().articleDto(articleDto).build());
        redirect.addAttribute(NAME, encodeUTF8(articleDto.getName()));
        return URL_REDIRECT_PREFIX + UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute(VALUE, decodeUTF8(name));

        return MANAGER_UPDATE_VIEW + VIEW_FINISH_SUFFIX;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidCompanyArticle(Model model) {
        model.addAttribute(DATA_TYPE_ENGLISH, ARTICLE);
        model.addAttribute(REMOVE_KEY, NAME);
        return MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(REMOVE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitRidCompanyArticle(RedirectAttributes redirect, @RequestParam String name) {
        articleService.removeArticle(name);
        redirect.addAttribute(NAME, encodeUTF8(name));
        return URL_REDIRECT_PREFIX + REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute(VALUE, decodeUTF8(name));
        return MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX;
    }
}