package springsideproject1.springsideproject1build.web.controller;

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
import springsideproject1.springsideproject1build.domain.entity.member.Member;
import springsideproject1.springsideproject1build.domain.entity.member.MemberDto;
import springsideproject1.springsideproject1build.domain.service.MemberService;
import springsideproject1.springsideproject1build.domain.validation.validator.MemberBirthValidator;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Member.MEMBER;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.FINISH_URL;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.REDIRECT_URL;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.FINISH_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.PROCESS_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.Word.ERRORS_ARE;
import static springsideproject1.springsideproject1build.domain.vo.Word.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.user.RequestUrl.MEMBERSHIP_URL;
import static springsideproject1.springsideproject1build.domain.vo.user.ViewName.MEMBERSHIP_VIEW;

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
        return MEMBERSHIP_VIEW + PROCESS_VIEW;
    }

    @PostMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembershipPage(@ModelAttribute(MEMBER) @Validated MemberDto memberDto,
                                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), model);
            return MEMBERSHIP_VIEW + PROCESS_VIEW;
        }

        birthValidator.validate(memberDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), model);
            return MEMBERSHIP_VIEW + PROCESS_VIEW;
        }

        memberService.registerMember(Member.builder().memberDto(memberDto).build());
        return REDIRECT_URL + MEMBERSHIP_URL + FINISH_URL;
    }

    @GetMapping(MEMBERSHIP_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishMembershipPage() {
        return MEMBERSHIP_VIEW + FINISH_VIEW;
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