package springsideproject1.springsideproject1build.web.controller.manager;

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
import springsideproject1.springsideproject1build.domain.validation.validator.article.ArticleMainAddValidator;
import springsideproject1.springsideproject1build.domain.validation.validator.article.ArticleMainModifyValidator;

import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.NUMBER_REGEX_PATTERN;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.VALUE;
import static springsideproject1.springsideproject1build.util.MainUtils.decodeWithUTF8;
import static springsideproject1.springsideproject1build.util.MainUtils.encodeWithUTF8;

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
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_PATH);
        model.addAttribute(ARTICLE, new ArticleMainDto());
        return ADD_COMPANY_ARTICLE_MAIN_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(ADD_ARTICLE_MAIN_URL)
    public String submitAddArticleMain(@ModelAttribute(ARTICLE) @Validated ArticleMainDto articleMainDto,
                                          BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        // TODO: 추후에 요청에 대한 필터 및 인터셉터 도입 예정
        if (articleMainDto.getName() != null) {
            articleMainDto.setName(articleMainDto.getName().strip());
        }

        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, BEAN_VALIDATION_ERROR, model);
            return ADD_COMPANY_ARTICLE_MAIN_VIEW + VIEW_PROCESS_SUFFIX;
        }

        addValidator.validate(articleMainDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_PATH, null, model);
            return ADD_COMPANY_ARTICLE_MAIN_VIEW + VIEW_PROCESS_SUFFIX;
        }

        articleMainService.registerArticle(ArticleMain.builder().articleDto(articleMainDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(articleMainDto.getName()));
        return URL_REDIRECT_PREFIX + ADD_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(ADD_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddArticleMain(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_COMPANY_ARTICLE_MAIN_VIEW + VIEW_FINISH_SUFFIX;
    }

    /**
     * See
     */
    @GetMapping(SELECT_ARTICLE_MAIN_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeArticleMains(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_PATH);
        model.addAttribute("articleMains", articleMainService.findArticles());
        return MANAGER_SELECT_VIEW + "company-article-mains-page";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_ARTICLE_MAIN_URL)
	@ResponseStatus(HttpStatus.OK)
	public String initiateModifyArticleMain(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
		return UPDATE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
	}

    @PostMapping(UPDATE_ARTICLE_MAIN_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyArticleMain(@RequestParam String numberOrName, Model model) {
        Optional<ArticleMain> articleOrEmpty = articleMainService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(ERRORS_ARE + NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME,
                    UPDATE_PROCESS_PATH, NOT_FOUND_COMPANY_ARTICLE_MAIN_ERROR, model);
            return UPDATE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS_SUFFIX;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_PATH);
        model.addAttribute("updateUrl", UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX);
        model.addAttribute(ARTICLE, articleOrEmpty.orElseThrow().toDto());
        return UPDATE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    }

    @PostMapping(UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX)
    public String submitModifyArticleMain(@ModelAttribute(ARTICLE) @Validated ArticleMainDto articleMainDto,
                                                 BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        // TODO: 추후에 요청에 대한 필터 및 인터셉터 도입 예정
        if (articleMainDto.getName() != null) {
            articleMainDto.setName(articleMainDto.getName().strip());
        }

        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_PATH, BEAN_VALIDATION_ERROR, model);
            model.addAttribute("updateUrl", UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX);
            return UPDATE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
        }

        modifyValidator.validate(articleMainDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_PATH, null, model);
            model.addAttribute("updateUrl", UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX);
            return UPDATE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
        }

        articleMainService.correctArticle(ArticleMain.builder().articleDto(articleMainDto).build());
        redirect.addAttribute(NAME, encodeWithUTF8(articleMainDto.getName()));
        return URL_REDIRECT_PREFIX + UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyArticleMain(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_FINISH_SUFFIX;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_ARTICLE_MAIN_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidArticleMain(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_PATH);
        return REMOVE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @PostMapping(REMOVE_ARTICLE_MAIN_URL)
    public String submitRidArticleMain(RedirectAttributes redirect, @RequestParam String numberOrName, Model model) {
        Optional<ArticleMain> articleOrEmpty = articleMainService.findArticleByNumberOrName(numberOrName);
        if (articleOrEmpty.isEmpty()) {
            finishForRollback(ERRORS_ARE + NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME,
                    REMOVE_PROCESS_PATH, NOT_FOUND_COMPANY_ARTICLE_MAIN_ERROR, model);
            return REMOVE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_PROCESS_SUFFIX;
        }

        if (NUMBER_REGEX_PATTERN.matcher(numberOrName).matches()) {
            numberOrName = articleMainService.findArticleByNumber(Long.parseLong(numberOrName)).orElseThrow().getName();
        }
        articleMainService.removeArticleByName(numberOrName);
        redirect.addAttribute(NAME, encodeWithUTF8(numberOrName));
        return URL_REDIRECT_PREFIX + REMOVE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX;
    }

    @GetMapping(REMOVE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidArticleMain(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_PATH);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_FINISH_SUFFIX;
    }

    /**
     * Other private methods
     */
    // Handle Error
    private void finishForRollback(String logMessage, String layoutPath, String error, Model model) {
        log.error(ERRORS_ARE, logMessage);
        model.addAttribute(LAYOUT_PATH, layoutPath);
        model.addAttribute(ERROR, error);
    }
}