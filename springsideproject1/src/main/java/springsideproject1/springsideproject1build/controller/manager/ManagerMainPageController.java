package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class ManagerMainPageController {

    /**
     * GetMapping
     */
    @GetMapping("/manager")
    @ResponseStatus(HttpStatus.OK)
    public String mainPage() {
        return "manager/mainPage";
    }
}
