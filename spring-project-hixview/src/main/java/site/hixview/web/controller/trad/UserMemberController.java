package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.hixview.domain.entity.member.dto.MemberDto;

import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.MEMBER;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.name.ViewName.VIEW_FINISH;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;
import static site.hixview.domain.vo.user.RequestPath.MEMBERSHIP_PATH;
import static site.hixview.domain.vo.user.ViewName.MEMBERSHIP_VIEW;
import static site.hixview.util.ControllerUtils.decodeWithUTF8;

@Controller
@RequiredArgsConstructor
public class UserMemberController {

    private final Logger log = LoggerFactory.getLogger(ManagerCompanyController.class);

    /**
     * Membership
     */
    @GetMapping(MEMBERSHIP_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String processMembershipPage(Model model) {
        model.addAttribute(MEMBER, new MemberDto());
        return MEMBERSHIP_VIEW + VIEW_PROCESS;
    }

    @GetMapping(MEMBERSHIP_PATH + FINISH_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String finishMembershipPage(@RequestParam String name, Model model) {
        model.addAttribute(NAME, decodeWithUTF8(name));
        return MEMBERSHIP_VIEW + VIEW_FINISH;
    }

}