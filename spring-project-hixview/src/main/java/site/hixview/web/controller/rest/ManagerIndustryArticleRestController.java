package site.hixview.web.controller.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hixview.domain.entity.article.SingleArticleErrorResponse;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.SingleArticleSuccessResponse;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.domain.validation.validator.IndustryArticleAddComplexValidator;
import site.hixview.domain.validation.validator.IndustryArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.IndustryArticleModifyValidator;

import java.util.*;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.Regex.MESSAGE_PATTERN;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_INDUSTRY_ARTICLE_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_INDUSTRY_ARTICLE_URL;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_INDUSTRY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE;
import static site.hixview.domain.vo.name.ExceptionName.BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.ViewName.VIEW_AFTER_PROCESS;
import static site.hixview.util.ControllerUtils.*;

@RestController
@RequiredArgsConstructor
public class ManagerIndustryArticleRestController {

    @Autowired
    private MessageSource source;

    private final IndustryArticleService articleService;

    private final Validator defaultValidator;
    private final IndustryArticleAddComplexValidator complexValidator;
    private final IndustryArticleAddSimpleValidator simpleValidator;
    private final IndustryArticleModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerIndustryArticleRestController.class);

    /**
     * Add - Single
     */
    @PostMapping(ADD_SINGLE_INDUSTRY_ARTICLE_URL)
    public ResponseEntity<?> submitAddIndustryArticle(@ModelAttribute(ARTICLE) @Validated IndustryArticleDto articleDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> returnedFieldErrorMap = new HashMap<>();
            Map<String, String> registeredFieldErrorMap = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String field = fieldError.getField();
                String defaultMessage = fieldError.getDefaultMessage();
                if (MESSAGE_PATTERN.matcher(Objects.requireNonNull(defaultMessage)).matches()) {
                    String substring = defaultMessage.substring(1, defaultMessage.length() - 1);
                    String[] splittedSubString = substring.split("\\.");
                    String errCode = splittedSubString[0];
                    if (registeredFieldErrorMap.containsKey(field)) {
                        compareAndProcessErrorHierarchy(registeredFieldErrorMap, field, errCode, splittedSubString, substring, returnedFieldErrorMap);
                    } else {
                        registeredFieldErrorMap.put(field, errCode);
                        defaultMessage = getDefaultMessage(errCode, splittedSubString, substring);
                        returnedFieldErrorMap.put(field, encodeWithUTF8(defaultMessage));
                    }
                } else {
                    returnedFieldErrorMap.put(field, encodeWithUTF8(defaultMessage));
                }
            }
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleArticleErrorResponse(ADD_PROCESS_LAYOUT, true, returnedFieldErrorMap));
        }

        complexValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrorMap = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                for (String code : Objects.requireNonNull(fieldError.getCodes())) {
                    try {
                        fieldErrorMap.put(fieldError.getField(),
                                encodeWithUTF8(source.getMessage(code, null, Locale.getDefault())));
                        break;
                    } catch (NoSuchMessageException ignored) {
                    }
                }
            }
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleArticleErrorResponse(ADD_PROCESS_LAYOUT, false, fieldErrorMap));
        }

        articleService.registerArticle(IndustryArticle.builder().articleDto(articleDto).build());
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON).body(
                new SingleArticleSuccessResponse(encodeWithUTF8(articleDto.getName()), ADD_SINGLE_INDUSTRY_ARTICLE_URL + FINISH_URL));
    }

    private void compareAndProcessErrorHierarchy(Map<String, String> registeredFieldErrorMap, String field, String errCode, String[] splittedSubString, String substring, Map<String, String> returnedFieldErrorMap) {
        String defaultMessage;
        int hierarchyDif = errorHierarchy.get(registeredFieldErrorMap.get(field)) - errorHierarchy.get(errCode);
        if (hierarchyDif > 0) {
            registeredFieldErrorMap.put(field, errCode);
            defaultMessage = getDefaultMessage(errCode, splittedSubString, substring);
            returnedFieldErrorMap.put(field, encodeWithUTF8(defaultMessage));
        } else if (hierarchyDif == 0) {
            throw new IllegalStateException("같은 수치의 오류 위계 구조를 비교하고 있습니다.");
        }
    }

    private String getDefaultMessage(String errCode, String[] splittedSubString, String substring) {
        if (errCode.equals("Range")) {
            return getDefaultMessageForRange(splittedSubString, substring);
        } else {
            return source.getMessage(substring, null, Locale.getDefault());
        }
    }

    private String getDefaultMessageForRange(String[] splittedSubString, String substring) {
        String dateTarget = source.getMessage(splittedSubString[2], null, Locale.getDefault());
        if (dateTarget.equals("연")) {
            return source.getMessage(substring, new Object[]{dateTarget, 2099, 1960}, Locale.getDefault());
        } else if (dateTarget.equals("월")) {
            return source.getMessage(substring, new Object[]{dateTarget, 12, 1}, Locale.getDefault());
        } else {
            return source.getMessage(substring, new Object[]{dateTarget, 31, 1}, Locale.getDefault());
        }
    }

    /**
     * Modify
     */
    @PostMapping(UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL)
    public String submitModifyIndustryArticle(@ModelAttribute(ARTICLE) @Validated IndustryArticleDto articleDto,
                                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            model.addAttribute("updateUrl", UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL);
            return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
        }

        modifyValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, null, model);
            model.addAttribute("updateUrl", UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL);
            return UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
        }

        articleService.correctArticle(IndustryArticle.builder().articleDto(articleDto).build());
        return REDIRECT_URL + fromPath(UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(articleDto.getName())).build().toUriString();
    }
}