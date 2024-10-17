package site.hixview.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.repository.IndustryArticleRepository;
import site.hixview.support.context.OnlyRealServiceContext;
import site.hixview.support.util.IndustryArticleTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_INDUSTRY_ARTICLE_NAME;
import static site.hixview.domain.vo.ExceptionMessage.NO_INDUSTRY_ARTICLE_WITH_THAT_NAME;
import static site.hixview.domain.vo.name.EntityName.Article.NUMBER;

@OnlyRealServiceContext
class IndustryArticleServiceJdbcTest implements IndustryArticleTestUtils {

    @Autowired
    private IndustryArticleService articleService;

    @Autowired
    private IndustryArticleRepository industryArticleRepository;

    private final String[] fieldNames = IndustryArticle.getFieldNamesWithNoNumber();

    @DisplayName("기사 번호와 이름으로 산업 기사 찾기")
    @Test
    void findIndustryArticleWithNumberAndNameTest() {
        // given
        IndustryArticle article = IndustryArticle.builder().article(testIndustryArticle).number(1L).build();
        when(industryArticleRepository.getArticleByNumber(article.getNumber())).thenReturn(Optional.of(article));
        when(industryArticleRepository.getArticleByName(article.getName()))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(industryArticleRepository.saveArticle(article)).thenReturn(1L);

        // when
        article = articleService.registerArticle(article);

        // then
        for (String str : List.of(String.valueOf(article.getNumber()), article.getName())) {
            assertThat(articleService.findArticleByNumberOrName(str).orElseThrow())
                    .usingRecursiveComparison()
                    .comparingOnlyFields(fieldNames)
                    .isEqualTo(article);
        }
    }

    @DisplayName("산업 기사들 동시 등록")
    @Test
    void registerIndustryArticlesTest() {
        // given & when
        IndustryArticle firstArticle = testIndustryArticle;
        IndustryArticle secondArticle = testNewIndustryArticle;
        when(industryArticleRepository.getArticles()).thenReturn(List.of(firstArticle, secondArticle));
        when(industryArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());
        when(industryArticleRepository.saveArticle(firstArticle)).thenReturn(1L);
        when(industryArticleRepository.saveArticle(secondArticle)).thenReturn(2L);
        articleService.registerArticles(firstArticle, secondArticle);

        // then
        assertThat(articleService.findArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(firstArticle, secondArticle));
    }

    @DisplayName("산업 기사 등록")
    @Test
    void registerIndustryArticleTest() {
        // given
        IndustryArticle article = IndustryArticle.builder().article(testIndustryArticle).number(1L).build();
        when(industryArticleRepository.getArticles()).thenReturn(List.of(article));
        when(industryArticleRepository.getArticleByName(article.getName())).thenReturn(Optional.empty());
        when(industryArticleRepository.saveArticle(article)).thenReturn(1L);

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(articleService.findArticles())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(List.of(article));
    }

    @DisplayName("산업 기사 중복 이름으로 등록")
    @Test
    void registerDuplicatedIndustryArticleWithSameNameTest() {
        // given
        IndustryArticle article = testIndustryArticle;
        String duplicatedName = article.getName();
        when(industryArticleRepository.getArticleByName(duplicatedName))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(industryArticleRepository.saveArticle(article)).thenReturn(1L);

        // when
        AlreadyExistException e = assertThrows(AlreadyExistException.class,
                () -> articleService.registerArticles(article,
                        IndustryArticle.builder().article(testNewIndustryArticle).name(duplicatedName).build()));

        // then
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_INDUSTRY_ARTICLE_NAME);
    }

    @DisplayName("산업 기사 존재하지 않는 이름으로 수정")
    @Test
    void correctIndustryArticleWithFaultNameTest() {
        // given
        when(industryArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.correctArticle(testIndustryArticle));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_INDUSTRY_ARTICLE_WITH_THAT_NAME);
    }

    @DisplayName("산업 기사 제거")
    @Test
    void removeIndustryArticleTest() {
        // given
        IndustryArticle article = testIndustryArticle;
        String name = article.getName();
        when(industryArticleRepository.getArticles()).thenReturn(Collections.emptyList());
        when(industryArticleRepository.getArticleByName(name))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(industryArticleRepository.saveArticle(article)).thenReturn(1L);
        doNothing().when(industryArticleRepository).deleteArticleByName(name);
        articleService.registerArticle(article);

        // when
        articleService.removeArticleByName(name);

        // then
        assertThat(articleService.findArticles()).isEmpty();
    }

    @DisplayName("산업 기사 존재하지 않는 이름으로 제거")
    @Test
    void removeIndustryArticleByFaultNameTest() {
        // given
        when(industryArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.removeArticleByName(INVALID_VALUE));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_INDUSTRY_ARTICLE_WITH_THAT_NAME);
    }
}