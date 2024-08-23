package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.error.ConstraintValidationException;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.domain.validation.CompanyArticleDtoFieldValidator;
import springsideproject1.springsideproject1build.domain.validation.CompanyArticleDtoObjectComplexValidator;
import springsideproject1.springsideproject1build.domain.validation.CompanyArticleDtoObjectSimpleValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.*;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.util.MainUtils.decodeUTF8;
import static springsideproject1.springsideproject1build.util.MainUtils.encodeUTF8;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.NUMBER_REGEX;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.*;

@Controller
@RequiredArgsConstructor
public class ManagerCompanyArticleController {

    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    private final Validator defaultValidator;
    private final CompanyArticleDtoFieldValidator fieldValidator;
    private final CompanyArticleDtoObjectComplexValidator objectComplexValidator;
    private final CompanyArticleDtoObjectSimpleValidator objectSimpleValidator;

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
        model.addAttribute(ARTICLE, new CompanyArticleDto());
        return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
    }

    @PostMapping(ADD_SINGLE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddCompanyArticle(@ModelAttribute("article") @Validated CompanyArticleDto articleDto,
                                          BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        String senderPage = ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
        if (bindingResult.hasErrors()) {
            handleErrorForModel(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, BEAN_VALIDATION_ERROR, model);
            return senderPage;
        }
        fieldValidator.validate(articleDto, bindingResult);
        objectComplexValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            handleErrorForModel(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, null, model);
            return senderPage;
        }
        articleService.registerArticle(CompanyArticle.builder().articleDto(articleDto).build());
        redirect.addAttribute(NAME, encodeUTF8(articleDto.getName()));
        return URL_REDIRECT_PREFIX + ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
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
    public String submitAddCompanyArticlesWithString(@RequestParam String nameDatePressString, @RequestParam String linkString,
                                                     @RequestParam String subjectCompany, RedirectAttributes redirect, Model model) {
        String senderPage = ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage";
        if (companyService.findCompanyByName(subjectCompany).isEmpty()) {
            handleErrorForModel(NO_COMPANY_WITH_THAT_NAME, ADD_PROCESS_PATH, NOT_FOUND_COMPANY_ERROR, model);
            return senderPage;
        }
        List<List<String>> partialArticleLists = parseArticleString(nameDatePressString);
        List<String> linkList = parseLinkString(linkString);
        if (partialArticleLists.size() != linkList.size()) {
            handleErrorForModel(NOT_EQUAL_LIST_SIZE, ADD_PROCESS_PATH, INDEX_OUT_OF_BOUND_ERROR, model);
            return senderPage;
        }

        List<String> returnList = new ArrayList<>();
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        String receiverPage = URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX;
        try {
            for (int i = 0; i < linkList.size(); i++) {
                List<String> partialArticle = partialArticleLists.get(i);

                companyArticleDto.setName(partialArticle.get(0));
                companyArticleDto.setPress(partialArticle.get(4));
                companyArticleDto.setSubjectCompany(subjectCompany);
                companyArticleDto.setLink(linkList.get(i));
                companyArticleDto.setYear(parseInt(partialArticle.get(1)));
                companyArticleDto.setMonth(parseInt(partialArticle.get(2)));
                companyArticleDto.setDate(parseInt(partialArticle.get(3)));
                companyArticleDto.setImportance(0);

                BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(companyArticleDto, ARTICLE);
                defaultValidator.validate(companyArticleDto, bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ConstraintValidationException(CONSTRAINT_VALIDATION_VIOLATED, bindingResult, true);
                }
                fieldValidator.validate(companyArticleDto, bindingResult);
                objectSimpleValidator.validate(companyArticleDto, bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ConstraintValidationException(CONSTRAINT_VALIDATION_VIOLATED, bindingResult, false);
                }
                returnList.add(articleService.registerArticle(CompanyArticle.builder().articleDto(companyArticleDto).build()).getName());
            }
            handleForRedirect("", redirect, encodeUTF8(returnList), false, null);
        } catch (NumberFormatException e) {
            if (companyArticleDto.getImportance() == null ||
                    NUMBER_REGEX.matcher(String.valueOf(companyArticleDto.getImportance())).matches()) {
                handleForRedirect(e.getMessage(), redirect, encodeUTF8(returnList), false, NUMBER_FORMAT_LOCAL_DATE_ERROR);
            } else {
                handleForRedirect(e.getMessage(), redirect, encodeUTF8(returnList), false, NUMBER_FORMAT_INTEGER_ERROR);
            }
        } catch (ConstraintValidationException e) {
            handleForRedirect(CONSTRAINT_VALIDATION_VIOLATED + '\n' + e.getError(), redirect,
                    encodeUTF8(returnList), e.isBeanValidationViolated(), null);
        }
        return receiverPage;
    }

    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompanyArticlesWithString(@RequestParam List<String> nameList, Model model,
                                                     Boolean beanValidationError, String errorSingle) {
        model.addAttribute(nameListString, decodeUTF8(nameList));
        model.addAttribute(BEAN_VALIDATION_ERROR, beanValidationError);
        model.addAttribute(ERROR_SINGLE, errorSingle);
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
        CompanyArticleDto article = articleOrEmpty.orElseThrow().toDto();
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

    /**
     * Other private methods
     */
    // Handle Error
    private void handleErrorForModel(String logMessage, String layoutPath, String error, Model model) {
        log.error(ERRORS_ARE, logMessage);
        model.addAttribute(LAYOUT_PATH, layoutPath);
        model.addAttribute(ERROR, error);
    }

    private void handleForRedirect(String logMessage, RedirectAttributes redirect,
                                   List<String> nameListString, boolean beanValidationError, String errorSingle) {
        if (!logMessage.isEmpty()) {
            log.error(ERRORS_ARE, logMessage);
        }
        redirect.addAttribute(this.nameListString, nameListString);
        redirect.addAttribute(BEAN_VALIDATION_ERROR, beanValidationError);
        redirect.addAttribute(ERROR_SINGLE, errorSingle);
    }

    // Parse
    private List<List<String>> parseArticleString(String articleString) {
        List<String> dividedArticle = List.of(articleString.split("\\R"));
        List<List<String>> returnArticle = new ArrayList<>();

        for (int i = 0; i < dividedArticle.size(); i++) {
            if (i % 2 == 0) {
                returnArticle.add(new ArrayList<>(List.of(dividedArticle.get(i))));
            } else {
                returnArticle.getLast().addAll(List.of(dividedArticle.get(i)
                        .replaceAll("^\\(|\\)$", "").split(",\\s|-")));
                if (returnArticle.getLast().size() != 5) {
                    returnArticle.remove(i);
                    break;
                }
            }
        }
        return returnArticle;
    }

    private List<String> parseLinkString(String linkString) {
        return List.of(linkString.split("\\R"));
    }
}