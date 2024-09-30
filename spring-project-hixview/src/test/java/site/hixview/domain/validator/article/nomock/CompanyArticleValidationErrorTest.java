package site.hixview.domain.validator.article.nomock;

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
import site.hixview.support.property.TestSchemaName;
import site.hixview.support.util.CompanyArticleTestUtils;
import site.hixview.support.util.CompanyTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.ERROR_SINGLE;
import static site.hixview.domain.vo.manager.RequestURL.ADD_COMPANY_ARTICLE_WITH_STRING_URL;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_COMPANY;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@TestSchemaName
@Transactional
public class CompanyArticleValidationErrorTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleValidationErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
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

    @DisplayName("중복 기사명 또는 기사 링크를 사용하는, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void duplicatedNameOrLinkCompanyArticleAddWithString() throws Exception {
        // given
        CompanyArticleBufferSimple articleBufferDuplicatedName = CompanyArticleBufferSimple.builder().article(CompanyArticle.builder()
                .article(testEqualDateCompanyArticle).link(testCompanyArticle.getLink()).build()).build();
        CompanyArticleBufferSimple articleBufferDuplicatedLink = CompanyArticleBufferSimple.builder().article(CompanyArticle.builder()
                .article(testEqualDateCompanyArticle).name(testCompanyArticle.getName()).build()).build();

        // when
        articleService.registerArticle(testEqualDateCompanyArticle);
        companyService.registerCompany(samsungElectronics);

        // then
        for (CompanyArticleBufferSimple articleBuffer : List.of(articleBufferDuplicatedName, articleBufferDuplicatedLink)) {
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
    }
}
