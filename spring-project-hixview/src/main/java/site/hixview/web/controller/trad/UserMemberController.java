package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.entity.member.dto.MemberDto;
import site.hixview.domain.service.MemberService;
import site.hixview.domain.validation.validator.MemberBirthdayValidator;

import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.MEMBER;
import static site.hixview.domain.vo.name.ViewName.VIEW_FINISH;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;
import static site.hixview.domain.vo.user.RequestUrl.MEMBERSHIP_URL;
import static site.hixview.domain.vo.user.ViewName.MEMBERSHIP_VIEW;
import static site.hixview.util.ControllerUtils.finishForRollback;

@Controller
@RequiredArgsConstructor
public class UserMemberController {

    private final MemberService memberService;

    private final MemberBirthdayValidator birthValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerCompanyController.class);

    /**
     * Membership
     */
    @GetMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processMembershipPage(Model model) {
        model.addAttribute(MEMBER, new MemberDto());
        return MEMBERSHIP_VIEW + VIEW_PROCESS;
    }

    @PostMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembershipPage(@ModelAttribute(MEMBER) @Validated MemberDto memberDto,
                                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), null, null, model);
            return MEMBERSHIP_VIEW + VIEW_PROCESS;
        }

        birthValidator.validate(memberDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), null, null, model);
            return MEMBERSHIP_VIEW + VIEW_PROCESS;
        }

        memberService.registerMember(Member.builder().memberDto(memberDto).build());
        return REDIRECT_URL + MEMBERSHIP_URL + FINISH_URL;
    }

    @GetMapping(MEMBERSHIP_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishMembershipPage() {
        return MEMBERSHIP_VIEW + VIEW_FINISH;
    }
}