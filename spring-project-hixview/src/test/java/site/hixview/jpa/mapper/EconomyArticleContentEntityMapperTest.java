package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.EconomyArticleContentEntityRepository;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.EconomyContentEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.EconomyArticleEntityTestUtils;
import site.hixview.support.jpa.util.EconomyContentEntityTestUtils;
import site.hixview.support.spring.util.EconomyArticleContentTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class EconomyArticleContentEntityMapperTest implements EconomyArticleContentTestUtils, EconomyArticleEntityTestUtils, EconomyContentEntityTestUtils {

    private final EconomyArticleContentEntityRepository economyArticleContentEntityRepository;
    private final EconomyArticleEntityRepository economyArticleEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final EconomyContentEntityRepository economyContentEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final EconomyArticleContentEntityMapperImpl mapperImpl = new EconomyArticleContentEntityMapperImpl();

    @Autowired
    public EconomyArticleContentEntityMapperTest(EconomyArticleContentEntityRepository economyArticleContentEntityRepository, EconomyArticleEntityRepository economyArticleEntityRepository, ArticleEntityRepository articleEntityRepository, EconomyContentEntityRepository economyContentEntityRepository, JdbcTemplate jdbcTemplate) {
        this.economyArticleContentEntityRepository = economyArticleContentEntityRepository;
        this.economyArticleEntityRepository = economyArticleEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.economyContentEntityRepository = economyContentEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("엔터티 매퍼 사용 후 EconomyArticleContent 일관성 보장")
    @Test
    void economyArticleContentMappingWithEntityMapper() {
        // given
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        EconomyArticleEntity economyArticleEntity = economyArticleEntityRepository.save(
                EconomyArticleEntity.builder().economyArticle(createEconomyArticleEntity()).article(articleEntity).build());
        EconomyContentEntity economyContentEntity = economyContentEntityRepository.save(createEconomyContentEntity());
        EconomyArticleContentEntity economyArticleContentEntity = economyArticleContentEntityRepository.save(
                new EconomyArticleContentEntity(economyArticleEntity, economyContentEntity));

        // when
        EconomyArticleContent economyArticleContent = EconomyArticleContent.builder()
                .number(economyArticleContentEntity.getNumber())
                .articleNumber(articleEntity.getNumber())
                .contentNumber(economyContentEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toEconomyArticleContent(mapperImpl.toEconomyArticleContentEntity(
                economyArticleContent, economyArticleEntityRepository, economyContentEntityRepository)))
                .usingRecursiveComparison().isEqualTo(economyArticleContent);
    }

    @DisplayName("엔터티 매퍼 사용 후 EconomyArticleContentEntity 일관성 보장")
    @Test
    void economyArticleContentEntityMappingWithEntityMapper() {
        // given & when
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        EconomyArticleEntity economyArticleEntity = economyArticleEntityRepository.save(
                EconomyArticleEntity.builder().economyArticle(createEconomyArticleEntity()).article(articleEntity).build());
        EconomyContentEntity economyContentEntity = economyContentEntityRepository.save(createEconomyContentEntity());
        EconomyArticleContentEntity economyArticleContentEntity = economyArticleContentEntityRepository.save(
                new EconomyArticleContentEntity(economyArticleEntity, economyContentEntity));

        // then
        assertThat(mapperImpl.toEconomyArticleContentEntity(mapperImpl.toEconomyArticleContent(
                economyArticleContentEntity), economyArticleEntityRepository, economyContentEntityRepository))
                .isEqualTo(economyArticleContentEntity);
    }
}