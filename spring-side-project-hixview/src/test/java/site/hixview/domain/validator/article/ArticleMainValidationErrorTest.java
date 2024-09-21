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
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.name.SchemaName.TEST_ARTICLE_MAINS_SCHEMA;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_ARTICLE_MAIN_URL;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@Transactional
public class ArticleMainValidationErrorTest implements ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ArticleMainService articleMainService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ArticleMainValidationErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAINS_SCHEMA, true);
    }

    @DisplayName("중복 기사 메인명을 사용하는 기사 메인 추가")
    @Test
    public void duplicatedNameArticleMainAdd() throws Exception {
        // given
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        String commonName = articleDto.getName();
        ArticleMainDto articleNewDto = createTestNewCompanyArticleMainDto();
        articleNewDto.setName(commonName);

        // when
        articleMainService.registerArticle(ArticleMain.builder().articleDto(articleNewDto).build());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("중복 이미지 경로를 사용하는 기사 메인 추가")
    @Test
    public void duplicatedImagePathArticleMainAdd() throws Exception {
        // given
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        String commonImagePath = articleDto.getImagePath();
        ArticleMainDto articleNewDto = createTestNewCompanyArticleMainDto();
        articleNewDto.setImagePath(commonImagePath);

        // when
        articleMainService.registerArticle(ArticleMain.builder().articleDto(articleNewDto).build());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기사 메인명까지 변경을 시도하는 기사 메인 변경")
    @Test
    public void changeNameArticleMainModify() throws Exception {
        // given
        ArticleMainDto articleDto = createTestCompanyArticleMainDto();
        articleMainService.registerArticle(ArticleMain.builder().articleDto(articleDto).build());

        // when
        articleDto.setName(testNewCompanyArticleMain.getName());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(
                UPDATE_ARTICLE_MAIN_URL + FINISH_URL, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
