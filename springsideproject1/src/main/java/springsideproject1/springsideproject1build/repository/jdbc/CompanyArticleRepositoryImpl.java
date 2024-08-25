package springsideproject1.springsideproject1build.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.article.Press;
import springsideproject1.springsideproject1build.domain.repository.CompanyArticleRepository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

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
        return jdbcTemplate.query("select * from " + COMPANY_ARTICLE_TABLE, articleRowMapper());
    }

    @Override
    public List<CompanyArticle> getArticlesByDate(LocalDate date) {
        return jdbcTemplate.query("select * from " + COMPANY_ARTICLE_TABLE + " where date = ?", articleRowMapper(), date);
    }

    @Override
    public List<CompanyArticle> getArticlesByDate(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(
                "select * from " + COMPANY_ARTICLE_TABLE + " where date between ? and ?", articleRowMapper(), startDate, endDate);
    }

    @Override
    public Optional<CompanyArticle> getArticleByNumber(Long number) {
        List<CompanyArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + COMPANY_ARTICLE_TABLE + " where number = ?", articleRowMapper(), number);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<CompanyArticle> getArticleByName(String name) {
        List<CompanyArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + COMPANY_ARTICLE_TABLE + " where name = ?", articleRowMapper(), name);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<CompanyArticle> getArticleByLink(String link) {
        List<CompanyArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + COMPANY_ARTICLE_TABLE + " where link = ?", articleRowMapper(), link);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    /**
     * INSERT CompanyArticle
     */
    @Override
    public Long saveArticle(CompanyArticle article) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(COMPANY_ARTICLE_TABLE).usingGeneratedKeyColumns("number")
                .executeAndReturnKey(new MapSqlParameterSource(article.toMapWithNoNumber())).longValue();
    }

    /**
     * UPDATE CompanyArticle
     */
    @Override
    public void updateArticle(CompanyArticle article) {
        jdbcTemplate.update("update " + COMPANY_ARTICLE_TABLE +
                        " set press = ?, subjectCompany = ?, link = ?, date = ?, importance = ? where name = ?",
                article.getPress().name(), article.getSubjectCompany(), article.getLink(), article.getDate(), article.getImportance(), article.getName());
    }

    /**
     * REMOVE CompanyArticle
     */
    @Override
    public void deleteArticleByName(String name) {
        jdbcTemplate.update("delete from " + COMPANY_ARTICLE_TABLE + " where name = ?", name);
    }

    /**
     * Other private methods
     */
    private RowMapper<CompanyArticle> articleRowMapper() {
        return (resultSet, rowNumber) -> CompanyArticle.builder()
                        .number(resultSet.getLong("number"))
                        .name(resultSet.getString(NAME))
                        .press(Press.valueOf(resultSet.getString(PRESS)))
                        .subjectCompany(resultSet.getString(SUBJECT_COMPANY))
                        .link(resultSet.getString(LINK))
                        .date(resultSet.getDate(DATE).toLocalDate())
                        .importance(resultSet.getInt(IMPORTANCE))
                        .build();
    }
}
