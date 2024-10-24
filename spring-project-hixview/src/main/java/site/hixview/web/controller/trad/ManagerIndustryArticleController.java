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
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.error.ConstraintValidationException;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.domain.validation.validator.IndustryArticleAddSimpleValidator;
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
import static site.hixview.util.JsonUtils.deserializeWithOneMapToList;
import static site.hixview.util.JsonUtils.serializeWithOneMap;

@Controller
@RequiredArgsConstructor
public class ManagerIndustryArticleController {

    private final IndustryArticleService articleService;

    private final Validator defaultValidator;
    private final IndustryArticleAddSimpleValidator simpleValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerIndustryArticleController.class);

    /**
     * Add - Single
     */
    @GetMapping(ADD_SINGLE_INDUSTRY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddIndustryArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        model.addAttribute(ARTICLE, new IndustryArticleDto());
        return ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
    }

    @GetMapping(ADD_SINGLE_INDUSTRY_ARTICLE_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddIndustryArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, ADD_SINGLE_INDUSTRY_ARTICLE_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_FINISH;
    }

    /**
     * Add - Multiple
     */
    @GetMapping(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddIndustryArticlesWithString(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        return ADD_INDUSTRY_ARTICLE_VIEW + VIEW_MULTIPLE_STRING_PROCESS;
    }

    @PostMapping(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL)
    public String submitAddIndustryArticlesWithString(@RequestParam String nameDatePressString,
                                                      @RequestParam String linkString,
                                                     @RequestParam String subjectFirstCategory,
                                                     @RequestParam String subjectSecondCategory,
                                                     RedirectAttributes redirect, Model model) {
        String senderPage = ADD_INDUSTRY_ARTICLE_VIEW + VIEW_MULTIPLE_STRING_PROCESS;
        if (!inEnumConstants(FirstCategory.class, subjectFirstCategory)) {
            finishForRollback(NO_FIRST_CATEGORY_WITH_THAT_VALUE, ADD_PROCESS_LAYOUT, NOT_FOUND_FIRST_CATEGORY_ERROR, model);
            return senderPage;
        }
        String subjectSecondCategoryJson = serializeWithOneMap(new ObjectMapper(), SUBJECT_SECOND_CATEGORY, List.of(subjectSecondCategory));
        String secondCategory = deserializeWithOneMapToList(new ObjectMapper(), SUBJECT_SECOND_CATEGORY, subjectSecondCategoryJson).getFirst();
        if (!inEnumConstants(SecondCategory.class, secondCategory)) {
            finishForRollback(NO_SECOND_CATEGORY_WITH_THAT_VALUE, ADD_PROCESS_LAYOUT, NOT_FOUND_SECOND_CATEGORY_ERROR, model);
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
                articleDto.setSubjectSecondCategories(subjectSecondCategoryJson);
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
        return REDIRECT_URL + ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + FINISH_URL;
    }

    @GetMapping(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddIndustryArticlesWithString(@RequestParam List<String> nameList, Model model,
                                                     Boolean isBeanValidationError, String errorSingle) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, ADD_INDUSTRY_ARTICLE_WITH_STRING_URL);
        model.addAttribute(NAME_LIST, ControllerUtils.decodeWithUTF8(nameList));
        model.addAttribute(IS_BEAN_VALIDATION_ERROR, isBeanValidationError);
        model.addAttribute(ERROR_SINGLE, errorSingle);
        return ADD_INDUSTRY_ARTICLE_VIEW + VIEW_MULTIPLE_FINISH;
    }

    /**
     * See
     */
    @GetMapping(SELECT_INDUSTRY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeIndustryArticles(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute(ARTICLES, articleService.findArticles());
        model.addAttribute("secondCategoryMap", getMapNameKorean(SecondCategory.class));
        return SELECT_VIEW + "industry-articles-page";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_INDUSTRY_ARTICLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public String initiateModifyIndustryArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT);
		return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS;
	}

    @PostMapping(UPDATE_INDUSTRY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyIndustryArticle(@RequestParam String numberOrName, Model model) {
        Optional<IndustryArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_INDUSTRY_ARTICLE_WITH_THAT_NUMBER_OR_NAME, UPDATE_PROCESS_LAYOUT, NOT_FOUND_INDUSTRY_ARTICLE_ERROR, model);
            return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
        model.addAttribute(UPDATE_URL, UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL);
        model.addAttribute(ARTICLE, articleOrEmpty.orElseThrow().toDto());
        return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
    }

    @GetMapping(UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyIndustryArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, UPDATE_INDUSTRY_ARTICLE_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_FINISH;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_INDUSTRY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidIndustryArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT);
        return REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS;
    }

    @PostMapping(REMOVE_INDUSTRY_ARTICLE_URL)
    public String submitRidIndustryArticle(@RequestParam String numberOrName, Model model) {
        Optional<IndustryArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_INDUSTRY_ARTICLE_WITH_THAT_NUMBER_OR_NAME, REMOVE_PROCESS_LAYOUT, NOT_FOUND_INDUSTRY_ARTICLE_ERROR, model);
            return REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS;
        }

        if (NUMBER_PATTERN.matcher(numberOrName).matches()) {
            numberOrName = articleService.findArticleByNumber(Long.parseLong(numberOrName)).orElseThrow().getName();
        }
        articleService.removeArticleByName(numberOrName);
        return REDIRECT_URL + fromPath(REMOVE_INDUSTRY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(numberOrName)).build().toUriString();
    }

    @GetMapping(REMOVE_INDUSTRY_ARTICLE_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidIndustryArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, REMOVE_INDUSTRY_ARTICLE_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_FINISH;
    }
}