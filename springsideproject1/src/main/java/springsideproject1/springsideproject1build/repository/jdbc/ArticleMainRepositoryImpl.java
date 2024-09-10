package springsideproject1.springsideproject1build.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.repository.ArticleMainRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_ARTICLE_MAIN_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Repository
public class ArticleMainRepositoryImpl implements ArticleMainRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ArticleMainRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * SELECT ArticleMain
     */
    @Override
    public List<ArticleMain> getArticles() {
        return jdbcTemplate.query("select * from " + TEST_ARTICLE_MAIN_TABLE, articleRowMapper());
    }

    @Override
    public Optional<ArticleMain> getArticleByNumber(Long number) {
        List<ArticleMain> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_ARTICLE_MAIN_TABLE + " where number = ?", articleRowMapper(), number);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<ArticleMain> getArticleByName(String name) {
        List<ArticleMain> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_ARTICLE_MAIN_TABLE + " where name = ?", articleRowMapper(), name);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<ArticleMain> getArticleByImagePath(String imagePath) {
        List<ArticleMain> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_ARTICLE_MAIN_TABLE + " where imagePath = ?", articleRowMapper(), imagePath);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    /**
     * INSERT ArticleMain
     */
    @Override
    public Long saveArticle(ArticleMain article) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(TEST_ARTICLE_MAIN_TABLE).usingGeneratedKeyColumns("number")
                .executeAndReturnKey(new MapSqlParameterSource(article.toMapWithNoNumber())).longValue();
    }

    /**
     * UPDATE ArticleMain
     */
    @Override
    public void updateArticle(ArticleMain article) {
        jdbcTemplate.update("update " + TEST_ARTICLE_MAIN_TABLE + " set imagePath = ?, summary = ? where name = ?",
                article.getImagePath(), article.getSummary(), article.getName());
    }

    /**
     * REMOVE ArticleMain
     */
    @Override
    public void deleteArticleByName(String name) {
        jdbcTemplate.update("delete from " + TEST_ARTICLE_MAIN_TABLE + " where name = ?", name);
    }

    /**
     * Other private methods
     */
    private RowMapper<ArticleMain> articleRowMapper() {
        return (resultSet, rowNumber) -> ArticleMain.builder()
                .number(resultSet.getLong(NUMBER))
                .name(resultSet.getString(NAME))
                .imagePath(resultSet.getString(IMAGE_PATH))
                .summary(resultSet.getString(SUMMARY))
                .articleClassName(resultSet.getString(ARTICLE_CLASS_NAME))
                .build();
    }
}
