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

import java.time.LocalDate;
import java.util.List;

import static springsideproject1.springsideproject1build.config.constant.FOLDER_PATH_CONFIG.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.MainUtility.decodeUTF8;

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
    public String loginPage(Model model) {
        model.addAttribute("findId", FIND_ID_URL);
        return USER_LOGIN_VIEW_NAME + "loginPage";
    }

    @GetMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processFindIdPage() {
        return USER_FIND_ID_VIEW_NAME + VIEW_NAME_PROCESS_SUFFIX;
    }

    @GetMapping(FIND_ID_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishFindIdPage(Model model, @RequestParam List<String> idList) {
        model.addAttribute("idList", decodeUTF8(idList));
        return USER_FIND_ID_VIEW_NAME + VIEW_NAME_FINISH_SUFFIX;
    }

    @PostMapping(FIND_ID_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitFindIdPage(RedirectAttributes redirect,
            @RequestParam String name, @RequestParam int year, @RequestParam int month, @RequestParam int date) {
        redirect.addAttribute("idList",
                memberService.findMembersByNameAndBirth(name, LocalDate.of(year, month, date)));
        return URL_REDIRECT_PREFIX + FIND_ID_URL + URL_FINISH_SUFFIX;
    }
}