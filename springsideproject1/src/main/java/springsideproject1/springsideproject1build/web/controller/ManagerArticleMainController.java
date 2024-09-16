package springsideproject1.springsideproject1build.web.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMainDto;
import springsideproject1.springsideproject1build.domain.service.ArticleMainService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.domain.validation.validator.ArticleMainAddValidator;
import springsideproject1.springsideproject1build.domain.validation.validator.ArticleMainModifyValidator;

import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Article.ARTICLE;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionMessage.NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionString.BEAN_VALIDATION_ERROR;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionString.NOT_FOUND_ARTICLE_MAIN_ERROR;
import static springsideproject1.springsideproject1build.domain.vo.Regex.NUMBER_PATTERN;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.FINISH_URL;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.REDIRECT_URL;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.*;
import static springsideproject1.springsideproject1build.domain.vo.Word.*;
import static springsideproject1.springsideproject1build.domain.vo.manager.Layout.*;
import static springsideproject1.springsideproject1build.domain.vo.manager.RequestUrl.*;
import static springsideproject1.springsideproject1build.domain.vo.manager.ViewName.*;
import static springsideproject1.springsideproject1build.util.ControllerUtils.*;

@Controller
@RequiredArgsConstructor
public class ManagerArticleMainController {

    private final ArticleMainService articleMainService;
    private final CompanyService companyService;

    private final ArticleMainAddValidator addValidator;
    private final ArticleMainModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerArticleMainController.class);

    /**
     * Add
     */
    @GetMapping(ADD_ARTICLE_MAIN_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddArticleMain(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        model.addAttribute(ARTICLE, new ArticleMainDto());
        return ADD_ARTICLE_MAIN_VIEW + PROCESS_VIEW;
    }

    @PostMapping(ADD_ARTICLE_MAIN_URL)
    public String submitAddArticleMain(@ModelAttribute(ARTICLE) @Validated ArticleMainDto articleMainDto,
                                          BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            return ADD_ARTICLE_MAIN_VIEW + PROCESS_VIEW;
        }

        addValidator.validate(articleMainDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, null, model);
            return ADD_ARTICLE_MAIN_VIEW + PROCESS_VIEW;
        }

        articleMainService.registerArticle(ArticleMain.builder().articleDto(articleMainDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(articleMainDto.getName()));
        return REDIRECT_URL + ADD_ARTICLE_MAIN_URL + FINISH_URL;
    }

    @GetMapping(ADD_ARTICLE_MAIN_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddArticleMain(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_ARTICLE_MAIN_VIEW + FINISH_VIEW;
    }

    /**
     * See
     */
    @GetMapping(SELECT_ARTICLE_MAIN_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeArticleMains(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute("articleMains", articleMainService.findArticles());
        return SELECT_VIEW + "article-mains-page";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_ARTICLE_MAIN_URL)
	@ResponseStatus(HttpStatus.OK)
	public String initiateModifyArticleMain(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
		return UPDATE_ARTICLE_MAIN_VIEW + BEFORE_PROCESS_VIEW;
	}

    @PostMapping(UPDATE_ARTICLE_MAIN_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyArticleMain(@RequestParam String numberOrName, Model model) {
        Optional<ArticleMain> articleOrEmpty = articleMainService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME,
                    UPDATE_PROCESS_LAYOUT, NOT_FOUND_ARTICLE_MAIN_ERROR, model);
            return UPDATE_ARTICLE_MAIN_VIEW + BEFORE_PROCESS_VIEW;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
        model.addAttribute("updateUrl", UPDATE_ARTICLE_MAIN_URL + FINISH_URL);
        model.addAttribute(ARTICLE, articleOrEmpty.orElseThrow().toDto());
        return UPDATE_ARTICLE_MAIN_VIEW + AFTER_PROCESS_VIEW;
    }

    @PostMapping(UPDATE_ARTICLE_MAIN_URL + FINISH_URL)
    public String submitModifyArticleMain(@ModelAttribute(ARTICLE) @Validated ArticleMainDto articleMainDto,
                                                 BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            model.addAttribute("updateUrl", UPDATE_ARTICLE_MAIN_URL + FINISH_URL);
            return UPDATE_ARTICLE_MAIN_VIEW + AFTER_PROCESS_VIEW;
        }

        modifyValidator.validate(articleMainDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, null, model);
            model.addAttribute("updateUrl", UPDATE_ARTICLE_MAIN_URL + FINISH_URL);
            return UPDATE_ARTICLE_MAIN_VIEW + AFTER_PROCESS_VIEW;
        }

        articleMainService.correctArticle(ArticleMain.builder().articleDto(articleMainDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(articleMainDto.getName()));
        return REDIRECT_URL + UPDATE_ARTICLE_MAIN_URL + FINISH_URL;
    }

    @GetMapping(UPDATE_ARTICLE_MAIN_URL + FINISH_URL)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyArticleMain(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_ARTICLE_MAIN_VIEW + FINISH_VIEW;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_ARTICLE_MAIN_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidArticleMain(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT);
        return REMOVE_ARTICLE_MAIN_VIEW + PROCESS_VIEW;
    }

    @PostMapping(REMOVE_ARTICLE_MAIN_URL)
    public String submitRidArticleMain(RedirectAttributes redirect, @RequestParam String numberOrName, Model model) {
        Optional<ArticleMain> articleOrEmpty = articleMainService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME,
                    REMOVE_PROCESS_LAYOUT, NOT_FOUND_ARTICLE_MAIN_ERROR, model);
            return REMOVE_ARTICLE_MAIN_VIEW + PROCESS_VIEW;
        }

        if (NUMBER_PATTERN.matcher(numberOrName).matches()) {
            numberOrName = articleMainService.findArticleByNumber(Long.parseLong(numberOrName)).orElseThrow().getName();
        }
        articleMainService.removeArticleByName(numberOrName);
        redirect.addAttribute(NAME, encodeWithUTF8(numberOrName));
        return REDIRECT_URL + REMOVE_ARTICLE_MAIN_URL + FINISH_URL;
    }

    @GetMapping(REMOVE_ARTICLE_MAIN_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidArticleMain(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_ARTICLE_MAIN_VIEW + FINISH_VIEW;
    }
}