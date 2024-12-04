package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.SecondCategoryTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.support.jpa.util.ObjectTestUtils.resetAutoIncrement;

@OnlyRealRepositoryContext
class SecondCategoryRepositoryTest implements SecondCategoryTestUtils {

    @Autowired
    private SecondCategoryRepository secondCategoryRepository;

    private static final Logger log = LoggerFactory.getLogger(SecondCategoryRepositoryTest.class);

    @BeforeAll
    public static void beforeAll(@Autowired ApplicationContext applicationContext) {
        resetAutoIncrement(applicationContext);
    }

    @DisplayName("JpaRepository 2차 업종 연결")
    @Test
    void connectTest() {
        // given
        SecondCategoryEntity secondCategory = createSecondCategory();

        // when
        secondCategoryRepository.save(secondCategory);

        // then
        assertThat(secondCategoryRepository.findByNumber(secondCategory.getNumber()).orElseThrow()).isEqualTo(secondCategory);
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