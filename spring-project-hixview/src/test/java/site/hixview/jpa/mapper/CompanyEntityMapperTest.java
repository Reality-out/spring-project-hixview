package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.Company;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.CompanyEntityRepository;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.CompanyEntityTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.COMPANY;
import static site.hixview.aggregate.vo.WordSnake.FIRST_CATEGORY_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.SECOND_CATEGORY_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class CompanyEntityMapperTest implements CompanyEntityTestUtils {

    private final CompanyEntityRepository companyEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + COMPANY,
            TEST_TABLE_PREFIX + FIRST_CATEGORY_SNAKE, TEST_TABLE_PREFIX + SECOND_CATEGORY_SNAKE};

    private final CompanyEntityMapperImpl mapperImpl = new CompanyEntityMapperImpl();

    @Autowired
    public CompanyEntityMapperTest(CompanyEntityRepository companyEntityRepository, FirstCategoryEntityRepository firstCategoryEntityRepository, SecondCategoryEntityRepository secondCategoryEntityRepository, JdbcTemplate jdbcTemplate) {
        this.companyEntityRepository = companyEntityRepository;
        this.firstCategoryEntityRepository = firstCategoryEntityRepository;
        this.secondCategoryEntityRepository = secondCategoryEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 Company 일관성 보장")
    @Test
    void companyMappingWithEntityMapper() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategory = companyEntity.getFirstCategory();
        SecondCategoryEntity secondCategory = companyEntity.getSecondCategory();
        firstCategoryEntityRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryEntityRepository.save(secondCategory);
        Company company = Company.builder()
                .firstCategoryNumber(firstCategory.getNumber())
                .secondCategoryNumber(secondCategory.getNumber()).build();

        // when
        companyEntityRepository.save(companyEntity);

        // then
        assertThat(mapperImpl.toCompany(mapperImpl.toCompanyEntity(
                company, firstCategoryEntityRepository, secondCategoryEntityRepository)))
                .usingRecursiveComparison().isEqualTo(company);
    }

    @DisplayName("엔터티 매퍼 사용 후 CompanyEntity 일관성 보장")
    @Test
    void companyEntityMappingWithEntityMapper() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategory = companyEntity.getFirstCategory();
        SecondCategoryEntity secondCategory = companyEntity.getSecondCategory();
        firstCategoryEntityRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryEntityRepository.save(secondCategory);

        // when
        companyEntity = companyEntityRepository.save(companyEntity);

        // then
        assertThat(mapperImpl.toCompanyEntity(mapperImpl.toCompany(
                companyEntity), firstCategoryEntityRepository, secondCategoryEntityRepository))
                .isEqualTo(companyEntity);
    }
}