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
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.entity.article.response.BasicSuccessResponse;
import site.hixview.domain.entity.article.response.BeanValidationErrorResponse;
import site.hixview.domain.service.EconomyArticleService;
import site.hixview.domain.validation.validator.EconomyArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.EconomyArticleModifyValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static site.hixview.domain.vo.Regex.MESSAGE_PATTERN;
import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.ARTICLE;
import static site.hixview.domain.vo.manager.RequestPath.ADD_SINGLE_ECONOMY_ARTICLE_PATH;
import static site.hixview.domain.vo.manager.RequestPath.UPDATE_ECONOMY_ARTICLE_PATH;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;
import static site.hixview.util.RestControllerUtils.getMapWithNameMessageFromProperty;
import static site.hixview.util.RestControllerUtils.processMessagePatternString;

@RestController
@RequiredArgsConstructor
public class ManagerEconomyArticleRestController {

    @Autowired
    private MessageSource source;

    private final EconomyArticleService articleService;

    private final EconomyArticleAddSimpleValidator simpleValidator;
    private final EconomyArticleModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerEconomyArticleRestController.class);

    /**
     * Add - Single
     */
    @PostMapping(ADD_SINGLE_ECONOMY_ARTICLE_PATH)
    public ResponseEntity<?> submitAddEconomyArticle(@ModelAttribute(ARTICLE) @Validated EconomyArticleDto articleDto,
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
                    new BeanValidationErrorResponse(true, returnedFieldErrorMap));
        }
        simpleValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrorMap = getMapWithNameMessageFromProperty(source, bindingResult);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new BeanValidationErrorResponse(false, fieldErrorMap));
        }
        articleService.registerArticle(EconomyArticle.builder().articleDto(articleDto).build());
        String redirectPath = ADD_SINGLE_ECONOMY_ARTICLE_PATH + FINISH_PATH;
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON)
                .body(new BasicSuccessResponse(encodeWithUTF8(articleDto.getName()), redirectPath));
    }

    /**
     * Modify
     */
    @PostMapping(UPDATE_ECONOMY_ARTICLE_PATH + FINISH_PATH)
    public ResponseEntity<?> submitModifyEconomyArticle(@ModelAttribute(ARTICLE) @Validated EconomyArticleDto articleDto,
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
                    new BeanValidationErrorResponse(true, returnedFieldErrorMap));
        }
        modifyValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrorMap = getMapWithNameMessageFromProperty(source, bindingResult);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new BeanValidationErrorResponse(false, fieldErrorMap));
        }
        articleService.correctArticle(EconomyArticle.builder().articleDto(articleDto).build());
        String redirectPath = UPDATE_ECONOMY_ARTICLE_PATH + FINISH_PATH;
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON)
                .body(new BasicSuccessResponse(encodeWithUTF8(articleDto.getName()), redirectPath));
    }
}