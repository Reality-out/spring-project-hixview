package springsideproject1.springsideproject1build.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static springsideproject1.springsideproject1build.config.FolderConfig.USER_BASIC_LAYOUT_PATH;

@Controller
public class UserMainPageController {

    /**
     * Main
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String mainPage(Model model) {
        model.addAttribute("layoutPath", USER_BASIC_LAYOUT_PATH);
        return "user/mainPage";
    }

    /**
     * Login
     */
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String loginOnSite() {
        return "user/loginPage";
    }
}