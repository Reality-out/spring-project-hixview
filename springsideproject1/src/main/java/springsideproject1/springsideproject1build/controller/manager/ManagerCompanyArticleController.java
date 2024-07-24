package springsideproject1.springsideproject1build.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.service.CompanyArticleService;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static springsideproject1.springsideproject1build.config.ViewNameConfig.*;

@Controller
@RequestMapping("/manager/article")
@RequiredArgsConstructor
public class ManagerCompanyArticleController {

    @Autowired
    private final CompanyArticleService articleService;

    /**
     * Add - Single
     */
    @GetMapping("/add/single")
    @ResponseStatus(HttpStatus.OK)
    public String processAddSingleArticle() {
        return MANAGER_ADD_COMPANY_ARTICLE + "singleProcessPage";
    }

    @GetMapping("/add/single/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishAddSingleArticle() {
        return MANAGER_ADD_COMPANY_ARTICLE + "singleFinishPage";
    }

    @PostMapping("/add/single")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddSingleArticle(String name, String press, String subjectCompany, String link,
                                         int year, int month, int date, Integer importance) {
        articleService.joinArticle(new CompanyArticle.ArticleBuilder().name(name).press(press).subjectCompany(subjectCompany)
                .link(link).date(LocalDate.of(year, month, date)).importance(importance).build());
        return "redirect:single/finish";
    }

    /**
     * Add - Multiple
     */
    @GetMapping("/add/multiple/string")
    @ResponseStatus(HttpStatus.OK)
    public String processAddMultipleArticleUsingString() {
        return MANAGER_ADD_COMPANY_ARTICLE + "multipleProcessStringPage";
    }

    @GetMapping("/add/multiple/string/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishAddMultipleArticleUsingString() {
        return MANAGER_ADD_COMPANY_ARTICLE + "multipleFinishStringPage";
    }

    @PostMapping("/add/multiple/string")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitAddMultipleArticleUsingString(
            @RequestParam String subjectCompany, @RequestParam String articleString, @RequestParam String linkString) {
        articleService.joinArticlesWithString(subjectCompany, articleString, linkString);
        return "redirect:string/finish";
    }

    /**
     * Remove - Single
     */
    @GetMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public String processArticleRemove() {
        return MANAGER_REMOVE_COMPANY_ARTICLE + "processPage";
    }

    @GetMapping("/remove/finish")
    @ResponseStatus(HttpStatus.OK)
    public String finishArticleRemove(@RequestParam String name, Model model) {
        model.addAttribute("name", URLDecoder.decode(name, StandardCharsets.UTF_8));
        return MANAGER_REMOVE_COMPANY_ARTICLE + "finishPage";
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitRemoveSingleArticle(@RequestParam String name) {
        return "redirect:remove/finish?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
    }
}