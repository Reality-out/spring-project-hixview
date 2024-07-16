package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springsideproject1.springsideproject1build.domain.Article;
import springsideproject1.springsideproject1build.service.ArticleService;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Controller
@RequestMapping("/manager/article")
@RequiredArgsConstructor
public class ManagerArticleController {

    @Autowired
    private final ArticleService articleService;

    /*
     * GetMapping
     */
    @GetMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public String processArticleAdd() {
        return "manager/add/article/processPage";
    }

    @GetMapping("/add/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishArticleAdd() {
        return "manager/add/article/finishPage";
    }

    @GetMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public String processArticleRemove() {
        return "manager/remove/article/processPage";
    }

    @GetMapping("/remove/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishArticleRemove(@RequestParam String name, Model model) {
        model.addAttribute("name", URLDecoder.decode(name, StandardCharsets.UTF_8));
        return "manager/remove/article/finishPage";
    }

    /*
     * PostMapping
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitArticleAdd(String name, String press, String subjectCompany, String link,
                                   int year, int month, int date, Integer importance) {
        articleService.joinArticle(new Article.ArticleBuilder().name(name).press(press).subjectCompany(subjectCompany)
                .link(link).date(LocalDate.of(year, month, date)).importance(importance).build());
        return "redirect:add/finish";
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitArticleRemove(@RequestParam String name) {
        return "redirect:remove/finish?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
    }
}
