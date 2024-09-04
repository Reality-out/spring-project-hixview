package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.controller.manager.ManagerCompanyController;
import springsideproject1.springsideproject1build.domain.entity.member.Member;
import springsideproject1.springsideproject1build.domain.entity.member.MemberDto;
import springsideproject1.springsideproject1build.domain.service.MemberService;
import springsideproject1.springsideproject1build.domain.validation.validator.member.MemberBirthValidator;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERRORS_ARE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.MEMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;

@Controller
@RequiredArgsConstructor
public class UserMemberController {

    @Autowired
    private final MemberService memberService;

    private final MemberBirthValidator birthValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerCompanyController.class);

    /**
     * Membership
     */
    @GetMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processMembershipPage(Model model) {
        model.addAttribute(MEMBER, new MemberDto());
        return MEMBERSHIP_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembershipPage(@ModelAttribute(MEMBER) @Validated MemberDto memberDto,
                                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), model);
            return MEMBERSHIP_VIEW + VIEW_PROCESS_SUFFIX;
        }

        birthValidator.validate(memberDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), model);
            return MEMBERSHIP_VIEW + VIEW_PROCESS_SUFFIX;
        }

        memberService.registerMember(Member.builder().memberDto(memberDto).build());
        return URL_REDIRECT_PREFIX + MEMBERSHIP_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(MEMBERSHIP_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishMembershipPage() {
        return MEMBERSHIP_VIEW + VIEW_FINISH_SUFFIX;
    }

    /**
     * Other private methods
     */
    // Handle Error
    private void finishForRollback(String logMessage, Model model) {
        log.error(ERRORS_ARE, logMessage);
        model.addAttribute(LAYOUT_PATH, null);
    }
}