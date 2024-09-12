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
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERROR;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.vo.DATABASE.TEST_ARTICLE_MAIN_TABLE;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;

@SpringBootTest
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
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAIN_TABLE, true);
    }

    @DisplayName("중복 기사 메인명을 사용하는 기사 메인 추가")
    @Test
    public void duplicatedNameArticleMainAdd() throws Exception {
        // given
        ArticleMainDto articleDto = createTestArticleMainDto();
        String commonName = articleDto.getName();
        ArticleMainDto articleNewDto = createTestNewArticleMainDto();
        articleNewDto.setName(commonName);

        // when
        articleMainService.registerArticle(ArticleMain.builder().articleDto(articleNewDto).build());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("중복 이미지 경로를 사용하는 기사 메인 추가")
    @Test
    public void duplicatedImagePathArticleMainAdd() throws Exception {
        // given
        ArticleMainDto articleDto = createTestArticleMainDto();
        String commonImagePath = articleDto.getImagePath();
        ArticleMainDto articleNewDto = createTestNewArticleMainDto();
        articleNewDto.setImagePath(commonImagePath);

        // when
        articleMainService.registerArticle(ArticleMain.builder().articleDto(articleNewDto).build());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                .andExpectAll(view().name(addArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("기사 메인명까지 변경을 시도하는 기사 메인 변경")
    @Test
    public void changeNameArticleMainModify() throws Exception {
        // given
        ArticleMainDto articleDto = createTestArticleMainDto();
        articleMainService.registerArticle(ArticleMain.builder().articleDto(articleDto).build());

        // when
        articleDto.setName(testNewCompanyArticleMain.getName());

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithArticleMainDto(
                UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX, articleDto))
                .andExpectAll(view().name(modifyArticleMainProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }
}
