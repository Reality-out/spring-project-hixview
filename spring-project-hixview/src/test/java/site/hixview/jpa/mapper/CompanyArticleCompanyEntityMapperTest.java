package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.repository.*;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.CompanyArticleEntityTestUtils;
import site.hixview.support.jpa.util.CompanyEntityTestUtils;
import site.hixview.support.spring.util.CompanyArticleCompanyTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class CompanyArticleCompanyEntityMapperTest implements CompanyArticleCompanyTestUtils, CompanyArticleEntityTestUtils, CompanyEntityTestUtils {

    private final CompanyArticleCompanyEntityRepository companyArticleCompanyEntityRepository;
    private final CompanyArticleEntityRepository companyArticleEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final CompanyArticleCompanyEntityMapper mapperImpl = new CompanyArticleCompanyEntityMapperImpl();

    @Autowired
    CompanyArticleCompanyEntityMapperTest(CompanyArticleCompanyEntityRepository companyArticleCompanyEntityRepository, CompanyArticleEntityRepository companyArticleEntityRepository, ArticleEntityRepository articleEntityRepository, CompanyEntityRepository companyEntityRepository, FirstCategoryEntityRepository firstCategoryEntityRepository, SecondCategoryEntityRepository secondCategoryEntityRepository, JdbcTemplate jdbcTemplate) {
        this.companyArticleCompanyEntityRepository = companyArticleCompanyEntityRepository;
        this.companyArticleEntityRepository = companyArticleEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.companyEntityRepository = companyEntityRepository;
        this.firstCategoryEntityRepository = firstCategoryEntityRepository;
        this.secondCategoryEntityRepository = secondCategoryEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteAll(jdbcTemplate);
    }

    @DisplayName("엔터티 매퍼 사용 후 CompanyArticleCompany 일관성 보장")
    @Test
    void companyArticleCompanyMappingWithEntityMapper() {
        // given
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        CompanyArticleEntity companyArticleEntity = companyArticleEntityRepository.save(
                CompanyArticleEntity.builder().companyArticle(createCompanyArticleEntity()).article(articleEntity).build());
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategory = companyEntity.getFirstCategory();
        SecondCategoryEntity secondCategory = companyEntity.getSecondCategory();
        firstCategoryEntityRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryEntityRepository.save(secondCategory);
        companyEntity = companyEntityRepository.save(companyEntity);
        CompanyArticleCompanyEntity companyArticleCompanyEntity = companyArticleCompanyEntityRepository.save(
                new CompanyArticleCompanyEntity(companyArticleEntity, companyEntity));

        // when
        CompanyArticleCompany companyArticleCompany = CompanyArticleCompany.builder()
                .number(companyArticleCompanyEntity.getNumber())
                .articleNumber(articleEntity.getNumber())
                .companyCode(companyEntity.getCode()).build();

        // then
        assertThat(mapperImpl.toCompanyArticleCompany(mapperImpl.toCompanyArticleCompanyEntity(
                companyArticleCompany, companyArticleEntityRepository, companyEntityRepository)))
                .usingRecursiveComparison().isEqualTo(companyArticleCompany);
    }

    @DisplayName("엔터티 매퍼 사용 후 CompanyArticleCompanyEntity 일관성 보장")
    @Test
    void companyArticleCompanyEntityMappingWithEntityMapper() {
        // given
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        CompanyArticleEntity companyArticleEntity = companyArticleEntityRepository.save(
                CompanyArticleEntity.builder().companyArticle(createCompanyArticleEntity()).article(articleEntity).build());
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategory = companyEntity.getFirstCategory();
        SecondCategoryEntity secondCategory = companyEntity.getSecondCategory();
        firstCategoryEntityRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryEntityRepository.save(secondCategory);
        companyEntity = companyEntityRepository.save(companyEntity);

        // when
        CompanyArticleCompanyEntity companyArticleCompanyEntity = companyArticleCompanyEntityRepository.save(
                new CompanyArticleCompanyEntity(companyArticleEntity, companyEntity));

        // then
        assertThat(mapperImpl.toCompanyArticleCompanyEntity(mapperImpl.toCompanyArticleCompany(
                companyArticleCompanyEntity), companyArticleEntityRepository, companyEntityRepository))
                .isEqualTo(companyArticleCompanyEntity);
    }
}