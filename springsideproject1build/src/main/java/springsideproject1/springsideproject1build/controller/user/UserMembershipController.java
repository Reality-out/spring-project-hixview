package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.domain.MembershipForm;
import springsideproject1.springsideproject1build.service.MemberService;

@Controller
@RequestMapping("/membership")
@RequiredArgsConstructor
public class UserMembershipController {

    @Autowired
    private final MemberService memberService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String createMembership() {
        return "user/membership/createPage";
    }

    @GetMapping("/succeed")
    @ResponseStatus(HttpStatus.OK)
    public String succeedMembership() {
        return "user/membership/succeedPage";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembership(MembershipForm form) {

        Member member = new Member.MemberBuilder()
                .id(form.getId())
                .password(form.getPassword())
                .name(form.getName())
                .build();

        memberService.joinMember(member);

        return "redirect:membership/succeed";
    }
}