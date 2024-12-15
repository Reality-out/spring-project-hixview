package site.hixview.jpa.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.jpa.entity.IndustryCategoryEntity;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.IndustryCategoryEntityTestUtils;
import site.hixview.support.spring.util.IndustryCategoryTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.INDUSTRY_CATEGORY_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class IndustryCategoryEntityMapperTest implements IndustryCategoryTestUtils, IndustryCategoryEntityTestUtils {

    private final IndustryCategoryEntityRepository industryCategoryEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final IndustryCategoryEntityMapperImpl mapperImpl = new IndustryCategoryEntityMapperImpl();

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + INDUSTRY_CATEGORY_SNAKE};

    @Autowired
    IndustryCategoryEntityMapperTest(IndustryCategoryEntityRepository industryCategoryEntityRepository, JdbcTemplate jdbcTemplate) {
        this.industryCategoryEntityRepository = industryCategoryEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 IndustryCategory 일관성 보장")
    @Test
    void industryCategoryMappingWithEntityMapper() {
        // given
        IndustryCategoryEntity industryCategoryEntity = industryCategoryEntityRepository.save(createFirstIndustryCategoryEntity());

        // when
        IndustryCategory testIndustryCategory = IndustryCategory.builder()
                .industryCategory(industryCategory)
                .number(industryCategoryEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toIndustryCategory(
                mapperImpl.toIndustryCategoryEntity(testIndustryCategory)))
                .usingRecursiveComparison().isEqualTo(testIndustryCategory);
    }

    @DisplayName("엔터티 매퍼 사용 후 IndustryCategoryEntity 일관성 보장")
    @Test
    void industryCategoryEntityMappingWithEntityMapper() {
        // given & when
        IndustryCategoryEntity industryCategoryEntity = industryCategoryEntityRepository.save(createFirstIndustryCategoryEntity());

        // then
        assertThat(mapperImpl.toIndustryCategoryEntity(
                mapperImpl.toIndustryCategory(industryCategoryEntity))).isEqualTo(industryCategoryEntity);
    }
}