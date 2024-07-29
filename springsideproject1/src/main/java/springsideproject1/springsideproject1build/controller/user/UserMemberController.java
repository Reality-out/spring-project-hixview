package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.service.MemberService;

import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.URL_REDIRECT_PREFIX;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.MEMBERSHIP_VIEW_NAME;

@Controller
@RequiredArgsConstructor
public class UserMemberController {

    @Autowired
    private final MemberService memberService;

    /**
     * Membership
     */
    @GetMapping("/membership")
    @ResponseStatus(HttpStatus.OK)
    public String createMembership() {
        return MEMBERSHIP_VIEW_NAME + "createPage";
    }

    @GetMapping("/membership/succeed")
    @ResponseStatus(HttpStatus.OK)
    public String succeedMembership() {
        return MEMBERSHIP_VIEW_NAME + "succeedPage";
    }

    @PostMapping("/membership")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembership(@RequestParam String id, @RequestParam String password, @RequestParam String name) {

        memberService.joinMember(new Member.MemberBuilder()
                .id(id)
                .password(password)
                .name(name)
                .build());

        return URL_REDIRECT_PREFIX + "/membership/succeed";
    }
}