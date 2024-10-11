package site.hixview.web.controller;

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
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.error.ConstraintValidationException;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.CompanyArticleAddComplexValidator;
import site.hixview.domain.validation.validator.CompanyArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.CompanyArticleModifyValidator;
import site.hixview.util.ControllerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE;
import static site.hixview.domain.vo.ExceptionMessage.*;
import static site.hixview.domain.vo.name.ExceptionName.*;
import static site.hixview.domain.vo.Regex.NUMBER_PATTERN;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.name.ViewName.*;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.util.ControllerUtils.*;
import static site.hixview.util.EnumUtils.convertToEnum;
import static site.hixview.util.EnumUtils.inEnumValues;

@Controller
@RequiredArgsConstructor
public class ManagerCompanyArticleController {

    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    private final Validator defaultValidator;
    private final CompanyArticleAddComplexValidator complexValidator;
    private final CompanyArticleAddSimpleValidator simpleValidator;
    private final CompanyArticleModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerCompanyArticleController.class);

    /**
     * Add - Single
     */
    @GetMapping(ADD_SINGLE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddCompanyArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        model.addAttribute(ARTICLE, new CompanyArticleDto());
        return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
    }

    @PostMapping(ADD_SINGLE_COMPANY_ARTICLE_URL)
    public String submitAddCompanyArticle(@ModelAttribute(ARTICLE) @Validated CompanyArticleDto articleDto,
                                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
        }

        complexValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, null, model);
            return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
        }

        articleService.registerArticle(CompanyArticle.builder().articleDto(articleDto).build());
        return REDIRECT_URL + fromPath(ADD_SINGLE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(articleDto.getName())).build().toUriString();
    }

    @GetMapping(ADD_SINGLE_COMPANY_ARTICLE_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_FINISH;
    }

    /**
     * Add - Multiple
     */
    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddCompanyArticlesWithString(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        return ADD_COMPANY_ARTICLE_VIEW + VIEW_MULTIPLE_STRING_PROCESS;
    }

    @PostMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL)
    public String submitAddCompanyArticlesWithString(@RequestParam String nameDatePressString, @RequestParam String linkString,
                                                     @RequestParam String subjectCompany, RedirectAttributes redirect, Model model) {
        String senderPage = ADD_COMPANY_ARTICLE_VIEW + VIEW_MULTIPLE_STRING_PROCESS;
        if (companyService.findCompanyByName(subjectCompany).isEmpty()) {
            finishForRollback(NO_COMPANY_WITH_THAT_NAME, ADD_PROCESS_LAYOUT, NOT_FOUND_COMPANY_ERROR, model);
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
        CompanyArticleDto articleDto = new CompanyArticleDto();
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
                articleDto.setSubjectCompany(subjectCompany);
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
                        CompanyArticle.builder().articleDto(articleDto).build()).getName());
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
        return REDIRECT_URL + ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL;
    }

    @GetMapping(ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddCompanyArticlesWithString(@RequestParam List<String> nameList, Model model,
                                                     Boolean isBeanValidationError, String errorSingle) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute("nameList", ControllerUtils.decodeWithUTF8(nameList));
        model.addAttribute(IS_BEAN_VALIDATION_ERROR, isBeanValidationError);
        model.addAttribute(ERROR_SINGLE, errorSingle);
        return ADD_COMPANY_ARTICLE_VIEW + VIEW_MULTIPLE_FINISH;
    }

    /**
     * See
     */
    @GetMapping(SELECT_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeCompanyArticles(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute("articles", articleService.findArticles());
        return SELECT_VIEW + "company-articles-page";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_COMPANY_ARTICLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public String initiateModifyCompanyArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
		return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS;
	}

    @PostMapping(UPDATE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyCompanyArticle(@RequestParam String numberOrName, Model model) {
        Optional<CompanyArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_COMPANY_ARTICLE_WITH_THAT_NUMBER_OR_NAME, UPDATE_PROCESS_LAYOUT, NOT_FOUND_COMPANY_ARTICLE_ERROR, model);
            return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
        model.addAttribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + FINISH_URL);
        model.addAttribute(ARTICLE, articleOrEmpty.orElseThrow().toDto());
        return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
    }

    @PostMapping(UPDATE_COMPANY_ARTICLE_URL + FINISH_URL)
    public String submitModifyCompanyArticle(@ModelAttribute(ARTICLE) @Validated CompanyArticleDto articleDto,
                                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + FINISH_URL);
            return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
        }

        modifyValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, null, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + FINISH_URL);
            return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
        }

        articleService.correctArticle(CompanyArticle.builder().articleDto(articleDto).build());
        return REDIRECT_URL + fromPath(UPDATE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(articleDto.getName())).build().toUriString();
    }

    @GetMapping(UPDATE_COMPANY_ARTICLE_URL + FINISH_URL)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_FINISH;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_COMPANY_ARTICLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidCompanyArticle(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT);
        return REMOVE_COMPANY_URL_ARTICLE_VIEW + VIEW_PROCESS;
    }

    @PostMapping(REMOVE_COMPANY_ARTICLE_URL)
    public String submitRidCompanyArticle(@RequestParam String numberOrName, Model model) {
        Optional<CompanyArticle> articleOrEmpty = articleService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_COMPANY_ARTICLE_WITH_THAT_NUMBER_OR_NAME, REMOVE_PROCESS_LAYOUT, NOT_FOUND_COMPANY_ARTICLE_ERROR, model);
            return REMOVE_COMPANY_URL_ARTICLE_VIEW + VIEW_PROCESS;
        }

        if (NUMBER_PATTERN.matcher(numberOrName).matches()) {
            numberOrName = articleService.findArticleByNumber(Long.parseLong(numberOrName)).orElseThrow().getName();
        }
        articleService.removeArticleByName(numberOrName);
        return REDIRECT_URL + fromPath(REMOVE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(numberOrName)).build().toUriString();
    }

    @GetMapping(REMOVE_COMPANY_ARTICLE_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidCompanyArticle(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_COMPANY_URL_ARTICLE_VIEW + VIEW_FINISH;
    }
}