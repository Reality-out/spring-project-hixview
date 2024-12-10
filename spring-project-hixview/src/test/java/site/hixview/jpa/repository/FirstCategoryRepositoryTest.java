package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.executor.SqlExecutor;
import site.hixview.support.jpa.util.FirstCategoryTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class FirstCategoryRepositoryTest implements FirstCategoryTestUtils {

    @Autowired
    private FirstCategoryRepository firstCategoryRepository;

    private static final Logger log = LoggerFactory.getLogger(FirstCategoryRepositoryTest.class);

    @BeforeAll
    public static void beforeAll(@Autowired ApplicationContext applicationContext) {
        new SqlExecutor().resetAutoIncrement(applicationContext);
    }

    @DisplayName("번호로 1차 업종 찾기")
    @Test
    void findByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategory();

        // when
        firstCategoryRepository.save(firstCategory);

        // then
        assertThat(firstCategoryRepository.findByNumber(firstCategory.getNumber()).orElseThrow()).isEqualTo(firstCategory);
    }

    @DisplayName("한글명으로 1차 업종 찾기")
    @Test
    void findByKoreanNameTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategory();

        // when
        firstCategoryRepository.save(firstCategory);

        // then
        assertThat(firstCategoryRepository.findByKoreanName(firstCategory.getKoreanName()).orElseThrow()).isEqualTo(firstCategory);
    }

    @DisplayName("영문명으로 1차 업종 찾기")
    @Test
    void findByEnglishNameTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategory();

        // when
        firstCategoryRepository.save(firstCategory);

        // then
        assertThat(firstCategoryRepository.findByEnglishName(firstCategory.getEnglishName()).orElseThrow()).isEqualTo(firstCategory);
    }

    @DisplayName("번호로 1차 업종 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = firstCategoryRepository.save(createFirstCategory());

        // when
        firstCategoryRepository.deleteByNumber(firstCategory.getNumber());

        // then
        assertThat(firstCategoryRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 1차 업종 확인")
    @Test
    void existsByNumberTest() {
        // given
        FirstCategoryEntity firstCategory = createFirstCategory();

        // when
        firstCategoryRepository.save(firstCategory);

        // then
        assertThat(firstCategoryRepository.existsByNumber(firstCategory.getNumber())).isEqualTo(true);
    }
}