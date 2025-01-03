package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.EconomyContentEntityTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.ECONOMY_CONTENT_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class EconomyContentEntityRepositoryTest implements EconomyContentEntityTestUtils {

    private final EconomyContentEntityRepository economyContentEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + ECONOMY_CONTENT_SNAKE};

    @Autowired
    EconomyContentEntityRepositoryTest(EconomyContentEntityRepository economyContentEntityRepository, JdbcTemplate jdbcTemplate) {
        this.economyContentEntityRepository = economyContentEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("번호로 경제 컨텐츠 찾기")
    @Test
    void findByNumberTest() {
        // given
        EconomyContentEntity economyContent = createEconomyContentEntity();

        // when
        economyContentEntityRepository.save(economyContent);

        // then
        assertThat(economyContentEntityRepository.findByNumber(economyContent.getNumber()).orElseThrow()).isEqualTo(economyContent);
    }

    @DisplayName("이름으로 경제 컨텐츠 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        EconomyContentEntity economyContent = createEconomyContentEntity();

        // when
        economyContentEntityRepository.save(economyContent);

        // then
        assertThat(economyContentEntityRepository.findByName(economyContent.getName()).orElseThrow()).isEqualTo(economyContent);
    }

    @DisplayName("번호로 경제 컨텐츠 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        EconomyContentEntity economyContent = economyContentEntityRepository.save(createEconomyContentEntity());

        // when
        economyContentEntityRepository.deleteByNumber(economyContent.getNumber());

        // then
        assertThat(economyContentEntityRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 경제 컨텐츠 확인")
    @Test
    void existsByNumberTest() {
        // given
        EconomyContentEntity economyContent = createEconomyContentEntity();

        // when
        economyContentEntityRepository.save(economyContent);

        // then
        assertThat(economyContentEntityRepository.existsByNumber(economyContent.getNumber())).isEqualTo(true);
    }
}