package site.hixview.domain.validator.article.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.article.ArticleMain;
import site.hixview.domain.entity.article.ArticleMainDto;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.support.context.RealControllerAndValidatorContext;
import site.hixview.support.util.ArticleMainTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE;

@RealControllerAndValidatorContext
class ArticleMainValidationErrorTest implements ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleMainService articleMainService;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private IndustryArticleService industryArticleService;

    @DisplayName("기사 DB에 등록되지 않은 기사명을 사용하는 기사 메인 추가")
    @Test
    void notRegisteredArticleMainAdd() throws Exception {
        // given & when
        ArticleMain notRegisteredArticle = testCompanyArticleMain;
        ArticleMainDto notRegisteredArticleDto = notRegisteredArticle.toDto();
        when(companyArticleService.findArticleByName(notRegisteredArticle.getName())).thenReturn(Optional.empty());
        when(industryArticleService.findArticleByName(notRegisteredArticle.getImagePath())).thenReturn(Optional.empty());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, notRegisteredArticleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(notRegisteredArticleDto);
    }

    @DisplayName("중복 기사 메인명 또는 이미지 경로를 사용하는 기사 메인 추가")
    @Test
    void duplicatedNameOrImagePathArticleMainAdd() throws Exception {
        // given
        ArticleMain article = testCompanyArticleMain;
        when(articleMainService.findArticleByName(article.getName())).thenReturn(Optional.of(article));
        when(articleMainService.findArticleByImagePath(article.getImagePath())).thenReturn(Optional.of(article));
        when(articleMainService.registerArticle(article)).thenReturn(article);

        ArticleMainDto articleDtoDuplicatedName = createTestNewCompanyArticleMainDto();
        articleDtoDuplicatedName.setName(article.getName());
        ArticleMainDto articleDtoDuplicatedImagePath = createTestNewCompanyArticleMainDto();
        articleDtoDuplicatedImagePath.setImagePath(article.getImagePath());

        // when
        articleMainService.registerArticle(article);

        // then
        for (ArticleMainDto articleDto : List.of(articleDtoDuplicatedName, articleDtoDuplicatedImagePath)) {
            assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                    .andExpectAll(view().name(addArticleMainProcessPage),
                            model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                            model().attribute(ERROR, (String) null))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(articleDto);
        }
    }

    @DisplayName("기사 메인명까지 변경을 시도하는 기사 메인 변경")
    @Test
    void changeNameArticleMainModify() throws Exception {
        // given
        ArticleMainDto articleDto = testCompanyArticleMain.toDto();
        ArticleMain article = ArticleMain.builder().articleDto(articleDto).build();
        when(articleMainService.findArticleByName(article.getName())).thenReturn(Optional.empty());
        when(articleMainService.registerArticle(article)).thenReturn(article);

        articleMainService.registerArticle(article);

        // when
        articleDto.setName(testNewCompanyArticleMain.getName());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(
                UPDATE_ARTICLE_MAIN_URL + FINISH_URL, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
