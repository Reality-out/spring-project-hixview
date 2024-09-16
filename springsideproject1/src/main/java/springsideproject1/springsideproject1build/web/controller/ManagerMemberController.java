package springsideproject1.springsideproject1build.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.service.MemberService;

import static springsideproject1.springsideproject1build.domain.vo.Word.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.manager.Layout.SELECT_LAYOUT;
import static springsideproject1.springsideproject1build.domain.vo.manager.RequestUrl.SELECT_MEMBER_URL;
import static springsideproject1.springsideproject1build.domain.vo.manager.ViewName.SELECT_VIEW;

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
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute("members", memberService.findMembers());
        return SELECT_VIEW + "members-page";
    }
}
