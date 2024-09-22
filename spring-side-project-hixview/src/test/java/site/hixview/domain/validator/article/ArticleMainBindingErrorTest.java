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
import site.hixview.domain.entity.article.ArticleMain;
import site.hixview.domain.entity.article.ArticleMainDto;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.util.test.ArticleMainTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE;
import static site.hixview.domain.vo.name.ExceptionName.BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.SchemaName.TEST_ARTICLE_MAINS_SCHEMA;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_ARTICLE_MAIN_URL;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
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
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAINS_SCHEMA, true);
    }

    @DisplayName("NotBlank(공백)에 대한 기사 메인 추가 유효성 검증")
    @Test
    public void validateNotBlankSpaceArticleMainAdd() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setName(" ");
        articleDto.setImagePath(" ");
        articleDto.setSummary(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(ArticleMain.builder().articleDto(articleDto).name("").build().toDto());
    }

    @DisplayName("NotBlank(null)에 대한 기사 메인 추가 유효성 검증")
    @Test
    public void validateNotBlankNullArticleMainAdd() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setName(null);
        articleDto.setImagePath(null);
        articleDto.setSummary(null);
        articleDto.setArticleClassName(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Restrict에 대한 기사 메인 추가 유효성 검증")
    @Test
    public void validateRestrictArticleMainAdd() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setArticleClassName(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Size에 대한 기사 메인 추가 유효성 검증")
    @Test
    public void validateSizeArticleMainAdd() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setImagePath(getRandomLongString(81));
        articleDto.setSummary(getRandomLongString(37));

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("NotBlank(공백)에 대한 기사 메인 변경 유효성 검증")
    @Test
    public void validateNotBlankSpaceArticleMainModify() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setName(" ");
        articleDto.setImagePath(" ");
        articleDto.setSummary(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(ArticleMain.builder().articleDto(articleDto).name("").build().toDto());
    }

    @DisplayName("NotBlank(null)에 대한 기사 메인 변경 유효성 검증")
    @Test
    public void validateNotBlankNullArticleMainModify() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setName(null);
        articleDto.setImagePath(null);
        articleDto.setSummary(null);
        articleDto.setArticleClassName(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Restrict에 대한 기사 메인 변경 유효성 검증")
    @Test
    public void validateRestrictArticleMainModify() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setArticleClassName(INVALID_VALUE.toUpperCase());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("Size에 대한 기사 메인 변경 유효성 검증")
    @Test
    public void validateSizeArticleMainModify() throws Exception {
        // given & when
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleDto.setImagePath(getRandomLongString(81));
        articleDto.setSummary(getRandomLongString(37));

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, BEAN_VALIDATION_ERROR))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}