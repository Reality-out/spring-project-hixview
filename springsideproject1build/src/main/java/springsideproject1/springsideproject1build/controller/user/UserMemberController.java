package springsideproject1.springsideproject1build.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.domain.MembershipForm;
import springsideproject1.springsideproject1build.service.MemberService;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Controller
@RequiredArgsConstructor
public class UserMemberController {

    @Autowired
    private final MemberService memberService;

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String loginOnSite() {
        return "user/loginPage";
    }

    @GetMapping("/membership")
    @ResponseStatus(HttpStatus.OK)
    public String createMembership() {
        return "user/membership/createPage";
    }

    @GetMapping("/membership/succeed")
    @ResponseStatus(HttpStatus.OK)
    public String succeedMembership() {
        return "user/membership/succeedPage";
    }

    @PostMapping("/membership")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembership(HttpServletRequest request, MembershipForm form) throws MalformedURLException, URISyntaxException {

        Member member = new Member.MemberBuilder()
                .id(form.getId())
                .password(form.getPassword())
                .name(form.getName())
                .build();

        memberService.joinMember(member);

        return "redirect:succeed";
    }
}