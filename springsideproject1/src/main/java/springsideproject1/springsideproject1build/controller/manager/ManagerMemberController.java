package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.service.MemberService;

import static springsideproject1.springsideproject1build.vo.CLASS.MEMBER;
import static springsideproject1.springsideproject1build.vo.LAYOUT.*;
import static springsideproject1.springsideproject1build.vo.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.vo.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.vo.WORD.*;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class ManagerMemberController {

    @Autowired
    private final MemberService memberService;

    @ModelAttribute(DATA_TYPE_KOREAN)
    public String dataTypeKor() {
        return "회원";
    }

    /**
     * See
     */
    @GetMapping(SELECT_MEMBER_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeMembers(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_PATH);
        model.addAttribute("members", memberService.findMembers());
        return MANAGER_SELECT_VIEW + "membersPage";
    }

    /**
     * Get rid of
     */
    @GetMapping(REMOVE_MEMBER_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processMemberWithdraw(Model model) {
        model.addAttribute(DATA_TYPE_ENGLISH, MEMBER);
        model.addAttribute(REMOVE_KEY, ID);
        return MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(REMOVE_MEMBER_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMemberWithdraw(RedirectAttributes redirect, @RequestParam String id) {
        memberService.removeMember(id);
        redirect.addAttribute(ID, id);
        return URL_REDIRECT_PREFIX + REMOVE_MEMBER_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(REMOVE_MEMBER_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishMemberWithdraw(@RequestParam String id, Model model) {
        model.addAttribute(KEY, ID);
        model.addAttribute(VALUE, id);
        return MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX;
    }
}
