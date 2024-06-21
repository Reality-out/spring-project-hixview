package springsideproject1.springsideproject1build.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerMainPageController {

    @GetMapping("/manager")
    public String mainPage() {
        return "manager/mainPage";
    }
}
