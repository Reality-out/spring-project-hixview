package site.hixview.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.repository.CompanyArticleRepository;
import site.hixview.support.context.OnlyRealServiceContext;
import site.hixview.support.util.CompanyArticleTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_COMPANY_ARTICLE_NAME;
import static site.hixview.domain.vo.ExceptionMessage.NO_COMPANY_ARTICLE_WITH_THAT_NAME;
import static site.hixview.domain.vo.Word.NUMBER;

@OnlyRealServiceContext
class CompanyArticleServiceTest implements CompanyArticleTestUtils {

    @Autowired
    private CompanyArticleService articleService;

    @Autowired
    private CompanyArticleRepository companyArticleRepository;

    @DisplayName("기사 번호와 이름으로 기업 기사 찾기")
    @Test
    void findCompanyArticleWithNumberAndNameTest() {
        // given
        CompanyArticle article = CompanyArticle.builder().article(testCompanyArticle).number(1L).build();
        when(companyArticleRepository.getArticleByNumber(article.getNumber())).thenReturn(Optional.of(article));
        when(companyArticleRepository.getArticleByName(article.getName()))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(companyArticleRepository.saveArticle(article)).thenReturn(1L);

        // when
        article = articleService.registerArticle(article);

        // then
        for (String numberOrName : List.of(String.valueOf(article.getNumber()), article.getName())) {
            assertThat(articleService.findArticleByNumberOrName(numberOrName).orElseThrow()).isEqualTo(article);
        }
    }

    @DisplayName("기업 기사들 동시 등록")
    @Test
    void registerCompanyArticlesTest() {
        // given & when
        CompanyArticle firstArticle = testCompanyArticle;
        CompanyArticle secondArticle = testNewCompanyArticle;
        when(companyArticleRepository.getArticles()).thenReturn(List.of(firstArticle, secondArticle));
        when(companyArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());
        when(companyArticleRepository.saveArticle(firstArticle)).thenReturn(1L);
        when(companyArticleRepository.saveArticle(secondArticle)).thenReturn(2L);
        articleService.registerArticles(firstArticle, secondArticle);

        // then
        assertThat(articleService.findArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(firstArticle, secondArticle));
    }

    @DisplayName("기업 기사 등록")
    @Test
    void registerCompanyArticleTest() {
        // given
        CompanyArticle article = CompanyArticle.builder().article(testCompanyArticle).number(1L).build();
        when(companyArticleRepository.getArticles()).thenReturn(List.of(article));
        when(companyArticleRepository.getArticleByName(article.getName())).thenReturn(Optional.empty());
        when(companyArticleRepository.saveArticle(article)).thenReturn(1L);

        // when
        article = articleService.registerArticle(article);

        // then
        assertThat(articleService.findArticles().getFirst()).isEqualTo(article);
    }

    @DisplayName("기업 기사 중복 이름으로 등록")
    @Test
    void registerDuplicatedCompanyArticleWithSameNameTest() {
        // given
        CompanyArticle article = testCompanyArticle;
        String duplicatedName = article.getName();
        when(companyArticleRepository.getArticleByName(duplicatedName))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(companyArticleRepository.saveArticle(article)).thenReturn(1L);

        // when
        AlreadyExistException e = assertThrows(AlreadyExistException.class,
                () -> articleService.registerArticles(article,
                        CompanyArticle.builder().article(testNewCompanyArticle).name(duplicatedName).build()));

        // then
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_COMPANY_ARTICLE_NAME);
    }

    @DisplayName("기업 기사 존재하지 않는 이름으로 수정")
    @Test
    void correctCompanyArticleWithFaultNameTest() {
        // given
        when(companyArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.correctArticle(testCompanyArticle));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_COMPANY_ARTICLE_WITH_THAT_NAME);
    }

    @DisplayName("기업 기사 제거")
    @Test
    void removeCompanyArticleTest() {
        // given
        CompanyArticle article = testCompanyArticle;
        String name = article.getName();
        when(companyArticleRepository.getArticles()).thenReturn(Collections.emptyList());
        when(companyArticleRepository.getArticleByName(name))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(article));
        when(companyArticleRepository.saveArticle(article)).thenReturn(1L);
        doNothing().when(companyArticleRepository).deleteArticleByName(name);

        articleService.registerArticle(article);

        // when
        articleService.removeArticleByName(name);

        // then
        assertThat(articleService.findArticles()).isEmpty();
    }

    @DisplayName("기업 기사 존재하지 않는 이름으로 제거")
    @Test
    void removeCompanyArticleByFaultNameTest() {
        // given
        when(companyArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());

        // when
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> articleService.removeArticleByName(INVALID_VALUE));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_COMPANY_ARTICLE_WITH_THAT_NAME);
    }
}