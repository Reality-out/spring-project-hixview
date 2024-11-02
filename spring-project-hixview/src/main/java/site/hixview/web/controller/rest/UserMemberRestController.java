package site.hixview.web.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
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
import site.hixview.domain.entity.member.dto.LoginDto;
import site.hixview.domain.entity.member.dto.LoginInfoDto;
import site.hixview.domain.entity.member.dto.MembershipDto;
import site.hixview.domain.service.MemberService;
import site.hixview.domain.validation.validator.LoginValidator;
import site.hixview.domain.validation.validator.MembershipValidator;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static site.hixview.domain.vo.ExceptionMessage.COMPARE_SAME_VALUE_OF_ERROR_HIERARCHY;
import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.ErrorCodeName.EXIST;
import static site.hixview.domain.vo.name.ErrorCodeName.NOT_FOUND;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestPath.*;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

@RestController
@RequiredArgsConstructor
public class UserMemberRestController {

    private static final Logger log = LoggerFactory.getLogger(UserMemberRestController.class);
    private final MessageSource source;
    private final MemberService memberService;
    private final MembershipValidator membershipValidator;
    private final LoginValidator loginValidator;

    private final Map<String, Integer> privateErrorHierarchy = new HashMap<>() {{
        put("NotBlank", 0);
        put("Pattern", 1);
        put(EXIST, 2);
    }};

    @PostMapping(MEMBERSHIP_PATH)
    public ResponseEntity<?> submitMembershipPage(@ModelAttribute(MEMBER) @Validated MembershipDto membershipDto, BindingResult bindingResult, Model model) {
        membershipValidator.validate(membershipDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> returnedFieldErrorMap = getMapWithNameDefaultMessageMembership(bindingResult);
            returnedFieldErrorMap.replaceAll((k, v) -> encodeWithUTF8(returnedFieldErrorMap.get(k)));
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleErrorResponse(BASIC_LAYOUT, returnedFieldErrorMap));
        }
        memberService.registerMember(Member.builder().membershipDto(membershipDto).build());
        HttpHeaders headers = new HttpHeaders();
        String redirectPath = MEMBERSHIP_PATH + FINISH_PATH;
        headers.setLocation(URI.create(redirectPath));
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON)
                .headers(headers).body(new SingleSuccessResponse(membershipDto.getName(), redirectPath));
    }

    private Map<String, String> getMapWithNameDefaultMessageMembership(BindingResult bindingResult) {
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

    @PostMapping(LOGIN_PATH)
    public ResponseEntity<?> submitLoginPage(@ModelAttribute(MEMBER) @Validated LoginDto loginDto, BindingResult bindingResult, Model model, HttpServletRequest request) {
        loginValidator.validate(loginDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> returnedFieldErrorMap = getMapWithNameDefaultMessageLogin(bindingResult);
            if (returnedFieldErrorMap.get(ID) != null) {
                returnedFieldErrorMap.remove(PASSWORD);
            }
            returnedFieldErrorMap.replaceAll((k, v) -> encodeWithUTF8(returnedFieldErrorMap.get(k)));
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleErrorResponse(BASIC_LAYOUT, returnedFieldErrorMap));
        }
        HttpSession session = request.getSession();
        LoginInfoDto loginInfoDto = new LoginInfoDto();
        loginInfoDto.setId(loginDto.getId());
        loginInfoDto.setName(memberService.findMemberByID(loginDto.getId()).orElseThrow().getName());
        session.setAttribute(LOGIN_INFO, loginInfoDto);
        session.setMaxInactiveInterval(600);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(""));
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON)
                .headers(headers)
                .body(new HashMap<>() {{
                    put(REDIRECT_PATH, "");
                }});
    }

    private Map<String, String> getMapWithNameDefaultMessageLogin(BindingResult bindingResult) {
        Map<String, String> returnedFieldErrorMap = new HashMap<>();
        Map<String, String> registeredFieldErrorMap = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            String errCode = Objects.requireNonNull(fieldError.getCodes())[3];
            String registeredValue = registeredFieldErrorMap.get(field);
            if (registeredValue == null || registeredValue.equals(NOT_FOUND)) {
                registeredFieldErrorMap.put(field, errCode);
                returnedFieldErrorMap.put(field, defaultMessage);
            }
        }
        return returnedFieldErrorMap;
    }
}
