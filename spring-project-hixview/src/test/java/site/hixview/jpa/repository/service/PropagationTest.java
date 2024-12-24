package site.hixview.jpa.repository.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.aggregate.domain.*;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.repository.*;
import site.hixview.jpa.service.*;
import site.hixview.support.jpa.context.RealRepositoryAndServiceContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.BlogPostArticleEntityTestUtils;
import site.hixview.support.jpa.util.CompanyArticleCompanyEntityTestUtils;
import site.hixview.support.jpa.util.EconomyArticleContentEntityTestUtils;
import site.hixview.support.jpa.util.IndustryArticleSecondCategoryEntityTestUtils;
import site.hixview.support.spring.util.BlogPostTestUtils;
import site.hixview.support.spring.util.EconomyContentTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@RealRepositoryAndServiceContext
@Slf4j
class PropagationTest implements BlogPostArticleEntityTestUtils, CompanyArticleCompanyEntityTestUtils, IndustryArticleSecondCategoryEntityTestUtils, EconomyArticleContentEntityTestUtils, BlogPostTestUtils, EconomyContentTestUtils {

    private final TestEntityManager entityManager;
    private final BlogPostEntityService bpEntityService;
    private final BlogPostArticleEntityService bpaEntityService;
    private final CompanyEntityService companyEntityService;
    private final CompanyArticleEntityService caEntityService;
    private final CompanyArticleCompanyEntityService cacEntityService;
    private final EconomyArticleEntityService eaEntityService;
    private final EconomyContentEntityService ecEntityService;
    private final EconomyArticleContentEntityService eacEntityService;
    private final FirstCategoryEntityService fcEntityService;
    private final IndustryArticleEntityService iaEntityService;
    private final IndustryArticleSecondCategoryEntityService iascEntityService;
    private final PressEntityService pressEntityService;
    private final SecondCategoryEntityService scEntityService;

    private final BlogPostEntityRepository bpEntityRepository;
    private final BlogPostArticleEntityRepository bpaEntityRepository;
    private final CompanyEntityRepository companyEntityRepository;
    private final CompanyArticleEntityRepository caEntityRepository;
    private final CompanyArticleCompanyEntityRepository cacEntityRepository;
    private final EconomyArticleEntityRepository eaEntityRepository;
    private final EconomyContentEntityRepository ecEntityRepository;
    private final EconomyArticleContentEntityRepository eacEntityRepository;
    private final FirstCategoryEntityRepository fcEntityRepository;
    private final IndustryArticleEntityRepository iaEntityRepository;
    private final IndustryArticleSecondCategoryEntityRepository iascEntityRepository;
    private final PressEntityRepository pressEntityRepository;
    private final SecondCategoryEntityRepository scEntityRepository;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    PropagationTest(TestEntityManager entityManager, BlogPostEntityService bpEntityService, BlogPostArticleEntityService bpaEntityService, CompanyEntityService companyEntityService, CompanyArticleEntityService caEntityService, CompanyArticleCompanyEntityService cacEntityService, EconomyArticleEntityService eaEntityService, EconomyContentEntityService ecEntityService, EconomyArticleContentEntityService eacEntityService, FirstCategoryEntityService fcEntityService, IndustryArticleEntityService iaEntityService, IndustryArticleSecondCategoryEntityService iascEntityService, PressEntityService pressEntityService, SecondCategoryEntityService scEntityService, BlogPostEntityRepository bpEntityRepository, BlogPostArticleEntityRepository bpaEntityRepository, CompanyEntityRepository companyEntityRepository, CompanyArticleEntityRepository caEntityRepository, CompanyArticleCompanyEntityRepository cacEntityRepository, EconomyArticleEntityRepository eaEntityRepository, EconomyContentEntityRepository ecEntityRepository, EconomyArticleContentEntityRepository eacEntityRepository, FirstCategoryEntityRepository fcEntityRepository, IndustryArticleEntityRepository iaEntityRepository, IndustryArticleSecondCategoryEntityRepository iascEntityRepository, PressEntityRepository pressEntityRepository, SecondCategoryEntityRepository scEntityRepository, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.bpEntityService = bpEntityService;
        this.bpaEntityService = bpaEntityService;
        this.companyEntityService = companyEntityService;
        this.caEntityService = caEntityService;
        this.cacEntityService = cacEntityService;
        this.eaEntityService = eaEntityService;
        this.ecEntityService = ecEntityService;
        this.eacEntityService = eacEntityService;
        this.fcEntityService = fcEntityService;
        this.iaEntityService = iaEntityService;
        this.iascEntityService = iascEntityService;
        this.pressEntityService = pressEntityService;
        this.scEntityService = scEntityService;
        this.bpEntityRepository = bpEntityRepository;
        this.bpaEntityRepository = bpaEntityRepository;
        this.companyEntityRepository = companyEntityRepository;
        this.caEntityRepository = caEntityRepository;
        this.cacEntityRepository = cacEntityRepository;
        this.eaEntityRepository = eaEntityRepository;
        this.ecEntityRepository = ecEntityRepository;
        this.eacEntityRepository = eacEntityRepository;
        this.fcEntityRepository = fcEntityRepository;
        this.iaEntityRepository = iaEntityRepository;
        this.iascEntityRepository = iascEntityRepository;
        this.pressEntityRepository = pressEntityRepository;
        this.scEntityRepository = scEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteAll(jdbcTemplate);
    }

