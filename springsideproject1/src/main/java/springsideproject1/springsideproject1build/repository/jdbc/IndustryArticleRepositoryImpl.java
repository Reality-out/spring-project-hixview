package springsideproject1.springsideproject1build.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1build.domain.entity.FirstCategory;
import springsideproject1.springsideproject1build.domain.entity.Press;
import springsideproject1.springsideproject1build.domain.entity.SecondCategory;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.repository.IndustryArticleRepository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Article.*;
import static springsideproject1.springsideproject1build.domain.vo.SchemaName.TEST_INDUSTRY_ARTICLES_SCHEMA;
import static springsideproject1.springsideproject1build.domain.vo.Word.NAME;

@Repository
public class IndustryArticleRepositoryImpl implements IndustryArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IndustryArticleRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * SELECT IndustryArticle
     */
    @Override
    public List<IndustryArticle> getArticles() {
        return jdbcTemplate.query("select * from " + TEST_INDUSTRY_ARTICLES_SCHEMA, articleRowMapper());
    }

    @Override
    public List<IndustryArticle> getArticlesByDate(LocalDate date) {
        return jdbcTemplate.query("select * from " + TEST_INDUSTRY_ARTICLES_SCHEMA + " where date = ?", articleRowMapper(), date);
    }

    @Override
    public List<IndustryArticle> getArticlesByDate(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(
                "select * from " + TEST_INDUSTRY_ARTICLES_SCHEMA + " where date between ? and ?", articleRowMapper(), startDate, endDate);
    }

    @Override
    public List<IndustryArticle> getLatestArticles() {
        return jdbcTemplate.query("select * from " + TEST_INDUSTRY_ARTICLES_SCHEMA + " where date = " +
                "(select max(date) from " + TEST_INDUSTRY_ARTICLES_SCHEMA + ")", articleRowMapper());
    }

    @Override
    public Optional<IndustryArticle> getArticleByNumber(Long number) {
        List<IndustryArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_INDUSTRY_ARTICLES_SCHEMA + " where number = ?", articleRowMapper(), number);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<IndustryArticle> getArticleByName(String name) {
        List<IndustryArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_INDUSTRY_ARTICLES_SCHEMA + " where name = ?", articleRowMapper(), name);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<IndustryArticle> getArticleByLink(String link) {
        List<IndustryArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_INDUSTRY_ARTICLES_SCHEMA + " where link = ?", articleRowMapper(), link);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    /**
     * INSERT IndustryArticle
     */
    @Override
    public Long saveArticle(IndustryArticle article) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(TEST_INDUSTRY_ARTICLES_SCHEMA).usingGeneratedKeyColumns("number")
                .executeAndReturnKey(new MapSqlParameterSource(article.toMapWithNoNumber())).longValue();
    }

    /**
     * UPDATE IndustryArticle
     */
    @Override
    public void updateArticle(IndustryArticle article) {
        jdbcTemplate.update("update " + TEST_INDUSTRY_ARTICLES_SCHEMA +
                        " set press = ?, link = ?, date = ?, importance = ?," +
                        " subjectFirstCategory = ?, subjectSecondCategory = ? where name = ?",
                article.getPress().name(), article.getLink(), article.getDate(), article.getImportance(),
                article.getSubjectFirstCategory().name(), article.getSubjectSecondCategory().name(), article.getName());
    }

    /**
     * REMOVE IndustryArticle
     */
    @Override
    public void deleteArticleByName(String name) {
        jdbcTemplate.update("delete from " + TEST_INDUSTRY_ARTICLES_SCHEMA + " where name = ?", name);
    }

    /**
     * Other private methods
     */
    private RowMapper<IndustryArticle> articleRowMapper() {
        return (resultSet, rowNumber) -> IndustryArticle.builder()
                .number(resultSet.getLong(NUMBER))
                .name(resultSet.getString(NAME))
                .press(Press.valueOf(resultSet.getString(PRESS)))
                .link(resultSet.getString(LINK))
                .date(resultSet.getDate(DATE).toLocalDate())
                .importance(resultSet.getInt(IMPORTANCE))
                .subjectFirstCategory(FirstCategory.valueOf(resultSet.getString(SUBJECT_FIRST_CATEGORY)))
                .subjectSecondCategory(SecondCategory.valueOf(resultSet.getString(SUBJECT_SECOND_CATEGORY)))
                .build();
    }
}
