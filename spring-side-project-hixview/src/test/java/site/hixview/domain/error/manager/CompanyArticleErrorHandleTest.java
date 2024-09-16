package site.hixview.domain.error.manager;

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
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.CompanyArticleTestUtils;
import site.hixview.util.test.CompanyTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_COMPANY;
import static site.hixview.domain.vo.name.ExceptionName.*;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANY_ARTICLES_SCHEMA;
import static site.hixview.domain.vo.name.ViewName.BEFORE_PROCESS_VIEW;
import static site.hixview.domain.vo.name.ViewName.PROCESS_VIEW;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.REMOVE_COMPANY_URL_ARTICLE_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_COMPANY_ARTICLE_VIEW;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyArticleErrorHandleTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleErrorHandleTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("존재하지 않는 대상 기업을 사용하는, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void NotFoundSubjectCompanyArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateCompanyArticleStringBuffer.getNameDatePressString());
                    put(SUBJECT_COMPANY, INVALID_VALUE);
                    put(linkString, testEqualDateCompanyArticleStringBuffer.getLinkString());
                }}))
                .andExpectAll(view().name(addStringCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }

    @DisplayName("기사 리스트의 크기가 링크 리스트의 크기보다 큰, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void articleListBiggerCompanyArticleAddWithString() throws Exception {
        // given & when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testCompanyArticleStringBuffer.getNameDatePressString());
                    put(SUBJECT_COMPANY, testCompanyArticleStringBuffer.getSubjectCompany());
                    put(linkString, testEqualDateCompanyArticle.getLink());
                }}))
                .andExpectAll(view().name(addStringCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("링크 리스트의 크기가 기사 리스트의 크기보다 큰, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void linkListBiggerCompanyArticleAddWithString() throws Exception {
        // given & when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, CompanyArticleBufferSimple.builder().article(testNewCompanyArticle).build().getNameDatePressString());
                    put(SUBJECT_COMPANY, testCompanyArticleStringBuffer.getSubjectCompany());
                    put(linkString, testCompanyArticleStringBuffer.getLinkString());
                }}))
                .andExpectAll(view().name(addStringCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("비어 있는 기사를 사용하는, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void emptyCompanyArticleAddWithString() throws Exception {
        // given & when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, "");
                    put(SUBJECT_COMPANY, samsungElectronics.getName());
                    put(linkString, "");
                }}))
                .andExpectAll(view().name(addStringCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_BLANK_ARTICLE_ERROR)));
    }

    @DisplayName("서식이 올바르지 않은 입력일 값을 포함하는, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void dateFormatCompanyArticleAddWithString() throws Exception {
        // given & when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateCompanyArticleStringBuffer.getNameDatePressString().replace("2024", INVALID_VALUE));
                    put(SUBJECT_COMPANY, testEqualDateCompanyArticleStringBuffer.getSubjectCompany());
                    put(linkString, testEqualDateCompanyArticleStringBuffer.getLinkString());
                }}))
                .andExpectAll(view().name(
                                REDIRECT_URL + ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, NUMBER_FORMAT_LOCAL_DATE_ERROR)));
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하여 기업 기사를 검색하는, 기업 기사 변경")
    @Test
    public void NotFoundNumberOrNameCompanyArticleModify() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_ARTICLE_URL, "numberOrName", ""))
                .andExpectAll(view().name(UPDATE_COMPANY_ARTICLE_VIEW + BEFORE_PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_ARTICLE_URL, "numberOrName", "1"))
                .andExpectAll(view().name(UPDATE_COMPANY_ARTICLE_VIEW + BEFORE_PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_ARTICLE_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_COMPANY_ARTICLE_VIEW + BEFORE_PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR)));
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

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하는, 기업 기사 없애기")
    @Test
    public void NotFoundArticleNumberOrNameCompanyArticleRid() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, "numberOrName", ""))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_ARTICLE_VIEW + PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, "numberOrName", "1"))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_ARTICLE_VIEW + PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_ARTICLE_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_ARTICLE_VIEW + PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ARTICLE_ERROR)));
    }
}
