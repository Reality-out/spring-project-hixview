package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.service.MemberService;

import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.ConstantUtility.*;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class ManagerMemberController {

    @Autowired
    private final MemberService memberService;

    /**
     * Select
     */
    @GetMapping(SELECT_MEMBER_URL)
    @ResponseStatus(HttpStatus.OK)
    public String showMemberList(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return MANAGER_SELECT_VIEW + "/membersPage";
    }

    /**
     * Remove
     */
    @GetMapping(REMOVE_MEMBER_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processMemberWithdraw(Model model) {
        model.addAttribute(DATA_TYPE_KOREAN, "회원");
        model.addAttribute(DATA_TYPE_ENGLISH, "member");
        model.addAttribute(KEY, ID);
        return MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @GetMapping(REMOVE_MEMBER_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishMemberWithdraw(@RequestParam String id, Model model) {
        model.addAttribute(DATA_TYPE_KOREAN, "회원");
        model.addAttribute(KEY, ID);
        model.addAttribute(VALUE, id);
        return MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX;
    }

    @PostMapping(REMOVE_MEMBER_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMemberWithdraw(RedirectAttributes redirect, @RequestParam String id) {
        memberService.removeMember(id);
        redirect.addAttribute(ID, id);
        return URL_REDIRECT_PREFIX + REMOVE_MEMBER_URL + URL_FINISH_SUFFIX;
    }
}