    @DisplayName("블로그 포스트 엔터티 전파 테스트")
    @Test
    void blogPostEntityPropagationTest() {
        // given
        bpaEntityRepository.save(createBlogPostArticleEntity());

        // when
        String name = blogPost.getName();
        Long number = bpEntityRepository.findByName(name).orElseThrow().getNumber();
        BlogPost updatedBlogPost = BlogPost.builder().blogPost(anotherBlogPost).number(number).build();
        entityManager.clear();
        bpEntityService.update(updatedBlogPost);

        // then
        entityManager.clear();
        assertThat(bpaEntityService.getByBlogPost(updatedBlogPost).size()).isEqualTo(1);
    }

    @DisplayName("기업 엔터티 전파 테스트")
    @Test
    void companyEntityPropagationTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity fcEntity = companyEntity.getFirstCategory();
        SecondCategoryEntity scEntity = companyEntity.getSecondCategory();
        CompanyArticleCompanyEntity cacEntity = createCompanyArticleCompanyEntity();
        scEntity.updateFirstCategory(fcEntity);
        cacEntity.updateCompany(companyEntity);
        fcEntityRepository.save(fcEntity);
        scEntityRepository.save(scEntity);
        companyEntityRepository.save(companyEntity);
        cacEntityRepository.save(cacEntity);

        // when
        Company updatedCompany = Company.builder().company(anotherCompany).code(companyEntity.getCode()).firstCategoryNumber(companyEntity.getFirstCategory().getNumber()).secondCategoryNumber(companyEntity.getSecondCategory().getNumber()).build();
        entityManager.clear();
        companyEntityService.update(updatedCompany);

