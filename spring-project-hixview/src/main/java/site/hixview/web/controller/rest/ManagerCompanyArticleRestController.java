package site.hixview.web.controller.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.CompanyArticleAddComplexValidator;
import site.hixview.domain.validation.validator.CompanyArticleAddSimpleValidator;
import site.hixview.domain.validation.validator.CompanyArticleModifyValidator;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_COMPANY_ARTICLE_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_COMPANY_ARTICLE_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_COMPANY_ARTICLE_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_COMPANY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE;
import static site.hixview.domain.vo.name.ExceptionName.BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.ViewName.VIEW_AFTER_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_SINGLE_PROCESS;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;
import static site.hixview.util.ControllerUtils.finishForRollback;

@Controller
@RequiredArgsConstructor
public class ManagerCompanyArticleRestController {

    private final CompanyArticleService articleService;
    private final CompanyService companyService;

    private final Validator defaultValidator;
    private final CompanyArticleAddComplexValidator complexValidator;
    private final CompanyArticleAddSimpleValidator simpleValidator;
    private final CompanyArticleModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerCompanyArticleRestController.class);

    /**
     * Add - Single
     */
    @PostMapping(ADD_SINGLE_COMPANY_ARTICLE_URL)
    public String submitAddCompanyArticle(@ModelAttribute(ARTICLE) @Validated CompanyArticleDto articleDto,
                                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
        }

        complexValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), ADD_PROCESS_LAYOUT, null, model);
            return ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
        }

        articleService.registerArticle(CompanyArticle.builder().articleDto(articleDto).build());
        return REDIRECT_URL + fromPath(ADD_SINGLE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(articleDto.getName())).build().toUriString();
    }

    /**
     * Modify
     */
    @PostMapping(UPDATE_COMPANY_ARTICLE_URL + FINISH_URL)
    public String submitModifyCompanyArticle(@ModelAttribute(ARTICLE) @Validated CompanyArticleDto articleDto,
                                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, BEAN_VALIDATION_ERROR, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + FINISH_URL);
            return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
        }

        modifyValidator.validate(articleDto, bindingResult);
        if (bindingResult.hasErrors()) {
            finishForRollback(bindingResult.getAllErrors().toString(), UPDATE_PROCESS_LAYOUT, null, model);
            model.addAttribute("updateUrl", UPDATE_COMPANY_ARTICLE_URL + FINISH_URL);
            return UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
        }

        articleService.correctArticle(CompanyArticle.builder().articleDto(articleDto).build());
        return REDIRECT_URL + fromPath(UPDATE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(articleDto.getName())).build().toUriString();
    }
}