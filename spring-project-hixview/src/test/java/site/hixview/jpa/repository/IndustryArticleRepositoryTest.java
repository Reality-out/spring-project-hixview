package site.hixview.jpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.IndustryArticleTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class IndustryArticleRepositoryTest implements IndustryArticleTestUtils {

    @Autowired
    private IndustryArticleRepository industryArticleRepository;

    private static final Logger log = LoggerFactory.getLogger(IndustryArticleRepositoryTest.class);

    @DisplayName("JpaRepository 산업 기사 연결")
    @Test
    void connectTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("날짜로 산업 기사 찾기")
    @Test
    void findByDateTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByDate(article.getDate())).isEqualTo(List.of(article));
    }

    @DisplayName("날짜 범위로 산업 기사 찾기")
    @Test
    void findByDateBetweenTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();
        List<IndustryArticleEntity> articleList = List.of(article);

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByDateBetween(article.getDate(), article.getDate())).isEqualTo(articleList);
        assertThat(industryArticleRepository.findByDateBetween(article.getDate().minusDays(10), article.getDate().plusDays(10))).isEqualTo(articleList);
    }

    @DisplayName("대상 국가로 산업 기사 찾기")
    @Test
    void findBySubjectCountryTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findBySubjectCountry(article.getSubjectCountry())).isEqualTo(List.of(article));
    }

    @DisplayName("중요성으로 산업 기사 찾기")
    @Test
    void findByImportanceTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();
        List<IndustryArticleEntity> articleList = List.of(article);

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByImportance(article.getImportance())).isEqualTo(articleList);
    }

    @DisplayName("번호로 산업 기사 찾기")
    @Test
    void findByNumberTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("이름으로 산업 기사 찾기")
    @Test
    void findByNameTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByName(article.getName()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("링크로 산업 기사 찾기")
    @Test
    void findByLinkTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByLink(article.getLink()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("1차 업종으로 산업 기사 찾기")
    @Test
    void findByFirstCategoryTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();
        List<IndustryArticleEntity> articleList = List.of(article);

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByFirstCategory(article.getFirstCategory())).isEqualTo(articleList);
    }

    @DisplayName("번호로 산업 기사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();
        industryArticleRepository.save(article);

        // when
        industryArticleRepository.deleteByNumber(article.getNumber());

        // then
        assertThat(industryArticleRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 경제 기사 확인")
    @Test
    void existsByNumberTest() {
        // given
        IndustryArticleEntity article = createIndustryArticle();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.existsByNumber(article.getNumber())).isEqualTo(true);
    }
}