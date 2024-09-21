package site.hixview.domain.validator.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.CompanyArticleBufferSimple;
import site.hixview.domain.entity.article.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.CompanyArticleTestUtils;
import site.hixview.util.test.CompanyTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_COMPANY;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANY_ARTICLES_SCHEMA;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_COMPANY_ARTICLE_WITH_STRING_URL;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_COMPANY_ARTICLE_URL;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@Transactional
public class CompanyArticleValidatorErrorTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleValidatorErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("미래의 기사 입력일을 사용하는 기업 기사 추가 유효성 검증")
    @Test
    public void futureDateCompanyArticleAdd() throws Exception {
        CompanyArticleDto articleDtoFuture = createTestCompanyArticleDto();
        articleDtoFuture.setYear(2099);
        articleDtoFuture.setMonth(12);
        articleDtoFuture.setDays(31);

        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDtoFuture))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDtoFuture);
    }

    @DisplayName("기사 입력일이 유효하지 않은 기업 기사 추가 유효성 검증")
    @Test
    public void invalidDateCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("중복 기사명을 사용하는 기업 기사 추가")
    @Test
    public void duplicatedNameCompanyArticleAdd() throws Exception {
        // given
        CompanyArticle article1 = testCompanyArticle;
        String commonName = article1.getName();
        CompanyArticleDto articleDto2 = createTestNewCompanyArticleDto();
        articleDto2.setName(commonName);

        // when
        articleService.registerArticle(article1);
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto2))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto2);
    }

    @DisplayName("중복 기사 링크를 사용하는 기업 기사 추가")
    @Test
    public void duplicatedLinkCompanyArticleAdd() throws Exception {
        // given
        CompanyArticle article1 = testCompanyArticle;
        String commonLink = article1.getLink();
        CompanyArticleDto articleDto2 = createTestNewCompanyArticleDto();
        articleDto2.setLink(commonLink);

        // when
        articleService.registerArticle(article1);
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto2))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto2);
    }

    @DisplayName("대상 기업이 추가되지 않은 기업 기사 추가")
    @Test
    public void notRegisteredSubjectCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기사 입력일이 유효하지 않은, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void invalidDateCompanyArticleAddWithString() throws Exception {
        // given
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);
        CompanyArticleBufferSimple articleBuffer = CompanyArticleBufferSimple.builder().articleDto(articleDto).build();

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, articleBuffer.getNameDatePressString());
                    put(SUBJECT_COMPANY, articleBuffer.getSubjectCompany());
                    put(linkString, articleBuffer.getLinkString());
                }}))
                .andExpectAll(view().name(
                                REDIRECT_URL + ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }

    @DisplayName("중복 기사명을 사용하는, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void duplicatedNameCompanyArticleAddWithString() throws Exception {
        // given & when
        articleService.registerArticle(CompanyArticle.builder().article(testCompanyArticle).name(testEqualDateCompanyArticle.getName()).build());
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateCompanyArticleBuffer.getNameDatePressString());
                    put(SUBJECT_COMPANY, testEqualDateCompanyArticleBuffer.getSubjectCompany());
                    put(linkString, testEqualDateCompanyArticleBuffer.getLinkString());
                }}))
                .andExpectAll(view().name(
                                REDIRECT_URL + ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }

    @DisplayName("중복 기사 링크를 사용하는, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void duplicatedLinkCompanyArticleAddWithString() throws Exception {
        // given & when
        articleService.registerArticle(CompanyArticle.builder().article(testCompanyArticle).link(testEqualDateCompanyArticle.getLink()).build());
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateCompanyArticleBuffer.getNameDatePressString());
                    put(SUBJECT_COMPANY, testEqualDateCompanyArticleBuffer.getSubjectCompany());
                    put(linkString, testEqualDateCompanyArticleBuffer.getLinkString());
                }}))
                .andExpectAll(view().name(
                                REDIRECT_URL + ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }

    @DisplayName("미래의 기사 입력일을 사용하는 기업 기사 변경 유효성 검증")
    @Test
    public void futureDateCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDtoFuture = createTestCompanyArticleDto();
        articleDtoFuture.setYear(2099);
        articleDtoFuture.setMonth(12);
        articleDtoFuture.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDtoFuture))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDtoFuture);
    }

    @DisplayName("기사 입력일이 유효하지 않은 기업 기사 변경")
    @Test
    public void invalidDateCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기사명 또는 기사 링크까지 변경을 시도하는, 기업 기사 변경")
    @Test
    public void changeNameOrLinkCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticle article = articleService.registerArticle(testCompanyArticle);
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithCompanyArticle(modifyCompanyArticleFinishUrl,
                        CompanyArticle.builder().article(article).name(testNewCompanyArticle.getName()).build()))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null)));

        requireNonNull(mockMvc.perform(postWithCompanyArticle(modifyCompanyArticleFinishUrl,
                        CompanyArticle.builder().article(article).link(testNewCompanyArticle.getLink()).build()))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null)));
    }

    @DisplayName("대상 기업이 추가되지 않은 기업 기사 변경")
    @Test
    public void notRegisteredSubjectCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
