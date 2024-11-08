package site.hixview.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.domain.entity.home.ArticleMain;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.repository.ArticleMainRepository;
import site.hixview.support.context.OnlyRealServiceContext;
import site.hixview.support.util.ArticleMainTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_ARTICLE_MAIN_NAME;
import static site.hixview.domain.vo.ExceptionMessage.NO_ARTICLE_MAIN_WITH_THAT_NAME;

@OnlyRealServiceContext
class ArticleMainServiceTest implements ArticleMainTestUtils {

    @Autowired
    private ArticleMainService articleMainService;

    @Autowired
    private ArticleMainRepository articleMainRepository;

    @DisplayName("기사 메인들 동시 등록")
    @Test
    public void registerArticleMainsTest() {
        // given
        ArticleMain firstArticle = testCompanyArticleMain;
        ArticleMain secondArticle = testNewCompanyArticleMain;
        when(articleMainRepository.getArticles()).thenReturn(List.of(firstArticle, secondArticle));
        when(articleMainRepository.getArticleByName(any())).thenReturn(Optional.empty());
        when(articleMainRepository.saveArticle(firstArticle)).thenReturn(1L);
        when(articleMainRepository.saveArticle(secondArticle)).thenReturn(2L);

        // when
        articleMainService.registerArticles(firstArticle, secondArticle);

        // then
        assertThat(articleMainService.findArticles()).isEqualTo(List.of(firstArticle, secondArticle));
    }

    @DisplayName("기사 메인 등록")
    @Test
    public void registerArticleMainTest() {
        // given
        ArticleMain article = ArticleMain.builder().article(testCompanyArticleMain).number(1L).build();
        when(articleMainRepository.getArticles()).thenReturn(List.of(article));
        when(articleMainRepository.getArticleByName(article.getName())).thenReturn(Optional.empty());
        when(articleMainRepository.saveArticle(article)).thenReturn(1L);

        // when
        article = articleMainService.registerArticle(article);

        // then
        assertThat(articleMainService.findArticles()).isEqualTo(List.of(article));
    }

    @DisplayName("기사 메인 중복 이름으로 등록")
    @Test
    public void registerDuplicatedArticleMainWithSameNameTest() {
        // given
        ArticleMain firstRegisteredArticle = testCompanyArticleMain;
        when(articleMainRepository.getArticleByName(firstRegisteredArticle.getName()))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(firstRegisteredArticle));
        when(articleMainRepository.saveArticle(firstRegisteredArticle)).thenReturn(1L);

        // when
        AlreadyExistException e = assertThrows(AlreadyExistException.class,
                () -> articleMainService.registerArticles(firstRegisteredArticle,
                        ArticleMain.builder().article(firstRegisteredArticle).name(testCompanyArticleMain.getName()).build()));

        // then
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_ARTICLE_MAIN_NAME);
    }

    @DisplayName("기사 메인 존재하지 않는 이름으로 수정")
    @Test
    public void correctArticleMainWithFaultNameTest() {
        // given
        when(articleMainRepository.getArticleByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleMainService.correctArticle(testCompanyArticleMain));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_ARTICLE_MAIN_WITH_THAT_NAME);
    }

    @DisplayName("기사 메인 제거")
    @Test
    public void removeArticleMainTest() {
        // given
        ArticleMain article = testCompanyArticleMain;
        when(articleMainRepository.getArticles()).thenReturn(Collections.emptyList());
        when(articleMainRepository.getArticleByName(article.getName()))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(articleMainRepository.saveArticle(article)).thenReturn(1L);
        doNothing().when(articleMainRepository).deleteArticleByName(article.getName());

        articleMainService.registerArticle(article);

        // when
        articleMainService.removeArticleByName(article.getName());

        // then
        assertThat(articleMainService.findArticles()).isEmpty();
    }

    @DisplayName("기사 메인 존재하지 않는 이름으로 제거")
    @Test
    public void removeArticleMainByFaultNameTest() {
        // given
        when(articleMainRepository.getArticleByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleMainService.removeArticleByName(INVALID_VALUE));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_ARTICLE_MAIN_WITH_THAT_NAME);
    }
}