package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.repository.*;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.CompanyArticleEntityTestUtils;
import site.hixview.support.jpa.util.CompanyEntityTestUtils;
import site.hixview.support.spring.util.CompanyArticleTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class CompanyArticleEntityMapperTest implements CompanyArticleEntityTestUtils, CompanyEntityTestUtils, CompanyArticleTestUtils {

    private final CompanyArticleCompanyEntityRepository companyArticleCompanyEntityRepository;
    private final CompanyArticleEntityRepository companyArticleEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final PressEntityRepository pressEntityRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final FirstCategoryEntityRepository firstCategoryEntityRepository;
    private final SecondCategoryEntityRepository secondCategoryEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final CompanyArticleEntityMapper mapperImpl = new CompanyArticleEntityMapperImpl();

    @Autowired
    CompanyArticleEntityMapperTest(CompanyArticleCompanyEntityRepository companyArticleCompanyEntityRepository, CompanyArticleEntityRepository companyArticleEntityRepository, ArticleEntityRepository articleEntityRepository, PressEntityRepository pressEntityRepository, CompanyEntityRepository companyEntityRepository, FirstCategoryEntityRepository firstCategoryEntityRepository, SecondCategoryEntityRepository secondCategoryEntityRepository, JdbcTemplate jdbcTemplate) {
        this.companyArticleCompanyEntityRepository = companyArticleCompanyEntityRepository;
        this.companyArticleEntityRepository = companyArticleEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.pressEntityRepository = pressEntityRepository;
        this.companyEntityRepository = companyEntityRepository;
        this.firstCategoryEntityRepository = firstCategoryEntityRepository;
        this.secondCategoryEntityRepository = secondCategoryEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteAll(jdbcTemplate);
    }

    @DisplayName("엔터티 매퍼 사용 후 CompanyArticle 일관성 보장")
    @Test
    void companyArticleMappingWithEntityMapper() {
        // given
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        PressEntity pressEntity = createPressEntity();
        CompanyArticleEntity companyArticleEntity = companyArticleEntityRepository.save(
                CompanyArticleEntity.builder().companyArticle(createCompanyArticleEntity()).article(articleEntity).press(pressEntity).build());
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategory = companyEntity.getFirstCategory();
        SecondCategoryEntity secondCategory = companyEntity.getSecondCategory();
        firstCategoryEntityRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryEntityRepository.save(secondCategory);
        companyEntity = companyEntityRepository.save(companyEntity);
        CompanyEntity anotherCompanyEntity = companyEntityRepository.save(CompanyEntity.builder()
                .company(createAnotherCompanyEntity())
                .firstCategory(firstCategory)
                .secondCategory(secondCategory).build());
        companyArticleCompanyEntityRepository.save(
                new CompanyArticleCompanyEntity(companyArticleEntity, companyEntity));
        companyArticleCompanyEntityRepository.save(
                new CompanyArticleCompanyEntity(companyArticleEntity, anotherCompanyEntity));

        // when
        CompanyArticle testCompanyArticle = CompanyArticle.builder()
                .companyArticle(companyArticle)
                .number(articleEntity.getNumber())
                .pressNumber(pressEntity.getNumber())
                .mappedCompanyCodes(List.of(companyEntity.getCode(), anotherCompanyEntity.getCode())).build();

        // then
        CompanyArticleEntity mappedCompanyArticleEntity = mapperImpl.toCompanyArticleEntity(
                testCompanyArticle, articleEntityRepository, pressEntityRepository);
        assertThat(mappedCompanyArticleEntity).isEqualTo(companyArticleEntity);
        assertThat(mapperImpl.toCompanyArticle(companyArticleEntity,
                companyArticleCompanyEntityRepository))
                .usingRecursiveComparison().isEqualTo(testCompanyArticle);
    }

    @DisplayName("엔터티 매퍼 사용 후 CompanyArticleEntity 일관성 보장")
    @Test
    void companyArticleEntityMappingWithEntityMapper() {
        // given & when
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        PressEntity pressEntity = createPressEntity();
        CompanyArticleEntity companyArticleEntity = companyArticleEntityRepository.save(
                CompanyArticleEntity.builder().companyArticle(createCompanyArticleEntity()).article(articleEntity).press(pressEntity).build());
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategory = companyEntity.getFirstCategory();
        SecondCategoryEntity secondCategory = companyEntity.getSecondCategory();
        firstCategoryEntityRepository.save(firstCategory);
        secondCategory.updateFirstCategory(firstCategory);
        secondCategoryEntityRepository.save(secondCategory);
        companyEntity = companyEntityRepository.save(companyEntity);
        CompanyEntity anotherCompanyEntity = companyEntityRepository.save(CompanyEntity.builder()
                .company(createAnotherCompanyEntity())
                .firstCategory(firstCategory)
                .secondCategory(secondCategory).build());
        companyArticleCompanyEntityRepository.save(
                new CompanyArticleCompanyEntity(companyArticleEntity, companyEntity));
        companyArticleCompanyEntityRepository.save(
                new CompanyArticleCompanyEntity(companyArticleEntity, anotherCompanyEntity));

        // then
        assertThat(mapperImpl.toCompanyArticleEntity(
                mapperImpl.toCompanyArticle(companyArticleEntity, companyArticleCompanyEntityRepository),
                articleEntityRepository, pressEntityRepository)).isEqualTo(companyArticleEntity);
    }
}