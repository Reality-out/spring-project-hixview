package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.EconomyContentTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.support.jpa.util.ObjectTestUtils.resetAutoIncrement;

@OnlyRealRepositoryContext
class EconomyContentRepositoryTest implements EconomyContentTestUtils {

    @Autowired
    private EconomyContentRepository economyContentRepository;

    private static final Logger log = LoggerFactory.getLogger(EconomyContentRepositoryTest.class);

    @BeforeAll
    public static void beforeAll(@Autowired ApplicationContext applicationContext) {
        resetAutoIncrement(applicationContext);
    }

    @DisplayName("JpaRepository 경제 컨텐츠 연결")
    @Test
    void connectTest() {
        // given
        EconomyContentEntity economyContent = createEconomyContent();

        // when
        economyContentRepository.save(economyContent);

        // then
        assertThat(economyContentRepository.findByNumber(economyContent.getNumber()).orElseThrow()).isEqualTo(economyContent);
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