package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hixview.domain.entity.home.ArticleMain;
import site.hixview.domain.entity.home.dto.ArticleMainDto;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.ArticleMainAddValidator;
import site.hixview.domain.validation.validator.ArticleMainModifyValidator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.ExceptionMessage.NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME;
import static site.hixview.domain.vo.Regex.NUMBER_PATTERN;
import static site.hixview.domain.vo.RequestPath.*;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestPath.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.domain.vo.name.ExceptionName.BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_ARTICLE_MAIN_ERROR;
import static site.hixview.domain.vo.name.ViewName.*;
import static site.hixview.util.ControllerUtils.*;

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
    @GetMapping(ADD_ARTICLE_MAIN_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String processAddArticleMain(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        model.addAttribute(ARTICLE, new ArticleMainDto());
        return ADD_ARTICLE_MAIN_VIEW + VIEW_PROCESS;
    }

    @PostMapping(ADD_ARTICLE_MAIN_PATH)
    public String submitAddArticleMain(@ModelAttribute(ARTICLE) @Validated ArticleMainDto articleMainDto,
                                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            return ADD_ARTICLE_MAIN_VIEW + VIEW_PROCESS;
        }

        addValidator.validate(articleMainDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, null, model);
            return ADD_ARTICLE_MAIN_VIEW + VIEW_PROCESS;
        }

        articleMainService.registerArticle(ArticleMain.builder().articleDto(articleMainDto).build());
        return RELATIVE_REDIRECT_PATH + fromPath(ADD_ARTICLE_MAIN_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(articleMainDto.getName())).build().toUriString();
    }

    @GetMapping(ADD_ARTICLE_MAIN_PATH + FINISH_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddArticleMain(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute(REPEAT_PATH, ADD_ARTICLE_MAIN_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_ARTICLE_MAIN_VIEW + VIEW_FINISH;
    }

    /**
     * See
     */
    @GetMapping(SELECT_ARTICLE_MAIN_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeArticleMains(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute(ARTICLE_MAINS, articleMainService.findArticles());
        return SELECT_VIEW + "article-mains-page";
    }

    @GetMapping(CHECK_IMAGE_PATH_ARTICLE_MAIN_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String processCheckImagePathArticleMains(Model model) {
        List<ArticleMain> articlesWithInvalidImagePath = new ArrayList<>();
        Path prefix = Paths.get(ROOT_PATH).resolve(STATIC_RESOURCE_PATH);
        for (ArticleMain article : articleMainService.findArticles()) {
            Path filePath = prefix.resolve(article.getImagePath().substring(1)).toAbsolutePath();
            if (!filePath.toFile().isFile()) {
                articlesWithInvalidImagePath.add(article);
            }
        }
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute(ARTICLE_MAINS, articlesWithInvalidImagePath);
        return SELECT_VIEW + "article-mains-page";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_ARTICLE_MAIN_PATH)
	@ResponseStatus(HttpStatus.OK)
	public String initiateModifyArticleMain(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT);
		return UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS;
	}

    @PostMapping(UPDATE_ARTICLE_MAIN_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyArticleMain(@RequestParam String numberOrName, Model model) {
        Optional<ArticleMain> articleOrEmpty = articleMainService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME,
                    UPDATE_QUERY_LAYOUT, NOT_FOUND_ARTICLE_MAIN_ERROR, model);
            return UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
        model.addAttribute(UPDATE_PATH, UPDATE_ARTICLE_MAIN_PATH + FINISH_PATH);
        model.addAttribute(ARTICLE, articleOrEmpty.orElseThrow().toDto());
        return UPDATE_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS;
    }

    @PostMapping(UPDATE_ARTICLE_MAIN_PATH + FINISH_PATH)
    public String submitModifyArticleMain(@ModelAttribute(ARTICLE) @Validated ArticleMainDto articleMainDto,
                                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            model.addAttribute(UPDATE_PATH, UPDATE_ARTICLE_MAIN_PATH + FINISH_PATH);
            return UPDATE_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS;
        }

        modifyValidator.validate(articleMainDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, null, model);
            model.addAttribute(UPDATE_PATH, UPDATE_ARTICLE_MAIN_PATH + FINISH_PATH);
            return UPDATE_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS;
        }

        articleMainService.correctArticle(ArticleMain.builder().articleDto(articleMainDto).build());
        return RELATIVE_REDIRECT_PATH + fromPath(UPDATE_ARTICLE_MAIN_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(articleMainDto.getName())).build().toUriString();
    }

    @GetMapping(UPDATE_ARTICLE_MAIN_PATH + FINISH_PATH)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyArticleMain(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT);
        model.addAttribute(REPEAT_PATH, UPDATE_ARTICLE_MAIN_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_ARTICLE_MAIN_VIEW + VIEW_FINISH;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_ARTICLE_MAIN_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String processRidArticleMain(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT);
        return REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS;
    }

    @PostMapping(REMOVE_ARTICLE_MAIN_PATH)
    public String submitRidArticleMain(@RequestParam String numberOrName, Model model) {
        Optional<ArticleMain> articleOrEmpty = articleMainService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME,
                    REMOVE_PROCESS_LAYOUT, NOT_FOUND_ARTICLE_MAIN_ERROR, model);
            return REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS;
        }

        if (NUMBER_PATTERN.matcher(numberOrName).matches()) {
            numberOrName = articleMainService.findArticleByNumber(Long.parseLong(numberOrName)).orElseThrow().getName();
        }
        articleMainService.removeArticleByName(numberOrName);
        return RELATIVE_REDIRECT_PATH + fromPath(REMOVE_ARTICLE_MAIN_PATH + FINISH_PATH).queryParam(NAME, encodeWithUTF8(numberOrName)).build().toUriString();
    }

    @GetMapping(REMOVE_ARTICLE_MAIN_PATH + FINISH_PATH)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidArticleMain(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT);
        model.addAttribute(REPEAT_PATH, REMOVE_ARTICLE_MAIN_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_ARTICLE_MAIN_VIEW + VIEW_FINISH;
    }
}