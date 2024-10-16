package site.hixview.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.article.ArticleMain;
import site.hixview.domain.repository.ArticleMainRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.name.EntityName.Article.*;

@Repository
@Primary
public class ArticleMainRepositoryImpl implements ArticleMainRepository {

    @Value("${schema.article.mains}")
    private String CURRENT_SCHEMA;

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
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA, articleRowMapper());
    }

    @Override
    public Optional<ArticleMain> getArticleByNumber(Long number) {
        List<ArticleMain> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where number = ?", articleRowMapper(), number);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<ArticleMain> getArticleByName(String name) {
        List<ArticleMain> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where name = ?", articleRowMapper(), name);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<ArticleMain> getArticleByImagePath(String imagePath) {
        List<ArticleMain> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where imagePath = ?", articleRowMapper(), imagePath);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    /**
     * INSERT ArticleMain
     */
    @Override
    public Long saveArticle(ArticleMain article) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(CURRENT_SCHEMA).usingGeneratedKeyColumns("number")
                .executeAndReturnKey(new MapSqlParameterSource(article.toMapWithNoNumber())).longValue();
    }

    /**
     * UPDATE ArticleMain
     */
    @Override
    public void updateArticle(ArticleMain article) {
        jdbcTemplate.update("update " + CURRENT_SCHEMA +
                        " set imagePath = ?, summary = ?, classification = ? where name = ?",
                article.getImagePath(), article.getSummary(), article.getClassification().name(), article.getName());
    }

    /**
     * REMOVE ArticleMain
     */
    @Override
    public void deleteArticleByName(String name) {
        jdbcTemplate.update("delete from " + CURRENT_SCHEMA + " where name = ?", name);
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
                .classification(Classification.valueOf(resultSet.getString(CLASSIFICATION)))
                .build();
    }
}
