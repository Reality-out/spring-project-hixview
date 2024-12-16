package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.repository.*;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.*;
import site.hixview.support.spring.util.IndustryArticleTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class IndustryArticleEntityMapperTest implements IndustryArticleEntityTestUtils, ArticleEntityTestUtils, IndustryCategoryEntityTestUtils, FirstCategoryEntityTestUtils, SecondCategoryEntityTestUtils, PressEntityTestUtils, IndustryArticleTestUtils {

    private final IndustryArticleSecondCategoryEntityRepository IndustryArticleSecondCategoryEntityRepository;
    private final IndustryArticleEntityRepository industryArticleEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final PressEntityRepository pressEntityRepository;
    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final IndustryArticleEntityMapperImpl mapperImpl = new IndustryArticleEntityMapperImpl();

    @Autowired
    IndustryArticleEntityMapperTest(IndustryArticleSecondCategoryEntityRepository IndustryArticleSecondCategoryEntityRepository, IndustryArticleEntityRepository industryArticleEntityRepository, ArticleEntityRepository articleEntityRepository, IndustryCategoryEntityRepository industryCategoryEntityRepository, FirstCategoryEntityRepository firstCategoryEntityRepository, PressEntityRepository pressEntityRepository, SecondCategoryEntityRepository secondCategoryEntityRepository, JdbcTemplate jdbcTemplate) {
        this.IndustryArticleSecondCategoryEntityRepository = IndustryArticleSecondCategoryEntityRepository;
        this.industryArticleEntityRepository = industryArticleEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.industryCategoryEntityRepository = industryCategoryEntityRepository;
        this.firstCategoryEntityRepository = firstCategoryEntityRepository;
        this.pressEntityRepository = pressEntityRepository;
        this.secondCategoryEntityRepository = secondCategoryEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("엔터티 매퍼 사용 후 IndustryArticle 일관성 보장")
    @Test
    void industryArticleMappingWithEntityMapper() {
        // given
        IndustryCategoryEntity secondIndustryCategoryEntity = industryCategoryEntityRepository.save(createSecondIndustryCategoryEntity());
        FirstCategoryEntity firstCategoryEntity = firstCategoryEntityRepository.save(createFirstCategoryEntity());

        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        secondCategoryEntity.updateIndustryCategory(secondIndustryCategoryEntity);
        secondCategoryEntity = secondCategoryEntityRepository.save(secondCategoryEntity);

        SecondCategoryEntity anotherSecondCategoryEntity = createAnotherSecondCategoryEntity();
        anotherSecondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        anotherSecondCategoryEntity.updateIndustryCategory(secondIndustryCategoryEntity);
        anotherSecondCategoryEntity = secondCategoryEntityRepository.save(anotherSecondCategoryEntity);

        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        PressEntity pressEntity = createPressEntity();
        IndustryArticleEntity industryArticleEntity = industryArticleEntityRepository.save(
                IndustryArticleEntity.builder()
                        .industryArticle(createIndustryArticleEntity())
                        .press(pressEntity)
                        .article(articleEntity)
                        .firstCategory(firstCategoryEntity).build());

        IndustryArticleSecondCategoryEntityRepository.save(
                new IndustryArticleSecondCategoryEntity(industryArticleEntity, secondCategoryEntity));
        IndustryArticleSecondCategoryEntityRepository.save(
                new IndustryArticleSecondCategoryEntity(industryArticleEntity, anotherSecondCategoryEntity));

        // when
        IndustryArticle testIndustryArticle = IndustryArticle.builder()
                .industryArticle(industryArticle)
                .number(articleEntity.getNumber())
                .pressNumber(pressEntity.getNumber())
                .firstCategoryNumber(firstCategoryEntity.getNumber())
                .mappedSecondCategoryNumbers(List.of(secondCategoryEntity.getNumber(), anotherSecondCategoryEntity.getNumber())).build();

        // then
        IndustryArticleEntity mappedIndustryArticleEntity = mapperImpl.toIndustryArticleEntity(
                testIndustryArticle, articleEntityRepository, pressEntityRepository, firstCategoryEntityRepository);
        assertThat(mappedIndustryArticleEntity).isEqualTo(industryArticleEntity);
        assertThat(mapperImpl.toIndustryArticle(industryArticleEntity,
                IndustryArticleSecondCategoryEntityRepository))
                .usingRecursiveComparison().isEqualTo(testIndustryArticle);
    }

    @DisplayName("엔터티 매퍼 사용 후 IndustryArticleEntity 일관성 보장")
    @Test
    void industryArticleEntityMappingWithEntityMapper() {
        // given
        IndustryCategoryEntity secondIndustryCategoryEntity = industryCategoryEntityRepository.save(createSecondIndustryCategoryEntity());
        FirstCategoryEntity firstCategoryEntity = firstCategoryEntityRepository.save(createFirstCategoryEntity());

        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        secondCategoryEntity.updateIndustryCategory(secondIndustryCategoryEntity);
        secondCategoryEntity = secondCategoryEntityRepository.save(secondCategoryEntity);

        SecondCategoryEntity anotherSecondCategoryEntity = createAnotherSecondCategoryEntity();
        anotherSecondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        anotherSecondCategoryEntity.updateIndustryCategory(secondIndustryCategoryEntity);
        anotherSecondCategoryEntity = secondCategoryEntityRepository.save(anotherSecondCategoryEntity);

        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        PressEntity pressEntity = createPressEntity();
        IndustryArticleEntity industryArticleEntity = industryArticleEntityRepository.save(
                IndustryArticleEntity.builder()
                        .industryArticle(createIndustryArticleEntity())
                        .press(pressEntity)
                        .article(articleEntity)
                        .firstCategory(firstCategoryEntity).build());

        IndustryArticleSecondCategoryEntityRepository.save(
                new IndustryArticleSecondCategoryEntity(industryArticleEntity, secondCategoryEntity));
        IndustryArticleSecondCategoryEntityRepository.save(
                new IndustryArticleSecondCategoryEntity(industryArticleEntity, anotherSecondCategoryEntity));

        // when

        // then
        assertThat(mapperImpl.toIndustryArticleEntity(
                mapperImpl.toIndustryArticle(industryArticleEntity, IndustryArticleSecondCategoryEntityRepository),
                articleEntityRepository, pressEntityRepository, firstCategoryEntityRepository)).isEqualTo(industryArticleEntity);
    }
}