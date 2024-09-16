package springsideproject1.springsideproject1build.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1build.domain.entity.Press;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.repository.CompanyArticleRepository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Article.*;
import static springsideproject1.springsideproject1build.domain.vo.SchemaName.TEST_COMPANY_ARTICLES_SCHEMA;
import static springsideproject1.springsideproject1build.domain.vo.Word.NAME;

@Repository
public class CompanyArticleRepositoryImpl implements CompanyArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyArticleRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * SELECT CompanyArticle
     */
    @Override
    public List<CompanyArticle> getArticles() {
        return jdbcTemplate.query("select * from " + TEST_COMPANY_ARTICLES_SCHEMA, articleRowMapper());
    }

    @Override
    public List<CompanyArticle> getArticlesByDate(LocalDate date) {
        return jdbcTemplate.query("select * from " + TEST_COMPANY_ARTICLES_SCHEMA + " where date = ?", articleRowMapper(), date);
    }

    @Override
    public List<CompanyArticle> getArticlesByDate(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(
                "select * from " + TEST_COMPANY_ARTICLES_SCHEMA + " where date between ? and ?", articleRowMapper(), startDate, endDate);
    }

    @Override
    public List<CompanyArticle> getLatestArticles() {
        return jdbcTemplate.query("select * from " + TEST_COMPANY_ARTICLES_SCHEMA + " where date = " +
                "(select max(date) from " + TEST_COMPANY_ARTICLES_SCHEMA + ")", articleRowMapper());
    }

    @Override
    public Optional<CompanyArticle> getArticleByNumber(Long number) {
        List<CompanyArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_COMPANY_ARTICLES_SCHEMA + " where number = ?", articleRowMapper(), number);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<CompanyArticle> getArticleByName(String name) {
        List<CompanyArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_COMPANY_ARTICLES_SCHEMA + " where name = ?", articleRowMapper(), name);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<CompanyArticle> getArticleByLink(String link) {
        List<CompanyArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_COMPANY_ARTICLES_SCHEMA + " where link = ?", articleRowMapper(), link);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    /**
     * INSERT CompanyArticle
     */
    @Override
    public Long saveArticle(CompanyArticle article) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(TEST_COMPANY_ARTICLES_SCHEMA).usingGeneratedKeyColumns("number")
                .executeAndReturnKey(new MapSqlParameterSource(article.toMapWithNoNumber())).longValue();
    }

    /**
     * UPDATE CompanyArticle
     */
    @Override
    public void updateArticle(CompanyArticle article) {
        jdbcTemplate.update("update " + TEST_COMPANY_ARTICLES_SCHEMA +
                        " set press = ?, subjectCompany = ?, link = ?, date = ?, importance = ? where name = ?",
                article.getPress().name(), article.getSubjectCompany(), article.getLink(), article.getDate(), article.getImportance(), article.getName());
    }

    /**
     * REMOVE CompanyArticle
     */
    @Override
    public void deleteArticleByName(String name) {
        jdbcTemplate.update("delete from " + TEST_COMPANY_ARTICLES_SCHEMA + " where name = ?", name);
    }

    /**
     * Other private methods
     */
    private RowMapper<CompanyArticle> articleRowMapper() {
        return (resultSet, rowNumber) -> CompanyArticle.builder()
                        .number(resultSet.getLong(NUMBER))
                        .name(resultSet.getString(NAME))
                        .press(Press.valueOf(resultSet.getString(PRESS)))
                        .link(resultSet.getString(LINK))
                        .date(resultSet.getDate(DATE).toLocalDate())
                        .importance(resultSet.getInt(IMPORTANCE))
                        .subjectCompany(resultSet.getString(SUBJECT_COMPANY))
                        .build();
    }
}
