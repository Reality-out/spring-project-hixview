package site.hixview.jpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.PressTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class PressRepositoryTest implements PressTestUtils {

    @Autowired
    private PressRepository pressRepository;

    private static final Logger log = LoggerFactory.getLogger(PressRepositoryTest.class);

    @DisplayName("JpaRepository 언론사 연결")
    @Test
    void connectTest() {
        // given
        PressEntity press = createPress();

        // when
        pressRepository.save(press);

        // then
        assertThat(pressRepository.findByNumber(press.getNumber()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("번호로 언론사 찾기")
    @Test
    void findByNumberTest() {
        // given
        PressEntity press = createPress();

        // when
        pressRepository.save(press);

        // then
        assertThat(pressRepository.findByNumber(press.getNumber()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("한글명으로 언론사 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        PressEntity press = createPress();

        // when
        pressRepository.save(press);

        // then
        assertThat(pressRepository.findByKoreanName(press.getKoreanName()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("영문명으로 언론사 찾기")
    @Test
    void findByEnglishNameTest() {
        // given
        PressEntity press = createPress();

        // when
        pressRepository.save(press);

        // then
        assertThat(pressRepository.findByEnglishName(press.getEnglishName()).orElseThrow()).isEqualTo(press);
    }

    @DisplayName("번호로 언론사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        PressEntity press = pressRepository.save(createPress());

        // when
        pressRepository.deleteByNumber(press.getNumber());

        // then
        assertThat(pressRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 언론사 확인")
    @Test
    void existsByNumberTest() {
        // given
        PressEntity press = createPress();

        // when
        pressRepository.save(press);

        // then
        assertThat(pressRepository.existsByNumber(press.getNumber())).isEqualTo(true);
    }
}