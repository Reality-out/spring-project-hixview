package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.EconomyContentTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.ECONOMY_CONTENT_SNAKE;
import static site.hixview.support.jpa.util.ObjectTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class EconomyContentRepositoryTest implements EconomyContentTestUtils {

    @Autowired
    private EconomyContentRepository economyContentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + ECONOMY_CONTENT_SNAKE};

    private static final Logger log = LoggerFactory.getLogger(EconomyContentRepositoryTest.class);

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("번호로 경제 컨텐츠 찾기")
    @Test
    void findByNumberTest() {
        // given
        EconomyContentEntity economyContent = createEconomyContent();

        // when
        economyContentRepository.save(economyContent);

        // then
        assertThat(economyContentRepository.findByNumber(economyContent.getNumber()).orElseThrow()).isEqualTo(economyContent);
    }

    @DisplayName("이름으로 경제 컨텐츠 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        EconomyContentEntity economyContent = createEconomyContent();

        // when
        economyContentRepository.save(economyContent);

        // then
        assertThat(economyContentRepository.findByName(economyContent.getName()).orElseThrow()).isEqualTo(economyContent);
    }

    @DisplayName("번호로 경제 컨텐츠 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        EconomyContentEntity economyContent = economyContentRepository.save(createEconomyContent());

        // when
        economyContentRepository.deleteByNumber(economyContent.getNumber());

        // then
        assertThat(economyContentRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 경제 컨텐츠 확인")
    @Test
    void existsByNumberTest() {
        // given
        EconomyContentEntity economyContent = createEconomyContent();

        // when
        economyContentRepository.save(economyContent);

        // then
        assertThat(economyContentRepository.existsByNumber(economyContent.getNumber())).isEqualTo(true);
    }
}