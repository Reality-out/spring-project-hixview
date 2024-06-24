package springsideproject1.springsideproject1build.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserMainPageController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String mainPage() {
        return "user/mainPage";
    }
}