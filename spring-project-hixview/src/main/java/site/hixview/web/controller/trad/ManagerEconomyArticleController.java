package site.hixview.web.controller.trad;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.error.ConstraintValidationException;
import site.hixview.domain.service.EconomyArticleService;
import site.hixview.domain.validation.validator.EconomyArticleAddSimpleValidator;
import site.hixview.util.ControllerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.ExceptionMessage.*;
import static site.hixview.domain.vo.Regex.NUMBER_PATTERN;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.domain.vo.name.ExceptionName.*;
import static site.hixview.domain.vo.name.ViewName.*;
import static site.hixview.util.ControllerUtils.*;
import static site.hixview.util.EnumUtils.*;
import static site.hixview.util.JsonUtils.serializeWithOneMap;

@Controller
@RequiredArgsConstructor
public class ManagerEconomyArticleController {

    private final EconomyArticleService articleService;

    private final Validator defaultValidator;
    private final EconomyArticleAddSimpleValidator simpleValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerEconomyArticleController.class);

    /**
     * Add - Single
     */
    @GetMapping(ADD_SINGLE_ECONOMY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddEconomyArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        model.addAttribute(ARTICLE, new EconomyArticleDto());
        return ADD_ECONOMY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
    }

    @GetMapping(ADD_SINGLE_ECONOMY_ARTICLE_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddEconomyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, ADD_SINGLE_ECONOMY_ARTICLE_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_ECONOMY_ARTICLE_VIEW + VIEW_SINGLE_FINISH;
    }

    /**
     * Add - Multiple
     */
    @GetMapping(ADD_ECONOMY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddEconomyArticlesWithString(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        return ADD_ECONOMY_ARTICLE_VIEW + VIEW_MULTIPLE_STRING_PROCESS;
    }

    @PostMapping(ADD_ECONOMY_ARTICLE_WITH_STRING_URL)
    public String submitAddEconomyArticlesWithString(@RequestParam String nameDatePressString,
                                                     @RequestParam String linkString,
                                                     @RequestParam String subjectCountry,
                                                     @RequestParam String targetEconomyContent,
                                                     RedirectAttributes redirect, Model model) {
        String senderPage = ADD_ECONOMY_ARTICLE_VIEW + VIEW_MULTIPLE_STRING_PROCESS;
        if (!inEnumConstants(Country.class, subjectCountry)) {
            finishForRollback(NO_SUBJECT_COUNTRY_WITH_THAT_VALUE, ADD_PROCESS_LAYOUT, NOT_FOUND_SUBJECT_COUNTRY_ERROR, model);
            return senderPage;
        }
        List<List<String>> nameDatePressList = parseArticleString(nameDatePressString);
        List<String> linkList = parseLinkString(linkString);
        if (nameDatePressList.size() != linkList.size()) {
            finishForRollback(NOT_EQUAL_LIST_SIZE, ADD_PROCESS_LAYOUT, INDEX_OUT_OF_BOUND_ERROR, model);
            return senderPage;
        }
        if (linkList.isEmpty()) {
            finishForRollback(EMPTY_ARTICLE, ADD_PROCESS_LAYOUT, NOT_BLANK_ARTICLE_ERROR, model);
            return senderPage;
        }

        List<String> nameList = new ArrayList<>();
        EconomyArticleDto articleDto = new EconomyArticleDto();
        String targetEconomyContentJson = serializeWithOneMap(new ObjectMapper(), TARGET_ECONOMY_CONTENT, List.of(targetEconomyContent));
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
                articleDto.setSubjectCountry(subjectCountry);
                articleDto.setTargetEconomyContents(targetEconomyContentJson);
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
                        EconomyArticle.builder().articleDto(articleDto).build()).getName());
            }
            finishForRedirect("", redirect, ControllerUtils.encodeWithUTF8(nameList),
                    false, null);
        } catch (NumberFormatException e) {
            if (articleDto.getImportance() == null ||
                    NUMBER_PATTERN.matcher(String.valueOf(articleDto.getImportance())).matches()) {
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
        return REDIRECT_URL + ADD_ECONOMY_ARTICLE_WITH_STRING_URL + FINISH_URL;
    }

    @GetMapping(ADD_ECONOMY_ARTICLE_WITH_STRING_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddEconomyArticlesWithString(@RequestParam List<String> nameList, Model model,
                                                     Boolean isBeanValidationError, String errorSingle) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, ADD_ECONOMY_ARTICLE_WITH_STRING_URL);
        model.addAttribute(NAME_LIST, ControllerUtils.decodeWithUTF8(nameList));
        model.addAttribute(IS_BEAN_VALIDATION_ERROR, isBeanValidationError);
        model.addAttribute(ERROR_SINGLE, errorSingle);
        return ADD_ECONOMY_ARTICLE_VIEW + VIEW_MULTIPLE_FINISH;
    }

    /**
     * See
     */
    @GetMapping(SELECT_ECONOMY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeEconomyArticles(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute(ARTICLES, articleService.findArticles());
        return SELECT_VIEW + "economy-articles-page";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_ECONOMY_ARTICLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public String initiateModifyEconomyArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT);
		return UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS;
	}

    @PostMapping(UPDATE_ECONOMY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyEconomyArticle(@RequestParam String numberOrName, Model model) {
        Optional<EconomyArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_ECONOMY_ARTICLE_WITH_THAT_NUMBER_OR_NAME, UPDATE_QUERY_LAYOUT, NOT_FOUND_ECONOMY_ARTICLE_ERROR, model);
            return UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
        model.addAttribute(UPDATE_URL, UPDATE_ECONOMY_ARTICLE_URL + FINISH_URL);
        model.addAttribute(ARTICLE, articleOrEmpty.orElseThrow().toDto());
        return UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
    }

    @GetMapping(UPDATE_ECONOMY_ARTICLE_URL + FINISH_URL)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyEconomyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, UPDATE_ECONOMY_ARTICLE_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_FINISH;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_ECONOMY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidEconomyArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT);
        return REMOVE_ECONOMY_ARTICLE_VIEW + VIEW_PROCESS;
    }

    @PostMapping(REMOVE_ECONOMY_ARTICLE_URL)
    public String submitRidEconomyArticle(@RequestParam String numberOrName, Model model) {
        Optional<EconomyArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_ECONOMY_ARTICLE_WITH_THAT_NUMBER_OR_NAME, REMOVE_PROCESS_LAYOUT, NOT_FOUND_ECONOMY_ARTICLE_ERROR, model);
            return REMOVE_ECONOMY_ARTICLE_VIEW + VIEW_PROCESS;
        }

        if (NUMBER_PATTERN.matcher(numberOrName).matches()) {
            numberOrName = articleService.findArticleByNumber(Long.parseLong(numberOrName)).orElseThrow().getName();
        }
        articleService.removeArticleByName(numberOrName);
        return REDIRECT_URL + fromPath(REMOVE_ECONOMY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(numberOrName)).build().toUriString();
    }

    @GetMapping(REMOVE_ECONOMY_ARTICLE_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidEconomyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, REMOVE_ECONOMY_ARTICLE_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_ECONOMY_ARTICLE_VIEW + VIEW_FINISH;
    }
}