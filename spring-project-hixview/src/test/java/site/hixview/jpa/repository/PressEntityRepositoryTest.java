package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
@Slf4j
class PressEntityRepositoryTest implements PressEntityTestUtils {

    private final PressEntityRepository pressEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + PRESS};

    @Autowired
    PressEntityRepositoryTest(PressEntityRepository pressEntityRepository, JdbcTemplate jdbcTemplate) {
        this.pressEntityRepository = pressEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("번호로 언론사 찾기")
    @Test
    void findByNumberTest() {
        // given
        PressEntity press = createPressEntity();

        // when
        pressEntityRepository.save(press);

        // then
        assertThat(pressEntityRepository.findByNumber(press.getNumber()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("한글명으로 언론사 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        PressEntity press = createPressEntity();

        // when
        pressEntityRepository.save(press);

        // then
        assertThat(pressEntityRepository.findByKoreanName(press.getKoreanName()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("영문명으로 언론사 찾기")
    @Test
    void findByEnglishNameTest() {
        // given
        PressEntity press = createPressEntity();

        // when
        pressEntityRepository.save(press);

        // then
        assertThat(pressEntityRepository.findByEnglishName(press.getEnglishName()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("번호로 언론사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        PressEntity press = pressEntityRepository.save(createPressEntity());

        // when
        pressEntityRepository.deleteByNumber(press.getNumber());

        // then
        assertThat(pressEntityRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 언론사 확인")
    @Test
    void existsByNumberTest() {
        // given
        PressEntity press = createPressEntity();

        // when
        pressEntityRepository.save(press);

        // then
        assertThat(pressEntityRepository.existsByNumber(press.getNumber())).isEqualTo(true);
    }
}