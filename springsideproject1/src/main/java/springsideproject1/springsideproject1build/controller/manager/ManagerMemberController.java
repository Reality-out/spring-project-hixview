package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springsideproject1.springsideproject1build.service.MemberService;

@Controller
@RequestMapping("/manager/member")
@RequiredArgsConstructor
public class ManagerMemberController {

    @Autowired
    private final MemberService memberService;

    /*
     * GetMapping
     */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public String ShowMemberList(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return "manager/select/membersPage";
    }

    @GetMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public String createMemberWithdraw() {
        return "manager/remove/membership/processPage";
    }

    @GetMapping("/remove/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishMemberWithdraw(@RequestParam String id, Model model) {
        model.addAttribute("id", id);
        return "manager/remove/membership/finishPage";
    }

    /*
     * PostMapping
     */
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String removeMemberWithForm(@RequestParam String id) {
        memberService.removeMember(id);
        return "redirect:remove/finish?id=" + id;
    }
}