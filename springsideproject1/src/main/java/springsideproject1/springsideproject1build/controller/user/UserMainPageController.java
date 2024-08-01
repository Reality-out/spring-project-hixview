package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.service.MemberService;

import static springsideproject1.springsideproject1build.config.constant.FOLDER_PATH_CONFIG.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.USER_LOGIN_VIEW_NAME;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.VIEW_NAME_PROCESS_SUFFIX;

@Controller
@RequiredArgsConstructor
public class UserMainPageController {

    @Autowired
    private final MemberService memberService;

    /**
     * Main
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String mainPage(Model model) {
        model.addAttribute("layoutPath", BASIC_LAYOUT_PATH);
        return "user/mainPage";
    }

    /**
     * Login
     */
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String loginPage() {
        return USER_LOGIN_VIEW_NAME + "loginPage";
    }

    @GetMapping("/login/find/id")
    @ResponseStatus(HttpStatus.OK)
    public String processFindIdPage() {
        return USER_LOGIN_VIEW_NAME + VIEW_NAME_PROCESS_SUFFIX;
    }

    @PostMapping("/login/find/id")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitFindIdPage(RedirectAttributes redirect,
            @RequestParam String name, @RequestParam int year, @RequestParam int month, @RequestParam int date) {
        memberService.findMembersByName(name); // TODO: not completed logic
        return USER_LOGIN_VIEW_NAME + VIEW_NAME_PROCESS_SUFFIX;
    }
}