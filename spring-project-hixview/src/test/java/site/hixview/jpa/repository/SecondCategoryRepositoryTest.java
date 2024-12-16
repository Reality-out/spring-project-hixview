package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.SecondCategoryEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class SecondCategoryRepositoryTest implements SecondCategoryEntityTestUtils {

    private final SecondCategoryEntityRepository secondCategoryRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    SecondCategoryRepositoryTest(SecondCategoryEntityRepository secondCategoryRepository, JdbcTemplate jdbcTemplate) {
        this.secondCategoryRepository = secondCategoryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 2차 업종 찾기")
    @Test
    void findByNumberTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.findByNumber(secondCategory.getNumber()).orElseThrow()).isEqualTo(secondCategory);
    }

    @DisplayName("한글명으로 2차 업종 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.findByKoreanName(secondCategory.getKoreanName()).orElseThrow()).isEqualTo(secondCategory);
    }

    @DisplayName("영문명으로 2차 업종 찾기")
    @Test
    void findByEnglishNameTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.findByEnglishName(secondCategory.getEnglishName()).orElseThrow()).isEqualTo(secondCategory);
    }

    @DisplayName("1차 업종으로 2차 업종 찾기")
    @Test
    void findByFirstCategoryTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.findByFirstCategory(secondCategory.getFirstCategory())).isEqualTo(List.of(secondCategory));
    }

    @DisplayName("번호로 2차 업종 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        SecondCategoryEntity secondCategory = secondCategoryRepository.save(createSecondCategoryEntity());

        // when
        secondCategoryRepository.deleteByNumber(secondCategory.getNumber());

        // then
        assertThat(secondCategoryRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 2차 업종 확인")
    @Test
    void existsByNumberTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategoryEntity();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.existsByNumber(secondCategory.getNumber())).isEqualTo(true);
    }
}