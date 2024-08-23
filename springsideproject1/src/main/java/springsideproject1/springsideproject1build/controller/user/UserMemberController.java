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
import springsideproject1.springsideproject1build.domain.entity.member.Member;
import springsideproject1.springsideproject1build.domain.entity.member.MemberDto;
import springsideproject1.springsideproject1build.domain.entity.member.PhoneNumber;
import springsideproject1.springsideproject1build.domain.service.MemberService;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.MEMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;

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
    public String processMembershipPage(Model model) {
        model.addAttribute(MEMBER, new MemberDto());
        model.addAttribute("phoneNumber", PhoneNumber.builder().build());
        return MEMBERSHIP_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembershipPage(@ModelAttribute MemberDto memberDto) {
        memberService.registerMember(Member.builder().memberDto(memberDto)
                .phoneNumber(PhoneNumber.builder().build()).build());
        return URL_REDIRECT_PREFIX + MEMBERSHIP_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(MEMBERSHIP_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishMembershipPage() {
        return MEMBERSHIP_VIEW + VIEW_FINISH_SUFFIX;
    }
}