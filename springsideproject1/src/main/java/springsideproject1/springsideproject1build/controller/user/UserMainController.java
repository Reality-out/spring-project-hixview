package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.member.Member;
import springsideproject1.springsideproject1build.domain.member.MemberDto;
import springsideproject1.springsideproject1build.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

import static springsideproject1.springsideproject1build.config.constant.LAYOUT.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.utility.WordUtils.MEMBER;
import static springsideproject1.springsideproject1build.utility.MainUtils.decodeUTF8;
import static springsideproject1.springsideproject1build.utility.MainUtils.encodeUTF8;

@Controller
@RequiredArgsConstructor
public class UserMainController {

    @Autowired
    private final MemberService memberService;
    private final String idListString = "idList";

    /**
     * Main
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String processUserMainPage(Model model) {
        model.addAttribute(LAYOUT_PATH, BASIC_LAYOUT_PATH);
        return "user/mainPage";
    }

    /**
     * Login
     */
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String processLoginPage(Model model) {
        model.addAttribute("membership", MEMBERSHIP_URL);
        model.addAttribute("findId", FIND_ID_URL);
        return USER_LOGIN_VIEW + "loginPage";
    }

    /**
     * Find ID
     */
    @GetMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processFindIdPage(Model model) {
        model.addAttribute(MEMBER, new MemberDto());
        return USER_FIND_ID_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitFindIdPage(RedirectAttributes redirect, @ModelAttribute MemberDto memberDto) {
        Member member = Member.builder().memberDto(memberDto).build();
        redirect.addAttribute(idListString, encodeUTF8(memberService.findMembersByNameAndBirth(
                member.getName(), member.getBirth()).stream().map(Member::getId).collect(Collectors.toList())));
        return URL_REDIRECT_PREFIX + FIND_ID_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(FIND_ID_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishFindIdPage(Model model, @RequestParam List<String> idList) {
        model.addAttribute(idListString, decodeUTF8(idList));
        return USER_FIND_ID_VIEW + VIEW_FINISH_SUFFIX;
    }
}