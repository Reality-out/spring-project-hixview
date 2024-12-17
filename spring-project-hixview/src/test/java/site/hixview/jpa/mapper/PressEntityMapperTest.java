package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.Press;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.repository.PressEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.PressEntityTestUtils;
import site.hixview.support.spring.util.PressTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.PRESS;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class PressEntityMapperTest implements PressTestUtils, PressEntityTestUtils {

    private final PressEntityRepository pressEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final PressEntityMapper mapperImpl = new PressEntityMapperImpl();

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + PRESS};

    @Autowired
    PressEntityMapperTest(PressEntityRepository pressEntityRepository, JdbcTemplate jdbcTemplate) {
        this.pressEntityRepository = pressEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 Press 일관성 보장")
    @Test
    void pressMappingWithEntityMapper() {
        // given
        PressEntity pressEntity = pressEntityRepository.save(createPressEntity());

        // when
        Press press = Press.builder().number(pressEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toPress(
                mapperImpl.toPressEntity(press))).isEqualTo(press);
    }

    @DisplayName("엔터티 매퍼 사용 후 PressEntity 일관성 보장")
    @Test
    void pressEntityMappingWithEntityMapper() {
        // given & when
        PressEntity pressEntity = pressEntityRepository.save(createPressEntity());

        // then
        assertThat(mapperImpl.toPressEntity(
                mapperImpl.toPress(pressEntity))).isEqualTo(pressEntity);
    }
}