package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.service.MemberService;

@Controller
@RequestMapping("/membership")
@RequiredArgsConstructor
public class UserMembershipController {

    @Autowired
    private final MemberService memberService;

    /*
     * GetMapping
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String createMembership() {
        return "user/membership/createPage";
    }

    @GetMapping("/succeed")
    @ResponseStatus(HttpStatus.OK)
    public String succeedMembership() {
        return "user/membership/succeedPage";
    }

    /*
     * PostMapping
     */
    @PostMapping
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembership(@RequestParam String id, @RequestParam String password, @RequestParam String name) {

        Member member = new Member.MemberBuilder()
                .id(id)
                .password(password)
                .name(name)
                .build();

        memberService.joinMember(member);

        return "redirect:membership/succeed";
    }
}