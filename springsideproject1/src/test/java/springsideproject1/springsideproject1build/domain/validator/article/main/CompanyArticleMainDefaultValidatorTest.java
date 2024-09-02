package springsideproject1.springsideproject1build.domain.validator.article.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleMainDto;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleMainService;
import springsideproject1.springsideproject1build.util.test.CompanyArticleMainTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.BEAN_VALIDATION_ERROR;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERROR;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_COMPANY_ARTICLE_MAIN_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.ADD_COMPANY_ARTICLE_MAIN_URL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyArticleMainDefaultValidatorTest implements CompanyArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleMainService articleMainService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleMainDefaultValidatorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLE_MAIN_TABLE, true);
    }

    @DisplayName("NotBlank(공백)에 대한 기업 기사 메인 추가 유효성 검증")
    @Test
    public void validateNotBlankSpaceCompanyArticleMainAdd() throws Exception {
        // given & when
        CompanyArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setName(" ");
        articleDto.setImagePath(" ");
        articleDto.setSummary(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleMainDto(ADD_COMPANY_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(CompanyArticleMain.builder().articleDto(articleDto).name("").build().toDto());
    }

    @DisplayName("NotBlank(null)에 대한 기업 기사 메인 추가 유효성 검증")
    @Test
    public void validateNotBlankNullCompanyArticleMainAdd() throws Exception {
        // given & when
        CompanyArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setName(null);
        articleDto.setImagePath(null);
        articleDto.setSummary(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleMainDto(ADD_COMPANY_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotBlank(공백)에 대한 기업 기사 메인 변경 유효성 검증")
    @Test
    public void validateNotBlankSpaceCompanyArticleMainModify() throws Exception {
        // given & when
        CompanyArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setName(" ");
        articleDto.setImagePath(" ");
        articleDto.setSummary(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(CompanyArticleMain.builder().articleDto(articleDto).name("").build().toDto());
    }

    @DisplayName("NotBlank(null)에 대한 기업 기사 메인 변경 유효성 검증")
    @Test
    public void validateNotBlankNullCompanyArticleMainModify() throws Exception {
        // given & when
        CompanyArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setName(null);
        articleDto.setImagePath(null);
        articleDto.setSummary(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
