package site.hixview.web.controller.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hixview.domain.entity.article.response.SingleErrorResponse;
import site.hixview.domain.entity.article.response.SingleSuccessResponse;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.entity.member.dto.MembershipDto;
import site.hixview.domain.service.MemberService;
import site.hixview.domain.validation.validator.MembershipValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static site.hixview.domain.vo.ExceptionMessage.COMPARE_SAME_VALUE_OF_ERROR_HIERARCHY;
import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.MEMBER;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestPath.MEMBERSHIP_PATH;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

@RestController
@RequiredArgsConstructor
public class UserMemberRestController {

    private static final Logger log = LoggerFactory.getLogger(UserMemberRestController.class);
    private final MessageSource source;
    private final MemberService memberService;
    private final MembershipValidator membershipValidator;

    private final Map<String, Integer> privateErrorHierarchy = new HashMap<>() {{
        put("NotBlank", 0);
        put("Pattern", 1);
        put("Exist", 2);
    }};

    @PostMapping(MEMBERSHIP_PATH)
    public ResponseEntity<?> submitMembershipPage(@ModelAttribute(MEMBER) @Validated MembershipDto membershipDto, BindingResult bindingResult, Model model) {
        membershipValidator.validate(membershipDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> returnedFieldErrorMap = getMapWithNameDefaultMessage(bindingResult);
            returnedFieldErrorMap.replaceAll((k, v) -> encodeWithUTF8(returnedFieldErrorMap.get(k)));
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleErrorResponse(BASIC_LAYOUT, returnedFieldErrorMap));
        }
        memberService.registerMember(Member.builder().membershipDto(membershipDto).build());
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON).body(new SingleSuccessResponse(
                membershipDto.getName(), MEMBERSHIP_PATH + FINISH_PATH));
    }

    private Map<String, String> getMapWithNameDefaultMessage(BindingResult bindingResult) {
        Map<String, String> returnedFieldErrorMap = new HashMap<>();
        Map<String, String> registeredFieldErrorMap = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            String errCode = Objects.requireNonNull(fieldError.getCodes())[3];
            if (registeredFieldErrorMap.get(field) == null) {
                registeredFieldErrorMap.put(field, errCode);
                returnedFieldErrorMap.put(field, defaultMessage);
            } else {
                compareAndProcessErrorHierarchy(registeredFieldErrorMap, field, errCode, returnedFieldErrorMap, defaultMessage);
            }
        }
        return returnedFieldErrorMap;
    }

    private void compareAndProcessErrorHierarchy(Map<String, String> registeredFieldErrorMap, String field, String errCode, Map<String, String> returnedFieldErrorMap, String defaultMessage) {
        int hierarchyDif = privateErrorHierarchy.get(registeredFieldErrorMap.get(field)) - privateErrorHierarchy.get(errCode);
        if (hierarchyDif > 0) {
            registeredFieldErrorMap.put(field, errCode);
            returnedFieldErrorMap.put(field, defaultMessage);
        } else if (hierarchyDif == 0) {
            throw new IllegalStateException(COMPARE_SAME_VALUE_OF_ERROR_HIERARCHY);
        }
    }
}