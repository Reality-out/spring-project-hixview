package springsideproject1.springsideproject1build.web.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.service.MemberService;

import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.SELECT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.SELECT_MEMBER_URL;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.MANAGER_SELECT_VIEW;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class ManagerMemberController {

    private final MemberService memberService;

    /**
     * See
     */
    @GetMapping(SELECT_MEMBER_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeMembers(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_PATH);
        model.addAttribute("members", memberService.findMembers());
        return MANAGER_SELECT_VIEW + "members-page";
    }
}
