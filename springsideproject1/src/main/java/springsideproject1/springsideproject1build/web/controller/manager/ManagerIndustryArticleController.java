package springsideproject1.springsideproject1build.web.controller.manager;

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
import springsideproject1.springsideproject1build.domain.entity.FirstCategory;
import springsideproject1.springsideproject1build.domain.entity.Press;
import springsideproject1.springsideproject1build.domain.entity.SecondCategory;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleDto;
import springsideproject1.springsideproject1build.domain.error.ConstraintValidationException;
import springsideproject1.springsideproject1build.domain.service.IndustryArticleService;
import springsideproject1.springsideproject1build.domain.validation.validator.article.industry.IndustryArticleAddComplexValidator;
import springsideproject1.springsideproject1build.domain.validation.validator.article.industry.IndustryArticleAddSimpleValidator;
import springsideproject1.springsideproject1build.domain.validation.validator.article.industry.IndustryArticleModifyValidator;
import springsideproject1.springsideproject1build.util.ControllerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static springsideproject1.springsideproject1build.domain.vo.EXCEPTION_MESSAGE.*;
import static springsideproject1.springsideproject1build.domain.vo.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.vo.REGEX.NUMBER_REGEX_PATTERN;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.vo.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.vo.WORD.NAME;
import static springsideproject1.springsideproject1build.domain.vo.WORD.VALUE;
import static springsideproject1.springsideproject1build.util.ControllerUtils.*;
import static springsideproject1.springsideproject1build.util.EnumUtils.*;

@Controller
@RequiredArgsConstructor
public class ManagerIndustryArticleController {

    private final IndustryArticleService articleService;

