package site.hixview.web.controller.rest;

import lombok.RequiredArgsConstructor;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.MEMBER;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestPath.MEMBERSHIP_PATH;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

@RestController
@RequiredArgsConstructor
public class UserMemberRestController {

    private final MemberService memberService;

    @PostMapping(MEMBERSHIP_PATH)
    public ResponseEntity<?> submitMembershipPage(@ModelAttribute(MEMBER) @Validated MembershipDto membershipDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrorMap = getFieldErrorMap(bindingResult);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    new SingleErrorResponse(BASIC_LAYOUT, true, fieldErrorMap));
        }
        memberService.registerMember(Member.builder().membershipDto(membershipDto).build());
        return ResponseEntity.status(HttpStatus.SEE_OTHER).contentType(MediaType.APPLICATION_JSON).body(new SingleSuccessResponse(
                membershipDto.getName(), MEMBERSHIP_PATH + FINISH_PATH));
    }

    private static Map<String, String> getFieldErrorMap(BindingResult bindingResult) {
        Map<String, String> fieldErrorMap = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            if (fieldErrorMap.get(field) == null) {
                fieldErrorMap.put(field, defaultMessage);
            } else {
                String[] codes = fieldError.getCodes();
                if (Objects.requireNonNull(codes)[codes.length - 1].equals("NotBlank")) {
                    fieldErrorMap.put(field, defaultMessage);
                }
            }
        }
        fieldErrorMap.replaceAll((k, v) -> encodeWithUTF8(fieldErrorMap.get(k)));
        return fieldErrorMap;
    }
}
