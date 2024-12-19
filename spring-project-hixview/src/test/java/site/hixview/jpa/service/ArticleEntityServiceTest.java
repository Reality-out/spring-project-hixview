package site.hixview.jpa.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.ArticleEntityTestUtils;
import site.hixview.support.spring.util.ArticleTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@OnlyRealServiceContext
@Slf4j
class ArticleEntityServiceTest implements ArticleEntityTestUtils, ArticleTestUtils {

    private final ArticleEntityService articleEntityService;
    private final ArticleEntityRepository articleEntityRepository;

    @Autowired
    ArticleEntityServiceTest(ArticleEntityService articleEntityService, ArticleEntityRepository articleEntityRepository) {
        this.articleEntityService = articleEntityService;
        this.articleEntityRepository = articleEntityRepository;
    }

    @DisplayName("모든 기사 획득")
    @Test
    void getAllTest() {
        // given
        ArticleEntity articleEntity = createNumberedArticleEntity();
        when(articleEntityRepository.save(any())).thenReturn(articleEntity);
        when(articleEntityRepository.findAll()).thenReturn(List.of(articleEntity));

        // when
        articleEntityRepository.save(articleEntity);

        // then
        assertThat(articleEntityService.getAll()).isEqualTo(List.of(article));
    }

    @DisplayName("번호로 기사 획득")
    @Test
    void getByNumberTest() {
        // given
        ArticleEntity articleEntity = createNumberedArticleEntity();
        Long number = articleEntity.getNumber();
        when(articleEntityRepository.save(any())).thenReturn(articleEntity);
        when(articleEntityRepository.findByNumber(number)).thenReturn(Optional.of(articleEntity));

        // when
        articleEntityRepository.save(articleEntity);

        // then
        assertThat(articleEntityService.getByNumber(number).orElseThrow()).isEqualTo(article);
    }
}