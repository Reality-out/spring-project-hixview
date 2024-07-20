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
    @GetMapping("/add/single")
    @ResponseStatus(HttpStatus.OK)
    public String processAddSingleArticle() {
        return "manager/add/article/singleProcessPage";
    }

    @GetMapping("/add/single/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishAddSingleArticle() {
        return "manager/add/article/singleFinishPage";
    }

    @GetMapping("/add/multiple/string")
    @ResponseStatus(HttpStatus.OK)
    public String processAddMultipleArticleUsingString() {
        return "manager/add/article/multipleProcessStringPage";
    }

    @GetMapping("/add/multiple/string/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishAddMultipleArticleUsingString() {
        return "manager/add/article/multipleFinishStringPage";
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
    @PostMapping("/add/single")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddSingleArticle(String name, String press, String subjectCompany, String link,
                                         int year, int month, int date, Integer importance) {
        articleService.joinArticle(new Article.ArticleBuilder().name(name).press(press).subjectCompany(subjectCompany)
                .link(link).date(LocalDate.of(year, month, date)).importance(importance).build());
        return "redirect:single/finish";
    }

    @PostMapping("/add/multiple/string")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddMultipleArticleUsingString(String str) {
        articleService.joinArticlesWithString(str);
        return "redirect:string/finish";
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitRemoveSingleArticle(@RequestParam String name) {
        return "redirect:remove/finish?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
    }
}
