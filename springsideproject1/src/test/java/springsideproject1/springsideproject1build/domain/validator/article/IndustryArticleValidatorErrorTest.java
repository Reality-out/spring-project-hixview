package springsideproject1.springsideproject1build.domain.validator.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleBufferSimple;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleDto;
import springsideproject1.springsideproject1build.domain.service.IndustryArticleService;
import springsideproject1.springsideproject1build.util.test.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.*;
import static springsideproject1.springsideproject1build.domain.vo.DATABASE.TEST_INDUSTRY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IndustryArticleValidatorErrorTest implements IndustryArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IndustryArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public IndustryArticleValidatorErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLE_TABLE, true);
    }

    @DisplayName("미래의 기사 입력일을 사용하는 산업 기사 추가 유효성 검증")
    @Test
    public void futureDateIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDtoFuture = createTestIndustryArticleDto();
        articleDtoFuture.setYear(2099);
        articleDtoFuture.setMonth(12);
        articleDtoFuture.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDtoFuture))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDtoFuture);
    }

    @DisplayName("기사 입력일이 유효하지 않은 산업 기사 추가 유효성 검증")
    @Test
    public void invalidDateIndustryArticleAdd() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("중복 기사명을 사용하는 산업 기사 추가")
    @Test
    public void duplicatedNameIndustryArticleAdd() throws Exception {
        // given
        IndustryArticle article1 = testIndustryArticle;
        String commonName = article1.getName();
        IndustryArticleDto articleDto2 = createTestNewIndustryArticleDto();
        articleDto2.setName(commonName);

        // when
        articleService.registerArticle(article1);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto2))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto2);
    }

    @DisplayName("중복 기사 링크를 사용하는 산업 기사 추가")
    @Test
    public void duplicatedLinkIndustryArticleAdd() throws Exception {
        // given
        IndustryArticle article1 = testIndustryArticle;
        String commonLink = article1.getLink();
        IndustryArticleDto articleDto2 = createTestNewIndustryArticleDto();
        articleDto2.setLink(commonLink);

        // when
        articleService.registerArticle(article1);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto2))
                .andExpectAll(view().name(addSingleIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto2);
    }

    @DisplayName("기사 입력일이 유효하지 않은, 문자열을 사용하는 산업 기사들 추가")
    @Test
    public void invalidDateIndustryArticleAddWithString() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);
        IndustryArticleBufferSimple articleBuffer = IndustryArticleBufferSimple.builder().articleDto(articleDto).build();

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, articleBuffer.getNameDatePressString());
                    put(linkString, articleBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, articleBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, articleBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(
                                URL_REDIRECT_PREFIX + ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }

    @DisplayName("중복 기사명을 사용하는, 문자열을 사용하는 산업 기사들 추가")
    @Test
    public void duplicatedNameIndustryArticleAddWithString() throws Exception {
        // given & when
        articleService.registerArticle(IndustryArticle.builder().article(testIndustryArticle).name(testEqualDateIndustryArticle.getName()).build());

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateIndustryArticleStringBuffer.getNameDatePressString());
                    put(linkString, testEqualDateIndustryArticleStringBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, testEqualDateIndustryArticleStringBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, testEqualDateIndustryArticleStringBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(
                                URL_REDIRECT_PREFIX + ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }

    @DisplayName("중복 기사 링크를 사용하는, 문자열을 사용하는 산업 기사들 추가")
    @Test
    public void duplicatedLinkIndustryArticleAddWithString() throws Exception {
        // given & when
        articleService.registerArticle(IndustryArticle.builder().article(testIndustryArticle).link(testEqualDateIndustryArticle.getLink()).build());

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateIndustryArticleStringBuffer.getNameDatePressString());
                    put(linkString, testEqualDateIndustryArticleStringBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, testEqualDateIndustryArticleStringBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, testEqualDateIndustryArticleStringBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(
                                URL_REDIRECT_PREFIX + ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }

    @DisplayName("미래의 기사 입력일을 사용하는 산업 기사 변경 유효성 검증")
    @Test
    public void futureDateIndustryArticleModify() throws Exception {
        IndustryArticleDto articleDtoFuture = createTestIndustryArticleDto();
        articleDtoFuture.setYear(2099);
        articleDtoFuture.setMonth(12);
        articleDtoFuture.setDays(31);

        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDtoFuture))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDtoFuture);
    }

    @DisplayName("기사 입력일이 유효하지 않은 산업 기사 변경")
    @Test
    public void invalidDateIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleDto.setYear(2000);
        articleDto.setMonth(2);
        articleDto.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("존재하지 않는 산업 기사명을 사용하는 산업 기사 변경")
    @Test
    public void notExistNameIndustryArticleModify() throws Exception {
        // given
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleService.registerArticle(testIndustryArticle);

        // when
        articleDto.setName(testNewIndustryArticle.getName());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("존재하지 않는 산업 링크를 사용하는 산업 기사 변경")
    @Test
    public void notExistLinkIndustryArticleModify() throws Exception {
        // given
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        articleService.registerArticle(testIndustryArticle);

        // when
        articleDto.setLink(testNewIndustryArticle.getLink());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("대상 산업이 추가되지 않은 산업 기사 변경")
    @Test
    public void notRegisteredSubjectIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
