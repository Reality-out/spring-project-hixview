package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.repository.*;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.IndustryArticleEntityTestUtils;
import site.hixview.support.jpa.util.SecondCategoryEntityTestUtils;
import site.hixview.support.spring.util.IndustryArticleTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class IndustryArticleSecondCategoryEntityMapperTest implements IndustryArticleEntityTestUtils, SecondCategoryEntityTestUtils, IndustryArticleTestUtils {

    private final IndustryArticleSecondCategoryEntityRepository IndustryArticleSecondCategoryEntityRepository;
    private final IndustryArticleEntityRepository industryArticleEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final IndustryArticleSecondCategoryEntityMapper mapperImpl = new IndustryArticleSecondCategoryEntityMapperImpl();

    @Autowired
    IndustryArticleSecondCategoryEntityMapperTest(IndustryArticleSecondCategoryEntityRepository IndustryArticleSecondCategoryEntityRepository, IndustryArticleEntityRepository industryArticleEntityRepository, ArticleEntityRepository articleEntityRepository, IndustryCategoryEntityRepository industryCategoryEntityRepository, FirstCategoryEntityRepository firstCategoryEntityRepository, SecondCategoryEntityRepository secondCategoryEntityRepository, JdbcTemplate jdbcTemplate) {
        this.IndustryArticleSecondCategoryEntityRepository = IndustryArticleSecondCategoryEntityRepository;
        this.industryArticleEntityRepository = industryArticleEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.industryCategoryEntityRepository = industryCategoryEntityRepository;
        this.firstCategoryEntityRepository = firstCategoryEntityRepository;
        this.secondCategoryEntityRepository = secondCategoryEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("엔터티 매퍼 사용 후 IndustryArticleSecondCategory 일관성 보장")
    @Test
    void industryArticleSecondCategoryMappingWithEntityMapper() {
        // given
        IndustryCategoryEntity secondIndustryCategoryEntity = industryCategoryEntityRepository.save(createSecondIndustryCategoryEntity());
        FirstCategoryEntity firstCategoryEntity = firstCategoryEntityRepository.save(createFirstCategoryEntity());

        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        secondCategoryEntity.updateIndustryCategory(secondIndustryCategoryEntity);
        secondCategoryEntity = secondCategoryEntityRepository.save(secondCategoryEntity);

        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        PressEntity pressEntity = createPressEntity();
        IndustryArticleEntity industryArticleEntity = industryArticleEntityRepository.save(
                IndustryArticleEntity.builder()
                        .industryArticle(createIndustryArticleEntity())
                        .press(pressEntity)
                        .article(articleEntity)
                        .firstCategory(firstCategoryEntity).build());
        IndustryArticleSecondCategoryEntity industryArticleSecondCategoryEntity = IndustryArticleSecondCategoryEntityRepository.save(
                new IndustryArticleSecondCategoryEntity(industryArticleEntity, secondCategoryEntity));

        // when
        IndustryArticleSecondCategory industryArticleSecondCategory = IndustryArticleSecondCategory.builder()
                .number(industryArticleSecondCategoryEntity.getNumber())
                .articleNumber(industryArticleEntity.getNumber())
                .secondCategoryNumber(secondCategoryEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toIndustryArticleSecondCategory(
                mapperImpl.toIndustryArticleSecondCategoryEntity(industryArticleSecondCategory,
                        industryArticleEntityRepository, secondCategoryEntityRepository)
        )).usingRecursiveComparison().isEqualTo(industryArticleSecondCategory);
    }

    @DisplayName("엔터티 매퍼 사용 후 IndustryArticleSecondCategoryEntity 일관성 보장")
    @Test
    void industryArticleSecondCategoryEntityMappingWithEntityMapper() {
        // given
        IndustryCategoryEntity secondIndustryCategoryEntity = industryCategoryEntityRepository.save(createSecondIndustryCategoryEntity());
        FirstCategoryEntity firstCategoryEntity = firstCategoryEntityRepository.save(createFirstCategoryEntity());

        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        secondCategoryEntity.updateIndustryCategory(secondIndustryCategoryEntity);
        secondCategoryEntity = secondCategoryEntityRepository.save(secondCategoryEntity);

        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        PressEntity pressEntity = createPressEntity();
        IndustryArticleEntity industryArticleEntity = industryArticleEntityRepository.save(
                IndustryArticleEntity.builder()
                        .industryArticle(createIndustryArticleEntity())
                        .press(pressEntity)
                        .article(articleEntity)
                        .firstCategory(firstCategoryEntity).build());

        // when
        IndustryArticleSecondCategoryEntity industryArticleSecondCategoryEntity = IndustryArticleSecondCategoryEntityRepository.save(
                new IndustryArticleSecondCategoryEntity(industryArticleEntity, secondCategoryEntity));

        // then
        assertThat(mapperImpl.toIndustryArticleSecondCategoryEntity(
                mapperImpl.toIndustryArticleSecondCategory(industryArticleSecondCategoryEntity)
        ,industryArticleEntityRepository, secondCategoryEntityRepository)).isEqualTo(industryArticleSecondCategoryEntity);
    }
}