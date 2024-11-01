package site.hixview.domain.validator.article.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.home.ArticleMain;
import site.hixview.domain.entity.home.dto.ArticleMainDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestPath.ADD_ARTICLE_MAIN_PATH;

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

    private ResultActions expectAddProcessStatusViewLayoutPathError(ResultActions resultActions) throws Exception {
        return resultActions.andExpectAll(status().isOk(),
                view().name(addArticleMainProcessPage),
                model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                model().attribute(ERROR, (String) null),
                model().attribute(CLASSIFICATIONS, Classification.values()));
    }

    private ResultActions expectUpdateProcessStatusViewLayoutPathError(ResultActions resultActions) throws Exception {
        return resultActions.andExpectAll(status().isOk(),
                view().name(modifyArticleMainProcessPage),
                model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                model().attribute(ERROR, (String) null),
                model().attribute(CLASSIFICATIONS, Classification.values()));
    }

    @DisplayName("기사 DB에 등록되지 않은 기사명을 사용하는 기사 메인 추가")
    @Test
    void notRegisteredArticleMainAdd() throws Exception {
        // given & when
        ArticleMain notRegisteredArticle = testCompanyArticleMain;
        ArticleMainDto notRegisteredArticleDto = notRegisteredArticle.toDto();
        when(companyArticleService.findArticleByName(notRegisteredArticle.getName())).thenReturn(Optional.empty());
        when(industryArticleService.findArticleByName(notRegisteredArticle.getImagePath())).thenReturn(Optional.empty());

        // then
        assertThat(requireNonNull(expectAddProcessStatusViewLayoutPathError(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_PATH, notRegisteredArticleDto)))
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
            assertThat(requireNonNull(expectAddProcessStatusViewLayoutPathError(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_PATH, articleDto)))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(articleDto);
        }
    }

    @DisplayName("기사 메인명 또는 기사 이미지 경로까지 변경을 시도하는, 기사 메인 변경")
    @Test
    void changeNameOrImagePathArticleMainModify() throws Exception {
        // given
        when(articleMainService.findArticleByName(testCompanyArticleMain.getName())).thenReturn(Optional.empty());
        when(articleMainService.findArticleByImagePath(testCompanyArticleMain.getImagePath())).thenReturn(Optional.empty());
        when(articleMainService.registerArticle(testCompanyArticleMain)).thenReturn(testCompanyArticleMain);
        ArticleMain article = articleMainService.registerArticle(testCompanyArticleMain);

        // when
        articleMainService.registerArticle(testCompanyArticleMain);

        // then
        requireNonNull(expectUpdateProcessStatusViewLayoutPathError(mockMvc.perform(postWithArticleMain(modifyArticleMainFinishUrl,
                ArticleMain.builder().article(article).name(testIndustryArticleMain.getName()).build()))));

        requireNonNull(expectUpdateProcessStatusViewLayoutPathError(mockMvc.perform(postWithArticleMain(modifyArticleMainFinishUrl,
                ArticleMain.builder().article(article).imagePath(testIndustryArticleMain.getImagePath()).build()))));
    }
}
