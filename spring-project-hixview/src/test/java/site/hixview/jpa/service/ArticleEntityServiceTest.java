package site.hixview.jpa.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.mapper.ArticleEntityMapperImpl;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.spring.util.ArticleTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@OnlyRealServiceContext
class ArticleEntityServiceTest implements ArticleTestUtils {

    private final ArticleEntityService articleEntityService;
    private final ArticleEntityRepository articleEntityRepository;

    private final ArticleEntityMapperImpl articleEntityMapperImpl = new ArticleEntityMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(ArticleEntityServiceTest.class);

    @Autowired
    ArticleEntityServiceTest(ArticleEntityService articleEntityService, ArticleEntityRepository articleEntityRepository) {
        this.articleEntityService = articleEntityService;
        this.articleEntityRepository = articleEntityRepository;
    }

    @DisplayName("모든 기사 획득")
    @Test
    void getAllTest() {
        // given
        ArticleEntity articleEntity = articleEntityMapperImpl.toArticleEntity(article);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(articleEntityRepository.findAll()).thenReturn(List.of(articleEntity));

        // when
        articleEntityRepository.save(articleEntity);

        // then
        assertThat(articleEntityService.getAll()).isEqualTo(List.of(article));
    }
}