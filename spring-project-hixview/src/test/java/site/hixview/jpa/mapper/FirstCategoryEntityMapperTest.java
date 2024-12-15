package site.hixview.jpa.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.FirstCategoryEntityTestUtils;
import site.hixview.support.jpa.util.IndustryCategoryEntityTestUtils;
import site.hixview.support.spring.util.FirstCategoryTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.FIRST_CATEGORY_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.INDUSTRY_CATEGORY_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class FirstCategoryEntityMapperTest implements FirstCategoryTestUtils, FirstCategoryEntityTestUtils, IndustryCategoryEntityTestUtils {

    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + FIRST_CATEGORY_SNAKE,
            TEST_TABLE_PREFIX + INDUSTRY_CATEGORY_SNAKE};

    private final FirstCategoryEntityMapperImpl mapperImpl = new FirstCategoryEntityMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(FirstCategoryEntityMapperTest.class);

    @Autowired
    FirstCategoryEntityMapperTest(IndustryCategoryEntityRepository industryCategoryEntityRepository, JdbcTemplate jdbcTemplate) {
        this.industryCategoryEntityRepository = industryCategoryEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 FirstCategory 일관성 보장")
    @Test
    void firstCategoryMappingWithEntityMapper() {
        // given
        IndustryCategoryEntity firstIndustryCategoryEntity = industryCategoryEntityRepository.save(createFirstIndustryCategoryEntity());

        // when
        FirstCategory testFirstCategory = FirstCategory.builder().firstCategory(firstCategory)
                .industryCategoryNumber(firstIndustryCategoryEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toFirstCategory(mapperImpl.toFirstCategoryEntity(testFirstCategory,
                industryCategoryEntityRepository)))
                .usingRecursiveComparison().isEqualTo(testFirstCategory);
    }

    @DisplayName("엔터티 매퍼 사용 후 FirstCategoryEntity 일관성 보장")
    @Test
    void firstCategoryEntityMappingWithEntityMapper() {
        // given & when
        IndustryCategoryEntity firstIndustryCategoryEntity = industryCategoryEntityRepository.save(createFirstIndustryCategoryEntity());
        FirstCategoryEntity firstCategoryEntity = createFirstCategoryEntity();
        firstCategoryEntity.updateIndustryCategory(firstIndustryCategoryEntity);

        // then
        assertThat(mapperImpl.toFirstCategoryEntity(mapperImpl.toFirstCategory(firstCategoryEntity)
                , industryCategoryEntityRepository))
                .isEqualTo(firstCategoryEntity);
    }
}