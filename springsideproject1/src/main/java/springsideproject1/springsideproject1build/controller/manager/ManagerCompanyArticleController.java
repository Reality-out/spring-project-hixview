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
import springsideproject1.springsideproject1build.domain.validator.article.CompanyArticleDtoConstraintValidator;
import springsideproject1.springsideproject1build.domain.validator.article.CompanyArticleDtoLinkValidator;
import springsideproject1.springsideproject1build.domain.validator.article.CompanyArticleDtoNameValidator;
import springsideproject1.springsideproject1build.domain.validator.article.CompanyArticleDtoSubjectCompanyValidator;
import springsideproject1.springsideproject1build.util.MainUtils;

import java.util.*;

import static java.lang.Integer.parseInt;
import static springsideproject1.springsideproject1build.domain.entity.article.Press.*;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.*;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.LINK;
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
public class ManagerCompanyArticleController {

    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    private final Validator defaultValidator;
    private final CompanyArticleDtoConstraintValidator constraintValidator;
    private final CompanyArticleDtoNameValidator nameValidator;
    private final CompanyArticleDtoLinkValidator linkValidator;
    private final CompanyArticleDtoSubjectCompanyValidator subjectCompanyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerCompanyArticleController.class);

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
    public String submitAddCompanyArticle(@ModelAttribute(ARTICLE) @Validated CompanyArticleDto articleDto,
                                          BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (articleDto.getPress() != null)
            articleDto.setPress(articleDto.getPress().toUpperCase());
        if (processBindingError(bindingResult, ADD_PROCESS_PATH, model) ||
                processValidationErrorAdd(articleDto, bindingResult, model))
            return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
        checkAndConvertForKoreanEnum(articleDto);
        articleService.registerArticle(CompanyArticle.builder().articleDto(articleDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(articleDto.getName()));
        return URL_REDIRECT_PREFIX + ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_FINISH_SUFFIX;
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
    public String submitAddCompanyArticlesWithString(@RequestParam String nameDatePressString, @RequestParam String linkString,
                                                     @RequestParam String subjectCompany, RedirectAttributes redirect, Model model) {
        String senderPage = ADD_COMPANY_ARTICLE_VIEW + "multipleStringProcessPage";
        if (companyService.findCompanyByName(subjectCompany).isEmpty()) {
            finishForRollback(NO_COMPANY_WITH_THAT_NAME, ADD_PROCESS_PATH, NOT_FOUND_COMPANY_ERROR, model);
            return senderPage;
        }
        List<List<String>> nameDatePressList = parseArticleString(nameDatePressString);
        List<String> linkList = parseLinkString(linkString);
        if (nameDatePressList.size() != linkList.size()) {
            finishForRollback(NOT_EQUAL_LIST_SIZE, ADD_PROCESS_PATH, INDEX_OUT_OF_BOUND_ERROR, model);
            return senderPage;
        }
        if (linkList.isEmpty()) {
            finishForRollback(EMPTY_ARTICLE, ADD_PROCESS_PATH, NOT_BLANK_ARTICLE_ERROR, model);
            return senderPage;
        }

        List<String> nameList = new ArrayList<>();
        CompanyArticleDto articleDto = new CompanyArticleDto();
        try {
            for (int i = 0; i < linkList.size(); i++) {
                List<String> partialArticle = nameDatePressList.get(i);

                articleDto.setName(partialArticle.get(0));
                articleDto.setPress(partialArticle.get(4).toUpperCase());
                articleDto.setSubjectCompany(subjectCompany);
                articleDto.setLink(linkList.get(i));
                articleDto.setYear(parseInt(partialArticle.get(1)));
                articleDto.setMonth(parseInt(partialArticle.get(2)));
                articleDto.setDays(parseInt(partialArticle.get(3)));
                articleDto.setImportance(0);

                BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(articleDto, ARTICLE);
                defaultValidator.validate(articleDto, bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ConstraintValidationException(CONSTRAINT_VALIDATION_VIOLATED, bindingResult, true);
                }
                constraintValidator.validate(articleDto, bindingResult);
                nameValidator.validate(articleDto, bindingResult);
                linkValidator.validate(articleDto, bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ConstraintValidationException(CONSTRAINT_VALIDATION_VIOLATED, bindingResult, false);
                }
                checkAndConvertForKoreanEnum(articleDto);
                nameList.add(articleService.registerArticle(
                        CompanyArticle.builder().articleDto(articleDto).build()).getName());
            }
            finishForRedirect("", redirect, MainUtils.encodeWithUTF8(nameList),
                    false, null);
        } catch (NumberFormatException e) {
            if (articleDto.getImportance() == null ||
                    NUMBER_REGEX_PATTERN.matcher(String.valueOf(articleDto.getImportance())).matches()) {
                finishForRedirect(e.getMessage(), redirect, MainUtils.encodeWithUTF8(nameList),
                        false, NUMBER_FORMAT_LOCAL_DATE_ERROR);
            } else {
                finishForRedirect(e.getMessage(), redirect, MainUtils.encodeWithUTF8(nameList),
                        false, NUMBER_FORMAT_INTEGER_ERROR);
            }
        } catch (ConstraintValidationException e) {
            finishForRedirect(CONSTRAINT_VALIDATION_VIOLATED + '\n' + e.getError(), redirect,
                    MainUtils.encodeWithUTF8(nameList), e.isBeanValidationViolated(), null);
        }
        return URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompanyArticlesWithString(@RequestParam List<String> nameList, Model model,
                                                     Boolean isBeanValidationError, String errorSingle) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute("nameList", MainUtils.decodeWithUTF8(nameList));
        model.addAttribute(IS_BEAN_VALIDATION_ERROR, isBeanValidationError);
        model.addAttribute(ERROR_SINGLE, errorSingle);
        return ADD_COMPANY_ARTICLE_VIEW + "multipleFinishPage";
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
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
        model.addAttribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX);
        model.addAttribute(ARTICLE, articleOrEmpty.orElseThrow().toDto());
        return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    public String submitModifyCompanyArticle(@ModelAttribute(ARTICLE) @Validated CompanyArticleDto articleDto,
                                             BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (articleDto.getPress() != null)
            articleDto.setPress(articleDto.getPress().toUpperCase());
        if (processBindingError(bindingResult, UPDATE_PROCESS_PATH, model) ||
                processValidationErrorModify(articleDto, bindingResult, model)) {
            model.addAttribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX);
            return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
        }
        checkAndConvertForKoreanEnum(articleDto);
        articleService.correctArticle(CompanyArticle.builder().articleDto(articleDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(articleDto.getName()));
        return URL_REDIRECT_PREFIX + UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidCompanyArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_PATH);
        return REMOVE_COMPANY_ARTICLE_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(REMOVE_COMPANY_ARTICLE_URL)
    public String submitRidCompanyArticle(RedirectAttributes redirect, @RequestParam String numberOrName, Model model) {
        Optional<CompanyArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            log.error(ERRORS_ARE, NO_ARTICLE_WITH_THAT_NUMBER_OR_NAME);
            model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_PATH);
            model.addAttribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR);
            return REMOVE_COMPANY_ARTICLE_VIEW + VIEW_PROCESS_SUFFIX;
        }
        if (NUMBER_REGEX_PATTERN.matcher(numberOrName).matches()) {
            String removedName = articleService.findArticleByNumber(Long.parseLong(numberOrName)).orElseThrow().getName();
            articleService.removeArticleByName(removedName);
            redirect.addAttribute(NAME, encodeWithUTF8(removedName));
        } else {
            articleService.removeArticleByName(numberOrName);
            redirect.addAttribute(NAME, encodeWithUTF8(numberOrName));
        }
        return URL_REDIRECT_PREFIX + REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(REMOVE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_COMPANY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX;
    }

    /**
     * Other private methods
     */
    // Check
    private void checkAndConvertForKoreanEnum(CompanyArticleDto articleDto) {
        if (containsWithPressValue(articleDto.getPress()))
            articleDto.setPress(convertToPress(articleDto.getPress()).name());
    }

    // Handle Error
    private boolean processBindingError(BindingResult bindingResult, String layoutPath, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), layoutPath, BEAN_VALIDATION_ERROR, model);
            return true;
        }
        return false;
    }

    private boolean processValidationErrorAdd(CompanyArticleDto articleDto, BindingResult bindingResult, Model model) {
        constraintValidator.validate(articleDto, bindingResult);
        nameValidator.validate(articleDto, bindingResult);
        linkValidator.validate(articleDto, bindingResult);
        subjectCompanyValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, null, model);
            return true;
        }
        return false;
    }

    private boolean processValidationErrorModify(CompanyArticleDto articleDto, BindingResult bindingResult, Model model) {
        constraintValidator.validate(articleDto, bindingResult);
        subjectCompanyValidator.validate(articleDto, bindingResult);
        if (articleService.findArticleByName(articleDto.getName()).isEmpty()) {
            bindingResult.rejectValue(NAME, "NotFound");
        }
        if (articleService.findArticleByLink(articleDto.getLink()).isEmpty()) {
            bindingResult.rejectValue(LINK, "NotFound");
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
        model.addAttribute(ERROR, error);
    }

    private void finishForRedirect(String logMessage, RedirectAttributes redirect,
                                   List<String> nameListString, boolean isBeanValidationError, String errorSingle) {
        if (!logMessage.isEmpty()) {
            log.error(ERRORS_ARE, logMessage);
        }
        redirect.addAttribute("nameList", nameListString);
        redirect.addAttribute(IS_BEAN_VALIDATION_ERROR, isBeanValidationError);
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
        if (returnArticle.getLast().size() != 5) {
            returnArticle.removeLast();
        }
        return returnArticle;
    }

    private List<String> parseLinkString(String linkString) {
        if (linkString.isEmpty()) {
            return Collections.emptyList();
        }
        return List.of(linkString.split("\\R"));
    }
}