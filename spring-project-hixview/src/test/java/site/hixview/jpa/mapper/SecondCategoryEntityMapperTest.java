package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.FirstCategoryEntityTestUtils;
import site.hixview.support.jpa.util.IndustryCategoryEntityTestUtils;
import site.hixview.support.jpa.util.SecondCategoryEntityTestUtils;
import site.hixview.support.spring.util.SecondCategoryTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.*;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class SecondCategoryEntityMapperTest implements SecondCategoryTestUtils, SecondCategoryEntityTestUtils, FirstCategoryEntityTestUtils, IndustryCategoryEntityTestUtils {

    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + SECOND_CATEGORY_SNAKE,
            TEST_TABLE_PREFIX + FIRST_CATEGORY_SNAKE, TEST_TABLE_PREFIX + INDUSTRY_CATEGORY_SNAKE};

    private final SecondCategoryEntityMapperImpl mapperImpl = new SecondCategoryEntityMapperImpl();

    @Autowired
    SecondCategoryEntityMapperTest(SecondCategoryEntityRepository secondCategoryEntityRepository, FirstCategoryEntityRepository firstCategoryEntityRepository, IndustryCategoryEntityRepository industryCategoryEntityRepository, JdbcTemplate jdbcTemplate) {
        this.secondCategoryEntityRepository = secondCategoryEntityRepository;
        this.firstCategoryEntityRepository = firstCategoryEntityRepository;
        this.industryCategoryEntityRepository = industryCategoryEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 SecondCategory 일관성 보장")
    @Test
    void secondCategoryMappingWithEntityMapper() {
        // given
        IndustryCategoryEntity secondIndustryCategoryEntity = industryCategoryEntityRepository.save(createSecondIndustryCategoryEntity());
        FirstCategoryEntity firstCategoryEntity = firstCategoryEntityRepository.save(createFirstCategoryEntity());

        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        secondCategoryEntity.updateIndustryCategory(secondIndustryCategoryEntity);
        secondCategoryEntityRepository.save(secondCategoryEntity);

        // when
        SecondCategory testSecondCategory = SecondCategory.builder()
                .secondCategory(secondCategory)
                .industryCategoryNumber(secondIndustryCategoryEntity.getNumber())
                .firstCategoryNumber(firstCategoryEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toSecondCategory(mapperImpl.toSecondCategoryEntity(testSecondCategory,
                industryCategoryEntityRepository, firstCategoryEntityRepository)))
                .usingRecursiveComparison().isEqualTo(testSecondCategory);
    }

    @DisplayName("엔터티 매퍼 사용 후 SecondCategoryEntity 일관성 보장")
    @Test
    void secondCategoryEntityMappingWithEntityMapper() {
        // given
        IndustryCategoryEntity secondIndustryCategoryEntity = industryCategoryEntityRepository.save(createSecondIndustryCategoryEntity());
        FirstCategoryEntity firstCategoryEntity = firstCategoryEntityRepository.save(createFirstCategoryEntity());

        // when
        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        secondCategoryEntity.updateIndustryCategory(secondIndustryCategoryEntity);
        secondCategoryEntityRepository.save(secondCategoryEntity);

        // then
        assertThat(mapperImpl.toSecondCategoryEntity(mapperImpl.toSecondCategory(secondCategoryEntity),
                industryCategoryEntityRepository, firstCategoryEntityRepository))
                .isEqualTo(secondCategoryEntity);
    }
}