package site.hixview.domain.validator.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.CompanyArticleDto;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.CompanyArticleTestUtils;
import site.hixview.util.test.CompanyTestUtils;

import javax.sql.DataSource;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.name.ExceptionName.BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANY_ARTICLES_SCHEMA;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_COMPANY_ARTICLE_URL;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@Transactional
public class CompanyArticleBindingErrorTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleBindingErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("NotBlank(공백)에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateNotBlankSpaceCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setName(" ");
        articleDto.setPress(" ");
        articleDto.setSubjectCompany(" ");
        articleDto.setLink(" ");
        CompanyArticleDto returnedArticleDto = copyCompanyArticleDto(articleDto);
        returnedArticleDto.setName("");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(returnedArticleDto);
    }

    @DisplayName("NotBlank(null)에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateNotBlankNullCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setName(null);
        articleDto.setPress(null);
        articleDto.setSubjectCompany(null);
        articleDto.setLink(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotNull에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateNotNullCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setYear(null);
        articleDto.setMonth(null);
        articleDto.setDays(null);
        articleDto.setImportance(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Pattern에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validatePatternCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setLink(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Range에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateRangeCompanyArticleAdd() throws Exception {
        // given
        CompanyArticleDto articleDtoFallShortOf = createTestCompanyArticleDto();
        articleDtoFallShortOf.setYear(1959);
        articleDtoFallShortOf.setMonth(0);
        articleDtoFallShortOf.setDays(0);

        CompanyArticleDto articleDtoExceed = createTestCompanyArticleDto();
        articleDtoExceed.setYear(2100);
        articleDtoExceed.setMonth(13);
        articleDtoExceed.setDays(32);

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoFallShortOf, articleDtoExceed)) {
            assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                    .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                            model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                            model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(articleDto);
        }
    }

    @DisplayName("Restrict에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateRestrictCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setImportance(3);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Size에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateSizeCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setName(getRandomLongString(81));
        articleDto.setSubjectCompany(getRandomLongString(13));
        articleDto.setLink(getRandomLongString(401));

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("typeMismatch에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateTypeMismatchCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();

        // then
        mockMvc.perform(post(ADD_SINGLE_COMPANY_ARTICLE_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(NAME, articleDto.getName())
                        .param(PRESS, articleDto.getPress())
                        .param(SUBJECT_COMPANY, articleDto.getSubjectCompany())
                        .param(LINK, articleDto.getLink())
                        .param(YEAR, INVALID_VALUE)
                        .param(MONTH, INVALID_VALUE)
                        .param(DAYS, INVALID_VALUE)
                        .param(IMPORTANCE, INVALID_VALUE))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("Press의 typeMismatch에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validatePressTypeMismatchCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setPress(INVALID_VALUE);

        // then
        mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("NotBlank(공백)에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateNotBlankSpaceCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setName(" ");
        articleDto.setPress(" ");
        articleDto.setSubjectCompany(" ");
        articleDto.setLink(" ");
        CompanyArticleDto returnedArticleDto = copyCompanyArticleDto(articleDto);
        returnedArticleDto.setName("");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(returnedArticleDto);
    }

    @DisplayName("NotBlank(null)에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateNotBlankNullCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setName(null);
        articleDto.setPress(null);
        articleDto.setSubjectCompany(null);
        articleDto.setLink(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotNull에 대한 기업 기사 추가 유효성 검증")
    @Test
    public void validateNotNullCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setYear(null);
        articleDto.setMonth(null);
        articleDto.setDays(null);
        articleDto.setImportance(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Pattern에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validatePatternCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setLink(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Range에 대한 기업 변경 추가 유효성 검증")
    @Test
    public void validateRangeCompanyArticleModify() throws Exception {
        // given
        CompanyArticleDto articleDtoFallShortOf = createTestCompanyArticleDto();
        articleDtoFallShortOf.setYear(1959);
        articleDtoFallShortOf.setMonth(0);
        articleDtoFallShortOf.setDays(0);

        CompanyArticleDto articleDtoExceed = createTestCompanyArticleDto();
        articleDtoExceed.setYear(2100);
        articleDtoExceed.setMonth(13);
        articleDtoExceed.setDays(32);

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoFallShortOf, articleDtoExceed)) {
            assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                    .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                            model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                            model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                    .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                    .usingRecursiveComparison()
                    .isEqualTo(articleDto);
        }
    }

    @DisplayName("Restrict에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateRestrictCompanyArticleModify() throws Exception {
        // given
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleService.registerArticle(testCompanyArticle);

        // when
        articleDto.setImportance(3);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Size에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateSizeCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setName(getRandomLongString(81));
        articleDto.setSubjectCompany(getRandomLongString(13));
        articleDto.setLink(getRandomLongString(401));

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("typeMismatch에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validateTypeMismatchCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();

        // then
        mockMvc.perform(post(modifyCompanyArticleFinishUrl).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(NAME, articleDto.getName())
                        .param(PRESS, articleDto.getPress())
                        .param(SUBJECT_COMPANY, articleDto.getSubjectCompany())
                        .param(LINK, articleDto.getLink())
                        .param(YEAR, INVALID_VALUE)
                        .param(MONTH, INVALID_VALUE)
                        .param(DAYS, INVALID_VALUE)
                        .param(IMPORTANCE, INVALID_VALUE))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE));
    }

    @DisplayName("Press의 typeMismatch에 대한 기업 기사 변경 유효성 검증")
    @Test
    public void validatePressTypeMismatchCompanyArticleModify() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestCompanyArticleDto();
        articleDto.setPress(INVALID_VALUE);

        // then
        mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR),
                        model().attributeExists(ARTICLE));
    }
}