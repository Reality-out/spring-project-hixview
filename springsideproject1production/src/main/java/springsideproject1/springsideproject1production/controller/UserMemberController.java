package springsideproject1.springsideproject1production.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import springsideproject1.springsideproject1production.domain.Member;
import springsideproject1.springsideproject1production.domain.MembershipForm;
import springsideproject1.springsideproject1production.service.MemberService;

@Controller
@RequiredArgsConstructor
public class UserMemberController {
    private final MemberService memberService;

    @GetMapping("/membership")
    public String createMembershipForm() {
        return "user/createMembershipForm";
    }

    @PostMapping("/membership")
    public String submitMembershipForm(MembershipForm form) {
        Member member = new Member();
        member.setId(form.getId());
        member.setPassword(form.getPassword());
        member.setName(form.getName());

        memberService.joinMember(member);

        return "user/finishMembership";
    }
}