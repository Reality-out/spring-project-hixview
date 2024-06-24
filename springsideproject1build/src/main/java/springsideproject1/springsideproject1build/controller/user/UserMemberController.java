package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.domain.MembershipForm;
import springsideproject1.springsideproject1build.service.MemberService;

@Controller
@RequiredArgsConstructor
public class UserMemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String loginOnSite() {
        return "user/loginPage";
    }

    @GetMapping("/membership")
    @ResponseStatus(HttpStatus.OK)
    public String createMembershipForm() {
        return "user/membership/createPage";
    }

    @PostMapping("/membership")
    @ResponseStatus(HttpStatus.OK)
    public String submitMembershipForm(MembershipForm form) {
        Member member = new Member();
        member.setId(form.getId());
        member.setPassword(form.getPassword());
        member.setName(form.getName());

        memberService.joinMember(member);

        return "user/membership/finishPage";
    }
}