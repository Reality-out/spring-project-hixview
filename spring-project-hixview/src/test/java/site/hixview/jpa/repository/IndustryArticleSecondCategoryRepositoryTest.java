package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.IndustryArticleSecondCategoryEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class IndustryArticleSecondCategoryRepositoryTest implements IndustryArticleSecondCategoryEntityTestUtils {

    private final IndustryArticleSecondCategoryEntityRepository industryArticleMapperRepository;
    private final FirstCategoryEntityRepository firstCategoryRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    IndustryArticleSecondCategoryRepositoryTest(IndustryArticleSecondCategoryEntityRepository industryArticleMapperRepository, FirstCategoryEntityRepository firstCategoryRepository, JdbcTemplate jdbcTemplate) {
        this.industryArticleMapperRepository = industryArticleMapperRepository;
        this.firstCategoryRepository = firstCategoryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 산업 기사와 2차 업종 간 매퍼 찾기")
    @Test
    void findByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategoryEntity();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticleEntity()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryEntity mapper = new IndustryArticleSecondCategoryEntity(industryArticle, secondCategory);

        // when
        industryArticleMapperRepository.save(mapper);

        // then
        assertThat(industryArticleMapperRepository.findByNumber(mapper.getNumber()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("산업 기사로 산업 기사와 2차 업종 간 매퍼 찾기")
    @Test
    void findByIndustryArticleTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategoryEntity();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticleEntity()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryEntity mapper = new IndustryArticleSecondCategoryEntity(industryArticle, secondCategory);

        // when
        industryArticleMapperRepository.save(mapper);

        // then
        assertThat(industryArticleMapperRepository.findByIndustryArticle(mapper.getIndustryArticle())).isEqualTo(List.of(mapper));
    }

    @DisplayName("2차 업종으로 산업 기사와 2차 업종 간 매퍼 찾기")
    @Test
    void findBySecondCategoryTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategoryEntity();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticleEntity()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryEntity mapper = new IndustryArticleSecondCategoryEntity(industryArticle, secondCategory);

        // when
        industryArticleMapperRepository.save(mapper);

        // then
        assertThat(industryArticleMapperRepository.findBySecondCategory(mapper.getSecondCategory())).isEqualTo(List.of(mapper));
    }

    @DisplayName("번호로 산업 기사와 2차 업종 간 매퍼 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategoryEntity();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticleEntity()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryEntity mapper = new IndustryArticleSecondCategoryEntity(industryArticle, secondCategory);
        industryArticleMapperRepository.save(mapper);

        // when
        industryArticleMapperRepository.deleteByNumber(mapper.getNumber());

        // then
        assertThat(industryArticleMapperRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 산업 기사와 2차 업종 간 매퍼 확인")
    @Test
    void existsByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategoryEntity();
        firstCategoryRepository.save(firstCategory);
        IndustryArticleEntity industryArticle = IndustryArticleEntity.builder().industryArticle(createIndustryArticleEntity()).firstCategory(firstCategory).build();
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();
        secondCategory.updateFirstCategory(firstCategory);
        IndustryArticleSecondCategoryEntity mapper = new IndustryArticleSecondCategoryEntity(industryArticle, secondCategory);

        // when
        industryArticleMapperRepository.save(mapper);

        // then
        assertThat(industryArticleMapperRepository.existsByNumber(mapper.getNumber())).isEqualTo(true);
    }
}