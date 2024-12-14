package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.FirstCategoryEntityTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.FIRST_CATEGORY_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.INDUSTRY_CATEGORY_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class FirstCategoryRepositoryTest implements FirstCategoryEntityTestUtils {

    private final FirstCategoryEntityRepository firstCategoryRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + FIRST_CATEGORY_SNAKE,
            TEST_TABLE_PREFIX + INDUSTRY_CATEGORY_SNAKE};

    private static final Logger log = LoggerFactory.getLogger(FirstCategoryRepositoryTest.class);

    @Autowired
    FirstCategoryRepositoryTest(FirstCategoryEntityRepository firstCategoryRepository, JdbcTemplate jdbcTemplate) {
        this.firstCategoryRepository = firstCategoryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("번호로 1차 업종 찾기")
    @Test
    void findByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategoryEntity();

        // when
        firstCategoryRepository.save(firstCategory);

        // then
        assertThat(firstCategoryRepository.findByNumber(firstCategory.getNumber()).orElseThrow()).isEqualTo(firstCategory);
    }

    @DisplayName("한글명으로 1차 업종 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategoryEntity();

        // when
        firstCategoryRepository.save(firstCategory);

        // then
        assertThat(firstCategoryRepository.findByKoreanName(firstCategory.getKoreanName()).orElseThrow()).isEqualTo(firstCategory);
    }

    @DisplayName("영문명으로 1차 업종 찾기")
    @Test
    void findByEnglishNameTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategoryEntity();

        // when
        firstCategoryRepository.save(firstCategory);

        // then
        assertThat(firstCategoryRepository.findByEnglishName(firstCategory.getEnglishName()).orElseThrow()).isEqualTo(firstCategory);
    }

    @DisplayName("번호로 1차 업종 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = firstCategoryRepository.save(createFirstCategoryEntity());

        // when
        firstCategoryRepository.deleteByNumber(firstCategory.getNumber());

        // then
        assertThat(firstCategoryRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 1차 업종 확인")
    @Test
    void existsByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategoryEntity();

        // when
        firstCategoryRepository.save(firstCategory);

        // then
        assertThat(firstCategoryRepository.existsByNumber(firstCategory.getNumber())).isEqualTo(true);
    }
}