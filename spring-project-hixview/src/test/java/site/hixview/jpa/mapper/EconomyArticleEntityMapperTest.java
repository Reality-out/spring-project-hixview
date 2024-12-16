package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.repository.*;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.EconomyArticleEntityTestUtils;
import site.hixview.support.jpa.util.EconomyContentEntityTestUtils;
import site.hixview.support.spring.util.EconomyArticleTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.aggregate.vo.WordCamel.PRESS;
import static site.hixview.aggregate.vo.WordSnake.*;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class EconomyArticleEntityMapperTest implements EconomyArticleEntityTestUtils, EconomyContentEntityTestUtils, EconomyArticleTestUtils {

    private final EconomyArticleContentEntityRepository economyArticleContentEntityRepository;
    private final EconomyArticleEntityRepository economyArticleEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final PressEntityRepository pressEntityRepository;
    private final EconomyContentEntityRepository economyContentEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + ECON_ARTI_CONT_MAPPER_SNAKE,
            TEST_TABLE_PREFIX + ECONOMY_ARTICLE_SNAKE, TEST_TABLE_PREFIX + ARTICLE,
            TEST_TABLE_PREFIX + ECONOMY_CONTENT_SNAKE, TEST_TABLE_PREFIX + PRESS};

    private final EconomyArticleEntityMapperImpl mapperImpl = new EconomyArticleEntityMapperImpl();

    @Autowired
    EconomyArticleEntityMapperTest(EconomyArticleContentEntityRepository economyArticleContentEntityRepository, EconomyArticleEntityRepository economyArticleEntityRepository, ArticleEntityRepository articleEntityRepository, PressEntityRepository pressEntityRepository, EconomyContentEntityRepository economyContentEntityRepository, JdbcTemplate jdbcTemplate) {
        this.economyArticleContentEntityRepository = economyArticleContentEntityRepository;
        this.economyArticleEntityRepository = economyArticleEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.pressEntityRepository = pressEntityRepository;
        this.economyContentEntityRepository = economyContentEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 EconomyArticle 일관성 보장")
    @Test
    void economyArticleMappingWithEntityMapper() {
        // given
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        PressEntity pressEntity = createPressEntity();
        EconomyArticleEntity economyArticleEntity = economyArticleEntityRepository.save(
                EconomyArticleEntity.builder().economyArticle(createEconomyArticleEntity()).article(articleEntity).press(pressEntity).build());
        EconomyContentEntity economyContentEntity = economyContentEntityRepository.save(createEconomyContentEntity());
        EconomyContentEntity anotherEconomyContentEntity = economyContentEntityRepository.save(createAnotherEconomyContentEntity());
        economyArticleContentEntityRepository.save(
                new EconomyArticleContentEntity(economyArticleEntity, economyContentEntity));
        economyArticleContentEntityRepository.save(
                new EconomyArticleContentEntity(economyArticleEntity, anotherEconomyContentEntity));

        // when
        EconomyArticle testEconomyArticle = EconomyArticle.builder()
                .economyArticle(economyArticle)
                .number(articleEntity.getNumber())
                .pressNumber(pressEntity.getNumber())
                .mappedEconomyContentNumbers(List.of(economyContentEntity.getNumber(), anotherEconomyContentEntity.getNumber())).build();

        // then
        EconomyArticleEntity mappedEconomyArticleEntity = mapperImpl.toEconomyArticleEntity(
                testEconomyArticle, articleEntityRepository, pressEntityRepository);
        assertThat(mappedEconomyArticleEntity).isEqualTo(economyArticleEntity);
        assertThat(mapperImpl.toEconomyArticle(economyArticleEntity,
                economyArticleContentEntityRepository))
                .usingRecursiveComparison().isEqualTo(testEconomyArticle);
    }

    @DisplayName("엔터티 매퍼 사용 후 EconomyArticleEntity 일관성 보장")
    @Test
    void economyArticleEntityMappingWithEntityMapper() {
        // given & when
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        PressEntity pressEntity = createPressEntity();
        EconomyArticleEntity economyArticleEntity = economyArticleEntityRepository.save(
                EconomyArticleEntity.builder().economyArticle(createEconomyArticleEntity()).article(articleEntity).press(pressEntity).build());
        EconomyContentEntity economyContentEntity = economyContentEntityRepository.save(createEconomyContentEntity());
        EconomyContentEntity anotherEconomyContentEntity = economyContentEntityRepository.save(createAnotherEconomyContentEntity());
        economyArticleContentEntityRepository.save(
                new EconomyArticleContentEntity(economyArticleEntity, economyContentEntity));
        economyArticleContentEntityRepository.save(
                new EconomyArticleContentEntity(economyArticleEntity, anotherEconomyContentEntity));

        // then
        assertThat(mapperImpl.toEconomyArticleEntity(
                mapperImpl.toEconomyArticle(economyArticleEntity, economyArticleContentEntityRepository),
                articleEntityRepository, pressEntityRepository)).isEqualTo(economyArticleEntity);
    }
}