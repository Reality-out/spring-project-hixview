package site.hixview.jpa.repository.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.domain.Press;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.PressEntity;
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
    private final CompanyArticleEntityService caEntityService;
    private final EconomyArticleEntityService eaEntityService;
    private final EconomyContentEntityService ecEntityService;
    private final EconomyArticleContentEntityService eacEntityService;
    private final IndustryArticleEntityService iaEntityService;
    private final PressEntityService pressEntityService;

    private final BlogPostEntityRepository bpEntityRepository;
    private final BlogPostArticleEntityRepository bpaEntityRepository;
    private final CompanyArticleEntityRepository caEntityRepository;
    private final EconomyArticleEntityRepository eaEntityRepository;
    private final EconomyContentEntityRepository ecEntityRepository;
    private final EconomyArticleContentEntityRepository eacEntityRepository;
    private final IndustryArticleEntityRepository iaEntityRepository;
    private final PressEntityRepository pressEntityRepository;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    PropagationTest(TestEntityManager entityManager, BlogPostEntityService bpEntityService, BlogPostArticleEntityService bpaEntityService, CompanyArticleEntityService caEntityService, EconomyArticleEntityService eaEntityService, EconomyContentEntityService ecEntityService, EconomyArticleContentEntityService eacEntityService, IndustryArticleEntityService iaEntityService, PressEntityService pressEntityService, BlogPostEntityRepository bpEntityRepository, BlogPostArticleEntityRepository bpaEntityRepository, CompanyArticleEntityRepository caEntityRepository, EconomyArticleEntityRepository eaEntityRepository, EconomyContentEntityRepository ecEntityRepository, EconomyArticleContentEntityRepository eacEntityRepository, IndustryArticleEntityRepository iaEntityRepository, PressEntityRepository pressEntityRepository, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.bpEntityService = bpEntityService;
        this.bpaEntityService = bpaEntityService;
        this.caEntityService = caEntityService;
        this.eaEntityService = eaEntityService;
        this.ecEntityService = ecEntityService;
        this.eacEntityService = eacEntityService;
        this.iaEntityService = iaEntityService;
        this.pressEntityService = pressEntityService;
        this.bpEntityRepository = bpEntityRepository;
        this.bpaEntityRepository = bpaEntityRepository;
        this.caEntityRepository = caEntityRepository;
        this.eaEntityRepository = eaEntityRepository;
        this.ecEntityRepository = ecEntityRepository;
        this.eacEntityRepository = eacEntityRepository;
        this.iaEntityRepository = iaEntityRepository;
        this.pressEntityRepository = pressEntityRepository;
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
}
