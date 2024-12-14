package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.IndustryCategoryEntityTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.INDUSTRY_CATEGORY_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class IndustryCategoryRepositoryTest implements IndustryCategoryEntityTestUtils {

    private final IndustryCategoryEntityRepository industryCategoryRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + INDUSTRY_CATEGORY_SNAKE};

    private static final Logger log = LoggerFactory.getLogger(IndustryCategoryRepositoryTest.class);

    @Autowired
    IndustryCategoryRepositoryTest(IndustryCategoryEntityRepository industryCategoryRepository, JdbcTemplate jdbcTemplate) {
        this.industryCategoryRepository = industryCategoryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("번호로 산업 업종 찾기")
    @Test
    void findByNumberTest() {
        // given
        IndustryCategoryEntity industryCategory = createFirstIndustryCategoryEntity();

        // when
        industryCategoryRepository.save(industryCategory);

        // then
        assertThat(industryCategoryRepository.findByNumber(industryCategory.getNumber()).orElseThrow()).isEqualTo(industryCategory);
    }

    @DisplayName("한글명으로 산업 업종 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        IndustryCategoryEntity industryCategory = createFirstIndustryCategoryEntity();

        // when
        industryCategoryRepository.save(industryCategory);

        // then
        assertThat(industryCategoryRepository.findByKoreanName(industryCategory.getKoreanName()).orElseThrow()).isEqualTo(industryCategory);
    }

    @DisplayName("영문명으로 산업 업종 찾기")
    @Test
    void findByEnglishNameTest() {
        // given
        IndustryCategoryEntity industryCategory = createFirstIndustryCategoryEntity();

        // when
        industryCategoryRepository.save(industryCategory);

        // then
        assertThat(industryCategoryRepository.findByEnglishName(industryCategory.getEnglishName()).orElseThrow()).isEqualTo(industryCategory);
    }

    @DisplayName("번호로 산업 업종 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        IndustryCategoryEntity industryCategory = industryCategoryRepository.save(createFirstIndustryCategoryEntity());

        // when
        industryCategoryRepository.deleteByNumber(industryCategory.getNumber());

        // then
        assertThat(industryCategoryRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 산업 업종 확인")
    @Test
    void existsByNumberTest() {
        // given
        IndustryCategoryEntity industryCategory = createFirstIndustryCategoryEntity();

        // when
        industryCategoryRepository.save(industryCategory);

        // then
        assertThat(industryCategoryRepository.existsByNumber(industryCategory.getNumber())).isEqualTo(true);
    }
}