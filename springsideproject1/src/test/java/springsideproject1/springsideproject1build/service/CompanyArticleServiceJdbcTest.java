package springsideproject1.springsideproject1build.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.CompanyArticle;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.Utility.*;

@SpringBootTest
@Transactional
class CompanyArticleServiceJdbcTest {

    @Autowired
    CompanyArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleServiceJdbcTest(DataSource dataSource) {
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
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestArticle();

        // when
        articleService.joinArticle(article1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.joinArticle(article2));
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_ARTICLE_NAME);
    }

    @DisplayName("존재하지 않는 이름을 통한 기사 삭제")
    @Test
    public void removeArticleByFaultName() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> articleService.removeArticle("123456"));
        assertThat(e.getMessage()).isEqualTo(NO_ARTICLE_WITH_THAT_NAME);
    }
}