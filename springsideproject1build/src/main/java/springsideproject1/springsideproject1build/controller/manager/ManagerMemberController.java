package springsideproject1.springsideproject1build.controller.manager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.domain.MembershipForm;
import springsideproject1.springsideproject1build.service.MemberService;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/manager/member")
@RequiredArgsConstructor
public class ManagerMemberController {

    @Autowired
    private final MemberService memberService;

    @GetMapping("/showAll")
    @ResponseStatus(HttpStatus.OK)
    public String ShowMemberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "manager/select/membersPage";
    }

    @GetMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public String createMemberWithdraw() {
        return "manager/remove/membership/processPage";
    }

    @GetMapping("/remove/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishMemberWithdraw(Model model) {

        String ID = (String) model.getAttribute("ID");

        return "manager/remove/membership/finishPage";
    }

    @PostMapping("/remove")
    public ResponseEntity removeMemberWithForm(HttpServletRequest request, MembershipForm form, RedirectAttributes redirectAttributes) throws MalformedURLException, URISyntaxException {
        URL requestURL = new URL(request.getRequestURL().toString());
        URL redirectURL = new URL(requestURL + "/finish");

        memberService.removeMember(form.getId());
        redirectAttributes.addFlashAttribute("id", form.getId());

        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(redirectURL.toURI()).build();
    }
}