    private final Validator defaultValidator;
    private final IndustryArticleAddComplexValidator complexValidator;
    private final IndustryArticleAddSimpleValidator simpleValidator;
    private final IndustryArticleModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerIndustryArticleController.class);

    /**
     * Add - Single
     */
    @GetMapping(ADD_SINGLE_INDUSTRY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddIndustryArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
        model.addAttribute(ARTICLE, new IndustryArticleDto());
        return ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
    }

    @PostMapping(ADD_SINGLE_INDUSTRY_ARTICLE_URL)
    public String submitAddIndustryArticle(@ModelAttribute(ARTICLE) @Validated IndustryArticleDto articleDto,
                                          BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, BEAN_VALIDATION_ERROR, model);
            return ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
        }

        complexValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, null, model);
            return ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
        }

        articleService.registerArticle(IndustryArticle.builder().articleDto(articleDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(articleDto.getName()));
        return URL_REDIRECT_PREFIX + ADD_SINGLE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(ADD_SINGLE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddIndustryArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_FINISH_SUFFIX;
    }

    /**
     * Add - Multiple
     */
    @GetMapping(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddIndustryArticlesWithString(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
        return ADD_INDUSTRY_ARTICLE_VIEW + "multiple-string-process-page";
    }

    @PostMapping(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL)
    public String submitAddIndustryArticlesWithString(@RequestParam String nameDatePressString, @RequestParam String linkString,
                                                     @RequestParam String subjectFirstCategory,
                                                     @RequestParam String subjectSecondCategory,
                                                     RedirectAttributes redirect, Model model) {
        String senderPage = ADD_INDUSTRY_ARTICLE_VIEW + "multiple-string-process-page";
        if (!inEnumConstants(FirstCategory.class, subjectFirstCategory)) {
            finishForRollback(NO_FIRST_CATEGORY_WITH_THAT_VALUE, ADD_PROCESS_PATH, NOT_FOUND_FIRST_CATEGORY_ERROR, model);
            return senderPage;
        }
        if (!inEnumConstants(SecondCategory.class, subjectSecondCategory)) {
            finishForRollback(NO_SECOND_CATEGORY_WITH_THAT_VALUE, ADD_PROCESS_PATH, NOT_FOUND_SECOND_CATEGORY_ERROR, model);
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
        IndustryArticleDto articleDto = new IndustryArticleDto();
        try {
            for (int i = 0; i < linkList.size(); i++) {
                List<String> partialArticle = nameDatePressList.get(i);

                articleDto.setName(partialArticle.get(0).strip());
                articleDto.setPress(partialArticle.get(4).toUpperCase());
                articleDto.setLink(linkList.get(i));
                articleDto.setYear(parseInt(partialArticle.get(1)));
                articleDto.setMonth(parseInt(partialArticle.get(2)));
                articleDto.setDays(parseInt(partialArticle.get(3)));
                articleDto.setImportance(0);
                articleDto.setSubjectFirstCategory(subjectFirstCategory);
                articleDto.setSubjectSecondCategory(subjectSecondCategory);
                if (inEnumValues(Press.class, articleDto.getPress()))
                    articleDto.setPress(convertToEnum(Press.class, articleDto.getPress()).name());

                BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(articleDto, ARTICLE);
                defaultValidator.validate(articleDto, bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ConstraintValidationException(CONSTRAINT_VALIDATION_VIOLATED, bindingResult, true);
                }
                simpleValidator.validate(articleDto, bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ConstraintValidationException(CONSTRAINT_VALIDATION_VIOLATED, bindingResult, false);
                }
                nameList.add(articleService.registerArticle(
                        IndustryArticle.builder().articleDto(articleDto).build()).getName());
            }
            finishForRedirect("", redirect, ControllerUtils.encodeWithUTF8(nameList),
                    false, null);
        } catch (NumberFormatException e) {
            if (articleDto.getImportance() == null ||
                    NUMBER_REGEX_PATTERN.matcher(String.valueOf(articleDto.getImportance())).matches()) {
                finishForRedirect(e.getMessage(), redirect, ControllerUtils.encodeWithUTF8(nameList),
                        false, NUMBER_FORMAT_LOCAL_DATE_ERROR);
            } else {
                finishForRedirect(e.getMessage(), redirect, ControllerUtils.encodeWithUTF8(nameList),
                        false, NUMBER_FORMAT_INTEGER_ERROR);
            }
        } catch (ConstraintValidationException e) {
            finishForRedirect(CONSTRAINT_VALIDATION_VIOLATED + '\n' + e.getError(), redirect,
                    ControllerUtils.encodeWithUTF8(nameList), e.isBeanValidationViolated(), null);
        }
        return URL_REDIRECT_PREFIX + ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddIndustryArticlesWithString(@RequestParam List<String> nameList, Model model,
                                                     Boolean isBeanValidationError, String errorSingle) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute("nameList", ControllerUtils.decodeWithUTF8(nameList));
        model.addAttribute(IS_BEAN_VALIDATION_ERROR, isBeanValidationError);
        model.addAttribute(ERROR_SINGLE, errorSingle);
        return ADD_INDUSTRY_ARTICLE_VIEW + "multiple-finish-page";
    }

    /**
     * See
     */
    @GetMapping(SELECT_INDUSTRY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeIndustryArticles(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_PATH);
        model.addAttribute("articles", articleService.findArticles());
        return MANAGER_SELECT_VIEW + "industry-articles-page";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_INDUSTRY_ARTICLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public String initiateModifyIndustryArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
		return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
	}

    @PostMapping(UPDATE_INDUSTRY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyIndustryArticle(@RequestParam String numberOrName, Model model) {
        Optional<IndustryArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_INDUSTRY_ARTICLE_WITH_THAT_NUMBER_OR_NAME, UPDATE_PROCESS_PATH, NOT_FOUND_INDUSTRY_ARTICLE_ERROR, model);
            return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
        model.addAttribute("updateUrl", UPDATE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX);
        model.addAttribute(ARTICLE, articleOrEmpty.orElseThrow().toDto());
        return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX)
    public String submitModifyIndustryArticle(@ModelAttribute(ARTICLE) @Validated IndustryArticleDto articleDto,
                                             BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_PATH, BEAN_VALIDATION_ERROR, model);
            model.addAttribute("updateUrl", UPDATE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX);
            return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
        }

        modifyValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_PATH, null, model);
            model.addAttribute("updateUrl", UPDATE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX);
            return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
        }

        articleService.correctArticle(IndustryArticle.builder().articleDto(articleDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(articleDto.getName()));
        return URL_REDIRECT_PREFIX + UPDATE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(UPDATE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyIndustryArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_INDUSTRY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidIndustryArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_PATH);
        return REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(REMOVE_INDUSTRY_ARTICLE_URL)
    public String submitRidIndustryArticle(RedirectAttributes redirect, @RequestParam String numberOrName, Model model) {
        Optional<IndustryArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_INDUSTRY_ARTICLE_WITH_THAT_NUMBER_OR_NAME, REMOVE_PROCESS_PATH, NOT_FOUND_INDUSTRY_ARTICLE_ERROR, model);
            return REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS_SUFFIX;
        }

        if (NUMBER_REGEX_PATTERN.matcher(numberOrName).matches()) {
            numberOrName = articleService.findArticleByNumber(Long.parseLong(numberOrName)).orElseThrow().getName();
        }
        articleService.removeArticleByName(numberOrName);
        redirect.addAttribute(NAME, encodeWithUTF8(numberOrName));
        return URL_REDIRECT_PREFIX + REMOVE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(REMOVE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidIndustryArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_FINISH_SUFFIX;
    }
}