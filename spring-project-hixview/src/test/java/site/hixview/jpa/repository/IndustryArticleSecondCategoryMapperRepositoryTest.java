package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryMapperEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.executor.SqlExecutor;
import site.hixview.support.jpa.util.IndustryArticleSecondCategoryMapperTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class IndustryArticleSecondCategoryMapperRepositoryTest implements IndustryArticleSecondCategoryMapperTestUtils {

    @Autowired
    private IndustryArticleSecondCategoryMapperRepository industryArticleMapperRepository;

    @Autowired
    private FirstCategoryRepository firstCategoryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(IndustryArticleSecondCategoryMapperRepositoryTest.class);

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 산업 기사와 2차 업종 간 매퍼 찾기")
    @Test
    void findByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategory();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticle()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategory();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryMapperEntity mapper = new IndustryArticleSecondCategoryMapperEntity(industryArticle, secondCategory);

        // when
        industryArticleMapperRepository.save(mapper);

        // then
        assertThat(industryArticleMapperRepository.findByNumber(mapper.getNumber()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("산업 기사로 산업 기사와 2차 업종 간 매퍼 찾기")
    @Test
    void findByIndustryArticleTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategory();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticle()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategory();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryMapperEntity mapper = new IndustryArticleSecondCategoryMapperEntity(industryArticle, secondCategory);

        // when
        industryArticleMapperRepository.save(mapper);

        // then
        assertThat(industryArticleMapperRepository.findByIndustryArticle(mapper.getIndustryArticle())).isEqualTo(List.of(mapper));
    }

    @DisplayName("2차 업종으로 산업 기사와 2차 업종 간 매퍼 찾기")
    @Test
    void findBySecondCategoryTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategory();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticle()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategory();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryMapperEntity mapper = new IndustryArticleSecondCategoryMapperEntity(industryArticle, secondCategory);

        // when
        industryArticleMapperRepository.save(mapper);

        // then
        assertThat(industryArticleMapperRepository.findBySecondCategory(mapper.getSecondCategory())).isEqualTo(List.of(mapper));
    }

    @DisplayName("번호로 산업 기사와 2차 업종 간 매퍼 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategory();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticle()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategory();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryMapperEntity mapper = new IndustryArticleSecondCategoryMapperEntity(industryArticle, secondCategory);

        // when
        industryArticleMapperRepository.deleteByNumber(mapper.getNumber());

        // then
        assertThat(industryArticleMapperRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 산업 기사와 2차 업종 간 매퍼 확인")
    @Test
    void existsByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategory();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticle()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategory();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryMapperEntity mapper = new IndustryArticleSecondCategoryMapperEntity(industryArticle, secondCategory);

        // when
        industryArticleMapperRepository.save(mapper);

        // then
        assertThat(industryArticleMapperRepository.existsByNumber(mapper.getNumber())).isEqualTo(true);
    }
}