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
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMainDto;
import springsideproject1.springsideproject1build.domain.service.ArticleMainService;
import springsideproject1.springsideproject1build.util.test.ArticleMainTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.BEAN_VALIDATION_ERROR;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERROR;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_ARTICLE_MAIN_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.ADD_ARTICLE_MAIN_URL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ArticleMainBindingErrorTest implements ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ArticleMainService articleMainService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ArticleMainBindingErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAIN_TABLE, true);
    }

    @DisplayName("NotBlank(공백)에 대한 기사 메인 추가 유효성 검증")
    @Test
    public void validateNotBlankSpaceArticleMainAdd() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestArticleMainDto();
        articleDto.setName(" ");
        articleDto.setImagePath(" ");
        articleDto.setSummary(" ");
        articleDto.setArticleClassName(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(ArticleMain.builder().articleDto(articleDto).name("").build().toDto());
    }

    @DisplayName("NotBlank(null)에 대한 기사 메인 추가 유효성 검증")
    @Test
    public void validateNotBlankNullArticleMainAdd() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestArticleMainDto();
        articleDto.setName(null);
        articleDto.setImagePath(null);
        articleDto.setSummary(null);
        articleDto.setArticleClassName(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Restrict에 대한 기사 메인 추가 유효성 검증")
    @Test
    public void validateRestrictArticleMainAdd() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestArticleMainDto();
        articleDto.setArticleClassName(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotBlank(공백)에 대한 기사 메인 변경 유효성 검증")
    @Test
    public void validateNotBlankSpaceArticleMainModify() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestArticleMainDto();
        articleDto.setName(" ");
        articleDto.setImagePath(" ");
        articleDto.setSummary(" ");
        articleDto.setArticleClassName(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(ArticleMain.builder().articleDto(articleDto).name("").build().toDto());
    }

    @DisplayName("NotBlank(null)에 대한 기사 메인 변경 유효성 검증")
    @Test
    public void validateNotBlankNullArticleMainModify() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestArticleMainDto();
        articleDto.setName(null);
        articleDto.setImagePath(null);
        articleDto.setSummary(null);
        articleDto.setArticleClassName(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Restrict에 대한 기사 메인 변경 유효성 검증")
    @Test
    public void validateRestrictArticleMainModify() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestArticleMainDto();
        articleDto.setArticleClassName(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
