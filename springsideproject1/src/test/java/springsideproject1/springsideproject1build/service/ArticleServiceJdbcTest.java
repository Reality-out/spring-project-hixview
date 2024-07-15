package springsideproject1.springsideproject1build.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Article;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.Utility.*;

@SpringBootTest
@Transactional
class ArticleServiceJdbcTest {

    @Autowired
    ArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ArticleServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, articleTable);
    }

    @DisplayName("중복 기사 제목을 사용하는 기사 등록")
    @Test
    public void registerArticleWithSameName() {
        // given
        Article article1 = createTestArticle();
        Article article2 = createTestArticle();

        // when
        articleService.joinArticle(article1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.joinArticle(article2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 기사 제목입니다.");
    }

    @DisplayName("존재하지 않는 이름을 통한 기사 삭제")
    @Test
    public void removeArticleByFaultName() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.removeArticle("123456"));
        assertThat(e.getMessage()).isEqualTo("해당 제목과 일치하는 기사가 없습니다.");
    }
}