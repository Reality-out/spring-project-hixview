package site.hixview.jpa.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.repository.EconomyContentEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.EconomyContentEntityTestUtils;
import site.hixview.support.spring.util.EconomyContentTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.ECONOMY_CONTENT_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class EconomyContentEntityMapperTest implements EconomyContentTestUtils, EconomyContentEntityTestUtils {

    private static final Logger log = LoggerFactory.getLogger(EconomyContentEntityMapperTest.class);
    private final EconomyContentEntityRepository economyContentEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + ECONOMY_CONTENT_SNAKE};

    private final EconomyContentEntityMapperImpl mapperImpl = new EconomyContentEntityMapperImpl();

    @Autowired
    EconomyContentEntityMapperTest(EconomyContentEntityRepository economyContentEntityRepository, JdbcTemplate jdbcTemplate) {
        this.economyContentEntityRepository = economyContentEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 EconomyContent 일관성 보장")
    @Test
    void economyContentMappingWithEntityMapper() {
        // given
        EconomyContentEntity economyContentEntity = economyContentEntityRepository.save(createEconomyContentEntity());

        // when
        EconomyContent economyContent = EconomyContent.builder().number(economyContentEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toEconomyContent(
                mapperImpl.toEconomyContentEntity(economyContent)))
                .usingRecursiveComparison().isEqualTo(economyContent);
    }

    @DisplayName("엔터티 매퍼 사용 후 EconomyContentEntity 일관성 보장")
    @Test
    void economyContentEntityMappingWithEntityMapper() {
        // given & when
        EconomyContentEntity economyContentEntity = economyContentEntityRepository.save(createEconomyContentEntity());

        // then
        assertThat(mapperImpl.toEconomyContentEntity(
                mapperImpl.toEconomyContent(economyContentEntity))).isEqualTo(economyContentEntity);
    }
}