package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.domain.MembershipForm;
import springsideproject1.springsideproject1build.service.MemberService;

import java.util.List;

@Controller
@RequestMapping("/manager/member")
@RequiredArgsConstructor
public class ManagerMemberController {
    private final MemberService memberService;

    @GetMapping("/showAll")
    @ResponseStatus(HttpStatus.OK)
    public String ShowMemberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "manager/select/members";
    }

    @GetMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public String createMemberWithdrawForm() {
        return "manager/remove/membership/process";
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public String removeMemberWithForm(MembershipForm form, Model model) {
        memberService.removeMember(form.getId());
        model.addAttribute("ID", form.getId());
        return "manager/remove/membership/finish";
    }
}
