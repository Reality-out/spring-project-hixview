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
import site.hixview.domain.entity.article.response.BasicSuccessResponse;
import site.hixview.domain.entity.article.response.BeanValidationErrorResponse;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;
import site.hixview.domain.validation.validator.BlogPostAddValidator;
import site.hixview.domain.validation.validator.BlogPostModifyValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static site.hixview.domain.vo.Regex.MESSAGE_PATTERN;
import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.POST;
import static site.hixview.domain.vo.manager.RequestPath.ADD_BLOG_POST_PATH;
import static site.hixview.domain.vo.manager.RequestPath.UPDATE_BLOG_POST_PATH;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;
import static site.hixview.util.RestControllerUtils.getMapWithNameMessageFromProperty;
import static site.hixview.util.RestControllerUtils.processMessagePatternString;

@RestController
@RequiredArgsConstructor
public class ManagerBlogPostRestController {

    @Autowired
    private MessageSource source;

    private final BlogPostService postService;

    private final BlogPostAddValidator addValidator;
    private final BlogPostModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerBlogPostRestController.class);

    /**
     * Add - Single
     */
    @PostMapping(ADD_BLOG_POST_PATH)
    public ResponseEntity<?> submitAddBlogPost(@ModelAttribute(POST) @Validated BlogPostDto postDto,
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
        addValidator.validate(postDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrorMap = getMapWithNameMessageFromProperty(source, bindingResult);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new BeanValidationErrorResponse(false, fieldErrorMap));
        }
        postService.registerPost(BlogPost.builder().postDto(postDto).build());
        String redirectPath = ADD_BLOG_POST_PATH + FINISH_PATH;
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON)
                .body(new BasicSuccessResponse(encodeWithUTF8(postDto.getName()), redirectPath));
    }

    /**
     * Modify
     */
    @PostMapping(UPDATE_BLOG_POST_PATH + FINISH_PATH)
    public ResponseEntity<?> submitModifyBlogPost(@ModelAttribute(POST) @Validated BlogPostDto postDto,
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
        modifyValidator.validate(postDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrorMap = getMapWithNameMessageFromProperty(source, bindingResult);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new BeanValidationErrorResponse(false, fieldErrorMap));
        }
        postService.correctPost(BlogPost.builder().postDto(postDto).build());
        String redirectPath = UPDATE_BLOG_POST_PATH + FINISH_PATH;
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON)
                .body(new BasicSuccessResponse(encodeWithUTF8(postDto.getName()), redirectPath));
    }
}