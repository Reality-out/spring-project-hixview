package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.SecondCategoryEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.*;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class SecondCategoryRepositoryTest implements SecondCategoryEntityTestUtils {

    @Autowired
    private SecondCategoryEntityRepository secondCategoryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + SECOND_CATEGORY_SNAKE,
            TEST_TABLE_PREFIX + FIRST_CATEGORY_SNAKE, TEST_TABLE_PREFIX + INDUSTRY_CATEGORY_SNAKE};

    private static final Logger log = LoggerFactory.getLogger(SecondCategoryRepositoryTest.class);

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 2차 업종 찾기")
    @Test
    void findByNumberTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategory();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.findByNumber(secondCategory.getNumber()).orElseThrow()).isEqualTo(secondCategory);
    }

    @DisplayName("한글명으로 2차 업종 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategory();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.findByKoreanName(secondCategory.getKoreanName()).orElseThrow()).isEqualTo(secondCategory);
    }

    @DisplayName("영문명으로 2차 업종 찾기")
    @Test
    void findByEnglishNameTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategory();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.findByEnglishName(secondCategory.getEnglishName()).orElseThrow()).isEqualTo(secondCategory);
    }

    @DisplayName("1차 업종으로 2차 업종 찾기")
    @Test
    void findByFirstCategoryTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategory();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.findByFirstCategory(secondCategory.getFirstCategory())).isEqualTo(List.of(secondCategory));
    }

    @DisplayName("번호로 2차 업종 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        SecondCategoryEntity secondCategory = secondCategoryRepository.save(createSecondCategory());

        // when
        secondCategoryRepository.deleteByNumber(secondCategory.getNumber());

        // then
        assertThat(secondCategoryRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 2차 업종 확인")
    @Test
    void existsByNumberTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategory();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.existsByNumber(secondCategory.getNumber())).isEqualTo(true);
    }
}