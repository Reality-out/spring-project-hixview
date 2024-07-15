package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.Article;
import springsideproject1.springsideproject1build.service.ArticleService;
import springsideproject1.springsideproject1build.service.CompanyService;

import java.time.LocalDate;

import static springsideproject1.springsideproject1build.Utility.*;

@Controller
@RequiredArgsConstructor
public class ManagerMainPageController {

    /*
     * GetMapping
     */
    @GetMapping("/manager")
    @ResponseStatus(HttpStatus.OK)
    public String mainPage() {
        return "manager/mainPage";
    }
}
