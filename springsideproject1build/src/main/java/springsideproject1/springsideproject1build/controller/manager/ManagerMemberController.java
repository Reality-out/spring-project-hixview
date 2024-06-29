package springsideproject1.springsideproject1build.controller.manager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.domain.MembershipForm;
import springsideproject1.springsideproject1build.service.MemberService;

import java.util.List;

@Controller
@RequestMapping("/manager/member")
@RequiredArgsConstructor
public class ManagerMemberController {

    @Autowired
    private final MemberService memberService;

    @GetMapping("/showAll")
    @ResponseStatus(HttpStatus.OK)
    public String ShowMemberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "manager/select/membersPage";
    }

    @GetMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public String createMemberWithdraw() {
        return "manager/remove/membership/processPage";
    }

    @GetMapping("/remove/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishMemberWithdraw(@RequestParam("id") String id, Model model) {

        model.addAttribute("id", id);

        return "manager/remove/membership/finishPage";
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String removeMemberWithForm(HttpServletRequest request, MembershipForm form) {

        memberService.removeMember(form.getId());

        return "redirect:finish?id=" + form.getId();
    }
}
