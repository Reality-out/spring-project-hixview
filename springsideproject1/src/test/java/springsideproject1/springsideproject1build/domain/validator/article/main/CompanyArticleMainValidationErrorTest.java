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
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERROR;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_COMPANY_ARTICLE_MAIN_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyArticleMainValidationErrorTest implements CompanyArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleMainService articleMainService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleMainValidationErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLE_MAIN_TABLE, true);
    }

    @DisplayName("중복 기사 메인명을 사용하는 기사 메인 추가")
    @Test
    public void duplicatedNameCompanyArticleMainAdd() throws Exception {
        // given
        CompanyArticleMainDto articleDto = createTestCompanyArticleMainDto();
        String commonName = articleDto.getName();
        CompanyArticleMainDto articleNewDto = createTestNewCompanyArticleMainDto();
        articleNewDto.setName(commonName);

        // when
        articleMainService.registerArticle(CompanyArticleMain.builder().articleDto(articleNewDto).build());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleMainDto(ADD_COMPANY_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("중복 이미지 경로를 사용하는 기사 메인 추가")
    @Test
    public void duplicatedImagePathCompanyArticleMainAdd() throws Exception {
        // given
        CompanyArticleMainDto articleDto = createTestCompanyArticleMainDto();
        String commonImagePath = articleDto.getImagePath();
        CompanyArticleMainDto articleNewDto = createTestNewCompanyArticleMainDto();
        articleNewDto.setImagePath(commonImagePath);

        // when
        articleMainService.registerArticle(CompanyArticleMain.builder().articleDto(articleNewDto).build());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleMainDto(ADD_COMPANY_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기사 메인명까지 변경을 시도하는 기사 메인 변경")
    @Test
    public void changeNameCompanyArticleMainModify() throws Exception {
        // given
        CompanyArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleMainService.registerArticle(CompanyArticleMain.builder().articleDto(articleDto).build());

        // when
        articleDto.setName(testNewCompanyArticleMain.getName());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleMainDto(
                UPDATE_COMPANY_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
