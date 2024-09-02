package springsideproject1.springsideproject1build.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleMain;
import springsideproject1.springsideproject1build.domain.repository.CompanyArticleMainRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_COMPANY_ARTICLE_MAIN_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Repository
public class CompanyArticleMainRepositoryImpl implements CompanyArticleMainRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyArticleMainRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * SELECT CompanyArticleMain
     */
    @Override
    public List<CompanyArticleMain> getArticles() {
        return jdbcTemplate.query("select * from " + TEST_COMPANY_ARTICLE_MAIN_TABLE, articleRowMapper());
    }

    @Override
    public Optional<CompanyArticleMain> getArticleByNumber(Long number) {
        List<CompanyArticleMain> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_COMPANY_ARTICLE_MAIN_TABLE + " where number = ?", articleRowMapper(), number);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<CompanyArticleMain> getArticleByName(String name) {
        List<CompanyArticleMain> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + TEST_COMPANY_ARTICLE_MAIN_TABLE + " where name = ?", articleRowMapper(), name);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    /**
     * INSERT CompanyArticleMain
     */
    @Override
    public Long saveArticle(CompanyArticleMain article) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(TEST_COMPANY_ARTICLE_MAIN_TABLE).usingGeneratedKeyColumns("number")
                .executeAndReturnKey(new MapSqlParameterSource(article.toMapWithNoNumber())).longValue();
    }

    /**
     * UPDATE CompanyArticleMain
     */
    @Override
    public void updateArticle(CompanyArticleMain article) {
        jdbcTemplate.update("update " + TEST_COMPANY_ARTICLE_MAIN_TABLE + " set imagePath = ?, summary = ? where name = ?",
                article.getImagePath(), article.getSummary(), article.getName());
    }

    /**
     * REMOVE CompanyArticleMain
     */
    @Override
    public void deleteArticleByName(String name) {
        jdbcTemplate.update("delete from " + TEST_COMPANY_ARTICLE_MAIN_TABLE + " where name = ?", name);
    }

    /**
     * Other private methods
     */
    private RowMapper<CompanyArticleMain> articleRowMapper() {
        return (resultSet, rowNumber) -> CompanyArticleMain.builder()
                .number(resultSet.getLong(NUMBER))
                .name(resultSet.getString(NAME))
                .imagePath(resultSet.getString(IMAGE_PATH))
                .summary(resultSet.getString(SUMMARY))
                .build();
    }
}
