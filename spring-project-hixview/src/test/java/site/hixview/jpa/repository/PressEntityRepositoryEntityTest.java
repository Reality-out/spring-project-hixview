package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.PressEntityTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.PRESS;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class PressEntityRepositoryTest implements PressEntityTestUtils {

    @Autowired
    private PressEntityRepository pressEntityRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + PRESS};

    private static final Logger log = LoggerFactory.getLogger(PressEntityRepositoryTest.class);

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("번호로 언론사 찾기")
    @Test
    void findByNumberTest() {
        // given
        PressEntity press = createPress();

        // when
        pressEntityRepository.save(press);

        // then
        assertThat(pressEntityRepository.findByNumber(press.getNumber()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("한글명으로 언론사 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        PressEntity press = createPress();

        // when
        pressEntityRepository.save(press);

        // then
        assertThat(pressEntityRepository.findByKoreanName(press.getKoreanName()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("영문명으로 언론사 찾기")
    @Test
    void findByEnglishNameTest() {
        // given
        PressEntity press = createPress();

        // when
        pressEntityRepository.save(press);

        // then
        assertThat(pressEntityRepository.findByEnglishName(press.getEnglishName()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("번호로 언론사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        PressEntity press = pressEntityRepository.save(createPress());

        // when
        pressEntityRepository.deleteByNumber(press.getNumber());

        // then
        assertThat(pressEntityRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 언론사 확인")
    @Test
    void existsByNumberTest() {
        // given
        PressEntity press = createPress();

        // when
        pressEntityRepository.save(press);

        // then
        assertThat(pressEntityRepository.existsByNumber(press.getNumber())).isEqualTo(true);
    }
}