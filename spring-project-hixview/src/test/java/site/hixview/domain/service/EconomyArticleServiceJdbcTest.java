package site.hixview.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.repository.EconomyArticleRepository;
import site.hixview.support.context.OnlyRealServiceContext;
import site.hixview.support.util.EconomyArticleTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_ECONOMY_ARTICLE_NAME;
import static site.hixview.domain.vo.ExceptionMessage.NO_ECONOMY_ARTICLE_WITH_THAT_NAME;
import static site.hixview.domain.vo.Word.NUMBER;

@OnlyRealServiceContext
class EconomyArticleServiceJdbcTest implements EconomyArticleTestUtils {

    @Autowired
    private EconomyArticleService articleService;

    @Autowired
    private EconomyArticleRepository economyArticleRepository;

    private final String[] fieldNames = EconomyArticle.getFieldNamesWithNoNumber();

    @DisplayName("기사 번호와 이름으로 경제 기사 찾기")
    @Test
    void findEconomyArticleWithNumberAndNameTest() {
        // given
        EconomyArticle article = EconomyArticle.builder().article(testEconomyArticle).number(1L).build();
        when(economyArticleRepository.getArticleByNumber(article.getNumber())).thenReturn(Optional.of(article));
        when(economyArticleRepository.getArticleByName(article.getName()))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(economyArticleRepository.saveArticle(article)).thenReturn(1L);

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

    @DisplayName("경제 기사들 동시 등록")
    @Test
    void registerEconomyArticlesTest() {
        // given & when
        EconomyArticle firstArticle = testEconomyArticle;
        EconomyArticle secondArticle = testNewEconomyArticle;
        when(economyArticleRepository.getArticles()).thenReturn(List.of(firstArticle, secondArticle));
        when(economyArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());
        when(economyArticleRepository.saveArticle(firstArticle)).thenReturn(1L);
        when(economyArticleRepository.saveArticle(secondArticle)).thenReturn(2L);
        articleService.registerArticles(firstArticle, secondArticle);

        // then
        assertThat(articleService.findArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(firstArticle, secondArticle));
    }

    @DisplayName("경제 기사 등록")
    @Test
    void registerEconomyArticleTest() {
        // given
        EconomyArticle article = EconomyArticle.builder().article(testEconomyArticle).number(1L).build();
        when(economyArticleRepository.getArticles()).thenReturn(List.of(article));
        when(economyArticleRepository.getArticleByName(article.getName())).thenReturn(Optional.empty());
        when(economyArticleRepository.saveArticle(article)).thenReturn(1L);

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(articleService.findArticles())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(List.of(article));
    }

    @DisplayName("경제 기사 중복 이름으로 등록")
    @Test
    void registerDuplicatedEconomyArticleWithSameNameTest() {
        // given
        EconomyArticle article = testEconomyArticle;
        String duplicatedName = article.getName();
        when(economyArticleRepository.getArticleByName(duplicatedName))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(economyArticleRepository.saveArticle(article)).thenReturn(1L);

        // when
        AlreadyExistException e = assertThrows(AlreadyExistException.class,
                () -> articleService.registerArticles(article,
                        EconomyArticle.builder().article(testNewEconomyArticle).name(duplicatedName).build()));

        // then
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_ECONOMY_ARTICLE_NAME);
    }

    @DisplayName("경제 기사 존재하지 않는 이름으로 수정")
    @Test
    void correctEconomyArticleWithFaultNameTest() {
        // given
        when(economyArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.correctArticle(testEconomyArticle));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_ECONOMY_ARTICLE_WITH_THAT_NAME);
    }

    @DisplayName("경제 기사 제거")
    @Test
    void removeEconomyArticleTest() {
        // given
        EconomyArticle article = testEconomyArticle;
        String name = article.getName();
        when(economyArticleRepository.getArticles()).thenReturn(Collections.emptyList());
        when(economyArticleRepository.getArticleByName(name))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(economyArticleRepository.saveArticle(article)).thenReturn(1L);
        doNothing().when(economyArticleRepository).deleteArticleByName(name);
        articleService.registerArticle(article);

        // when
        articleService.removeArticleByName(name);

        // then
        assertThat(articleService.findArticles()).isEmpty();
    }

    @DisplayName("경제 기사 존재하지 않는 이름으로 제거")
    @Test
    void removeEconomyArticleByFaultNameTest() {
        // given
        when(economyArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.removeArticleByName(INVALID_VALUE));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_ECONOMY_ARTICLE_WITH_THAT_NAME);
    }
}