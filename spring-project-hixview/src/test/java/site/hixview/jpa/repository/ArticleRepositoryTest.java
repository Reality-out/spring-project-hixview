package site.hixview.jpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.ArticleTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext(resetTables = {ArticleEntity.class, PressEntity.class})
class ArticleRepositoryTest implements ArticleTestUtils {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    private static final Logger log = LoggerFactory.getLogger(ArticleRepositoryTest.class);

    @DisplayName("JpaRepository 기사 연결")
    @Test
    void connectTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("날짜로 기사 찾기")
    @Test
    void findByDateTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.findByDate(article.getDate()).getFirst()).isEqualTo(article);
    }

    @DisplayName("날짜 범위로 기사 찾기")
    @Test
    void findByDateBetweenTest() {
        // given
        ArticleEntity articleFirst = createArticle();
        ArticleEntity articleLast = createAnotherArticle();
        List<ArticleEntity> articleList = List.of(articleFirst, articleLast);

        // when
        articleRepository.saveAll(articleList);

        // then
        assertThat(articleRepository.findByDateBetween(articleFirst.getDate(), articleLast.getDate())).isEqualTo(articleList);
    }

    @DisplayName("분류로 기사 찾기")
    @Test
    void findByClassificationTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.findByClassification(article.getClassification()).getFirst()).isEqualTo(article);
    }

    @DisplayName("대상 국가로 기사 찾기")
    @Test
    void findBySubjectCountryTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.findBySubjectCountry(article.getSubjectCountry()).getFirst()).isEqualTo(article);
    }

    @DisplayName("중요성으로 기사 찾기")
    @Test
    void findByImportanceTest() {
        // given
        ArticleEntity article = createArticle();
        ArticleEntity anotherArticle = createAnotherArticle();
        List<ArticleEntity> articleList = List.of(article, anotherArticle);

        // when
        articleRepository.saveAll(articleList);

        // then
        assertThat(articleRepository.findByImportance(article.getImportance())).isEqualTo(articleList);
    }

    @DisplayName("번호로 기사 찾기")
    @Test
    void findByNumberTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("이름으로 기사 찾기")
    @Test
    void findByNameTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.findByName(article.getName()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("링크로 기사 찾기")
    @Test
    void findByLinkTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.findByLink(article.getLink()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("번호로 기사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        ArticleEntity article = createArticle();
        articleRepository.save(article);

        // when
        articleRepository.deleteByNumber(article.getNumber());

        // then
        assertThat(articleRepository.findAll()).isEmpty();
    }
}