        // then
        entityManager.clear();
        assertThat(cacEntityService.getByCompany(updatedCompany).size()).isEqualTo(1);
    }

    @DisplayName("기업 기사 엔터티 전파 테스트")
    @Test
    void companyArticleEntityPropagationTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity fcEntity = companyEntity.getFirstCategory();
        SecondCategoryEntity scEntity = companyEntity.getSecondCategory();
        CompanyArticleCompanyEntity cacEntity = createCompanyArticleCompanyEntity();
        CompanyArticleEntity caEntity = cacEntity.getCompanyArticle();
        scEntity.updateFirstCategory(fcEntity);
        cacEntity.updateCompany(companyEntity);
        fcEntityRepository.save(fcEntity);
        scEntityRepository.save(scEntity);
        companyEntityRepository.save(companyEntity);
        cacEntityRepository.save(cacEntity);

        // when
        CompanyArticle updatedCompanyArticle = CompanyArticle.builder().companyArticle(anotherCompanyArticle).number(caEntity.getNumber()).pressNumber(caEntity.getPress().getNumber()).build();
        entityManager.clear();
        caEntityService.update(updatedCompanyArticle);

        // then
        entityManager.clear();
        assertThat(cacEntityService.getByCompanyArticle(updatedCompanyArticle).size()).isEqualTo(1);
    }

    @DisplayName("경제 기사 엔터티 전파 테스트")
    @Test
    void economyArticleEntityPropagationTest() {
        // given
        EconomyArticleContentEntity eacEntity = createEconomyArticleContentEntity();
        EconomyArticleEntity eaEntity = eacEntity.getEconomyArticle();
        eacEntityRepository.save(eacEntity);

        // when
        EconomyArticle updatedEconomyArticle = EconomyArticle.builder().economyArticle(anotherEconomyArticle).number(eaEntity.getNumber()).pressNumber(eaEntity.getPress().getNumber()).build();
        entityManager.clear();
        eaEntityService.update(updatedEconomyArticle);

        // then
        entityManager.clear();
        assertThat(eacEntityService.getByEconomyArticle(updatedEconomyArticle).size()).isEqualTo(1);
    }

    @DisplayName("경제 컨텐츠 엔터티 전파 테스트")
    @Test
    void economyContentEntityPropagationTest() {
        // given
        eacEntityRepository.save(createEconomyArticleContentEntity());

        // when
        String name = economyContent.getName();
        Long number = ecEntityRepository.findByName(name).orElseThrow().getNumber();
        EconomyContent updatedEconomyContent = EconomyContent.builder().economyContent(anotherEconomyContent).number(number).build();
        entityManager.clear();
        ecEntityService.update(updatedEconomyContent);

        // then
        entityManager.clear();
        assertThat(eacEntityService.getByEconomyContent(updatedEconomyContent).size()).isEqualTo(1);
    }

    @DisplayName("1차 업종 엔터티 전파 테스트")
    @Test
    void firstCategoryEntityPropagationTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategoryEntity = createFirstCategoryEntity();
        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        companyEntity.updateFirstCategory(firstCategoryEntity);
        companyEntity.updateSecondCategory(secondCategoryEntity);
        fcEntityRepository.save(firstCategoryEntity);
        scEntityRepository.save(secondCategoryEntity);
        companyEntityRepository.save(companyEntity);
        iaEntityRepository.save(IndustryArticleEntity.builder().industryArticle(createIndustryArticleEntity()).firstCategory(firstCategoryEntity).build());
        entityManager.flush();

        // when
        String englishName = firstCategoryEntity.getEnglishName();
        Long number = fcEntityRepository.findByEnglishName(englishName).orElseThrow().getNumber();
        FirstCategory updatedFirstCategory = FirstCategory.builder().firstCategory(anotherFirstCategory)
                .industryCategoryNumber(firstCategoryEntity.getIndustryCategory().getNumber()).number(number).build();
        entityManager.clear();
        fcEntityService.update(updatedFirstCategory);

        // then
        assertThat(scEntityService.getByFirstCategory(updatedFirstCategory).size()).isEqualTo(1);
        assertThat(companyEntityService.getByFirstCategory(updatedFirstCategory).size()).isEqualTo(1);
        assertThat(iaEntityService.getByFirstCategory(updatedFirstCategory).size()).isEqualTo(1);
    }

    @DisplayName("산업 기사 엔터티 전파 테스트")
    @Test
    void industryArticleEntityPropagationTest() {
        // given
        FirstCategoryEntity fcEntity = createFirstCategoryEntity();
        SecondCategoryEntity scEntity = createSecondCategoryEntity();
        IndustryArticleSecondCategoryEntity iascEntity = createIndustryArticleSecondCategoryEntity();
        IndustryArticleEntity iaEntity = iascEntity.getIndustryArticle();
        scEntity.updateFirstCategory(fcEntity);
        iaEntity.updateFirstCategory(fcEntity);
        iascEntity.updateSecondCategory(scEntity);
        fcEntityRepository.save(fcEntity);
        scEntityRepository.save(scEntity);
        iascEntityRepository.save(iascEntity);

        // when
        IndustryArticle updatedIndustryArticle = IndustryArticle.builder().industryArticle(anotherIndustryArticle).number(iaEntity.getNumber()).pressNumber(iaEntity.getPress().getNumber()).firstCategoryNumber(iaEntity.getFirstCategory().getNumber()).build();
        entityManager.clear();
        iaEntityService.update(updatedIndustryArticle);

        // then
        entityManager.clear();
        assertThat(iascEntityService.getByIndustryArticle(updatedIndustryArticle).size()).isEqualTo(1);
    }

    @DisplayName("언론사 엔터티 전파 테스트")
    @Test
    void pressEntityPropagationTest() {
        // given
        PressEntity pressEntity = createPressEntity();
        caEntityRepository.save(CompanyArticleEntity.builder().companyArticle(createCompanyArticleEntity()).press(pressEntity).build());
        eaEntityRepository.save(EconomyArticleEntity.builder().economyArticle(createEconomyArticleEntity()).press(pressEntity).build());
        iaEntityRepository.save(IndustryArticleEntity.builder().industryArticle(createIndustryArticleEntity()).press(pressEntity).build());
        entityManager.flush();

        // when
        String englishName = pressEntity.getEnglishName();
        Long number = pressEntityRepository.findByEnglishName(englishName).orElseThrow().getNumber();
        Press updatedPress = Press.builder().press(anotherPress).number(number).build();
        entityManager.clear();
        pressEntityService.update(updatedPress);

        // then
        assertThat(caEntityService.getByPress(updatedPress).size()).isEqualTo(1);
        assertThat(eaEntityService.getByPress(updatedPress).size()).isEqualTo(1);
        assertThat(iaEntityService.getByPress(updatedPress).size()).isEqualTo(1);
    }

    @DisplayName("2차 업종 엔터티 전파 테스트")
    @Test
    void secondCategoryEntityPropagationTest() {
        // given
        CompanyEntity companyEntity = createCompanyEntity();
        FirstCategoryEntity firstCategoryEntity = createFirstCategoryEntity();
        SecondCategoryEntity secondCategoryEntity = createSecondCategoryEntity();
        IndustryArticleEntity industryArticleEntity = createIndustryArticleEntity();
        IndustryArticleSecondCategoryEntity iascEntity = createIndustryArticleSecondCategoryEntity();
        secondCategoryEntity.updateFirstCategory(firstCategoryEntity);
        companyEntity.updateFirstCategory(firstCategoryEntity);
        companyEntity.updateSecondCategory(secondCategoryEntity);
        industryArticleEntity.updateFirstCategory(firstCategoryEntity);
        iascEntity.updateIndustryArticle(industryArticleEntity);
        iascEntity.updateSecondCategory(secondCategoryEntity);
        fcEntityRepository.save(firstCategoryEntity);
        scEntityRepository.save(secondCategoryEntity);
        companyEntityRepository.save(companyEntity);
        iaEntityRepository.save(industryArticleEntity);
        iascEntityRepository.save(iascEntity);
        entityManager.flush();

        // when
        String englishName = secondCategoryEntity.getEnglishName();
        Long number = scEntityRepository.findByEnglishName(englishName).orElseThrow().getNumber();
        SecondCategory updatedSecondCategory = SecondCategory.builder().secondCategory(anotherSecondCategory)
                .industryCategoryNumber(secondCategoryEntity.getIndustryCategory().getNumber())
                .firstCategoryNumber(secondCategoryEntity.getFirstCategory().getNumber()).number(number).build();
        entityManager.clear();
        scEntityService.update(updatedSecondCategory);

        // then
        assertThat(companyEntityService.getBySecondCategory(updatedSecondCategory).size()).isEqualTo(1);
        assertThat(iascEntityService.getBySecondCategory(updatedSecondCategory).size()).isEqualTo(1);
    }
}
