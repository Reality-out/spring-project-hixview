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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hixview.domain.entity.article.response.SingleErrorResponse;
import site.hixview.domain.entity.article.response.SingleSuccessResponse;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;
import site.hixview.domain.validation.validator.BlogPostAddValidator;
import site.hixview.domain.validation.validator.BlogPostModifyValidator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static site.hixview.domain.vo.Regex.MESSAGE_PATTERN;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.POST;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_BLOG_POST_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_BLOG_POST_URL;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;
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
    @PostMapping(ADD_BLOG_POST_URL)
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
                    new SingleErrorResponse(ADD_PROCESS_LAYOUT, true, returnedFieldErrorMap));
        }
        addValidator.validate(postDto, bindingResult);
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
                    new SingleErrorResponse(ADD_PROCESS_LAYOUT, false, fieldErrorMap));
        }
        postService.registerPost(BlogPost.builder().blogPostDto(postDto).build());
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON).body(
                new SingleSuccessResponse(encodeWithUTF8(postDto.getName()), ADD_BLOG_POST_URL + FINISH_URL));
    }

    /**
     * Modify
     */
    @PostMapping(UPDATE_BLOG_POST_URL + FINISH_URL)
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
                    new SingleErrorResponse(UPDATE_PROCESS_LAYOUT, true, returnedFieldErrorMap));
        }
        modifyValidator.validate(postDto, bindingResult);
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
                    new SingleErrorResponse(UPDATE_PROCESS_LAYOUT, false, fieldErrorMap));
        }
        postService.correctPost(BlogPost.builder().blogPostDto(postDto).build());
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON).body(
                new SingleSuccessResponse(encodeWithUTF8(postDto.getName()), UPDATE_BLOG_POST_URL + FINISH_URL));
    }
}