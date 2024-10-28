package site.hixview.web.controller.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.entity.article.response.SingleErrorBeanResponse;
import site.hixview.domain.entity.article.response.SingleSuccessResponse;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.domain.validation.validator.IndustryArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.IndustryArticleModifyValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static site.hixview.domain.vo.Regex.MESSAGE_PATTERN;
import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.ARTICLE;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestPath.ADD_SINGLE_INDUSTRY_ARTICLE_PATH;
import static site.hixview.domain.vo.manager.RequestPath.UPDATE_INDUSTRY_ARTICLE_PATH;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;
import static site.hixview.util.RestControllerUtils.processMessagePatternString;
import static site.hixview.util.RestControllerUtils.getMapWithNameMessageFromProperty;

@RestController
@RequiredArgsConstructor
public class ManagerIndustryArticleRestController {

    @Autowired
    private MessageSource source;

    private final IndustryArticleService articleService;

    private final IndustryArticleAddSimpleValidator simpleValidator;
    private final IndustryArticleModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerIndustryArticleRestController.class);

    /**
     * Add - Single
     */
    @PostMapping(ADD_SINGLE_INDUSTRY_ARTICLE_PATH)
    public ResponseEntity<?> submitAddIndustryArticle(@ModelAttribute(ARTICLE) @Validated IndustryArticleDto articleDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> returnedFieldErrorMap = new HashMap<>();
            Map<String, String> registeredFieldErrorMap = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String field = fieldError.getField();
                String defaultMessage = fieldError.getDefaultMessage();
                if (MESSAGE_PATTERN.matcher(Objects.requireNonNull(defaultMessage)).matches()) {
                    processMessagePatternString(source, defaultMessage, registeredFieldErrorMap, field, returnedFieldErrorMap);
                } else {
                    returnedFieldErrorMap.put(field, encodeWithUTF8(defaultMessage));
                }
            }
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleErrorBeanResponse(ADD_PROCESS_LAYOUT, true, returnedFieldErrorMap));
        }
        simpleValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrorMap = getMapWithNameMessageFromProperty(source, bindingResult);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleErrorBeanResponse(ADD_PROCESS_LAYOUT, false, fieldErrorMap));
        }
        articleService.registerArticle(IndustryArticle.builder().articleDto(articleDto).build());
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON).body(
                new SingleSuccessResponse(encodeWithUTF8(articleDto.getName()), ADD_SINGLE_INDUSTRY_ARTICLE_PATH + FINISH_PATH));
    }

    /**
     * Modify
     */
    @PostMapping(UPDATE_INDUSTRY_ARTICLE_PATH + FINISH_PATH)
    public ResponseEntity<?> submitModifyIndustryArticle(@ModelAttribute(ARTICLE) @Validated IndustryArticleDto articleDto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> returnedFieldErrorMap = new HashMap<>();
            Map<String, String> registeredFieldErrorMap = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String field = fieldError.getField();
                String defaultMessage = fieldError.getDefaultMessage();
                if (MESSAGE_PATTERN.matcher(Objects.requireNonNull(defaultMessage)).matches()) {
                    processMessagePatternString(source, defaultMessage, registeredFieldErrorMap, field, returnedFieldErrorMap);
                } else {
                    returnedFieldErrorMap.put(field, encodeWithUTF8(defaultMessage));
                }
            }
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleErrorBeanResponse(UPDATE_PROCESS_LAYOUT, true, returnedFieldErrorMap));
        }
        modifyValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrorMap = getMapWithNameMessageFromProperty(source, bindingResult);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleErrorBeanResponse(UPDATE_PROCESS_LAYOUT, false, fieldErrorMap));
        }
        articleService.correctArticle(IndustryArticle.builder().articleDto(articleDto).build());
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON).body(
                new SingleSuccessResponse(encodeWithUTF8(articleDto.getName()), UPDATE_INDUSTRY_ARTICLE_PATH + FINISH_PATH));
    }
}