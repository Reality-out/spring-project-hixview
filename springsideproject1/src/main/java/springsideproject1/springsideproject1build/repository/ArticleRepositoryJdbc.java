package springsideproject1.springsideproject1build.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1build.domain.Article;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.Utility.articleRowMapper;
import static springsideproject1.springsideproject1build.Utility.articleTable;

@Repository
public class ArticleRepositoryJdbc implements ArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ArticleRepositoryJdbc(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Article> findAllArticles() {
        return jdbcTemplate.query("select * from " + articleTable, articleRowMapper());
    }

    @Override
    public Optional<Article> searchArticleByName(String name) {
        List<Article> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + articleTable + " where name = ?", articleRowMapper(), name);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public List<Article> searchArticlesByDate(LocalDate date) {
        return jdbcTemplate.query("select * from " + articleTable + " where date = ?", articleRowMapper(), date);
    }

    @Override
    public List<Article> searchArticlesByDate(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(
                "select * from " + articleTable + " where date between ? and ?", articleRowMapper(), startDate, endDate);
    }

    @Override
    public Long saveOneArticle(Article article) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(articleTable).usingGeneratedKeyColumns("number");
        Number articleKey = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(article.toMap()));

        return articleKey.longValue();
    }

    @Override
    public void removeArticleByName(String name) {
        jdbcTemplate.update("delete from " + articleTable + " where name = ?", name);
    }
}
