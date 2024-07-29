package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springsideproject1.springsideproject1build.service.MemberService;

import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.MANAGER_REMOVE_VIEW_NAME;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class ManagerMemberController {

    @Autowired
    private final MemberService memberService;

    /**
     * Select
     */
    @GetMapping("/manager/member/all")
    @ResponseStatus(HttpStatus.OK)
    public String ShowMemberList(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return "manager/select/membersPage";
    }

    /**
     * Remove
     */
    @GetMapping(REMOVE_MEMBER_URL)
    @ResponseStatus(HttpStatus.OK)
    public String createMemberWithdraw(Model model) {
        model.addAttribute("dataTypeKor", "회원");
        model.addAttribute("dataTypeEng", "member");
        model.addAttribute("key", "id");
        return MANAGER_REMOVE_VIEW_NAME + "processPage";
    }

    @GetMapping(REMOVE_MEMBER_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishMemberWithdraw(@RequestParam String id, Model model) {
        model.addAttribute("dataTypeKor", "회원");
        model.addAttribute("key", "id");
        model.addAttribute("value", id);
        return MANAGER_REMOVE_VIEW_NAME + "finishPage";
    }

    @PostMapping(REMOVE_MEMBER_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String removeMemberWithForm(@RequestParam String id) {
        memberService.removeMember(id);
        return URL_REDIRECT_PREFIX + REMOVE_MEMBER_URL + URL_FINISH_SUFFIX + "?id=" + id;
    }
}
