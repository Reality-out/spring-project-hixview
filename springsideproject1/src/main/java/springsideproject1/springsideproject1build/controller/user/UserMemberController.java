package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.member.Member;
import springsideproject1.springsideproject1build.domain.member.MemberDto;
import springsideproject1.springsideproject1build.domain.member.PhoneNumber;
import springsideproject1.springsideproject1build.service.MemberService;

import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.ConstantUtils.MEMBER;

@Controller
@RequiredArgsConstructor
public class UserMemberController {

    @Autowired
    private final MemberService memberService;

    /**
     * Membership
     */
    @GetMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processMembership(Model model) {
        model.addAttribute(MEMBER, new MemberDto());
        model.addAttribute("phoneNumber", PhoneNumber.builder().build());
        return MEMBERSHIP_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembership(@ModelAttribute MemberDto memberDto) {
        memberService.registerMember(Member.builder().memberDto(memberDto)
                .phoneNumber(PhoneNumber.builder().build()).build());
        return URL_REDIRECT_PREFIX + MEMBERSHIP_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(MEMBERSHIP_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishMembership() {
        return MEMBERSHIP_VIEW + VIEW_FINISH_SUFFIX;
    }
}