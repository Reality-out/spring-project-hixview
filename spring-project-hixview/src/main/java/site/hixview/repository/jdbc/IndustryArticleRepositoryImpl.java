package site.hixview.repository.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.repository.IndustryArticleRepository;
import site.hixview.util.JsonUtils;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.name.EntityName.Article.*;

@Repository
@Primary
public class IndustryArticleRepositoryImpl implements IndustryArticleRepository {

    @Value("${schema.article.industries}")
    private String CURRENT_SCHEMA;

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public IndustryArticleRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        objectMapper = new ObjectMapper();
    }

    /**
     * SELECT IndustryArticle
     */
    @Override
    public List<IndustryArticle> getArticles() {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA, articleRowMapper());
    }

    @Override
    public List<IndustryArticle> getArticlesByDate(LocalDate date) {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA + " where date = ?", articleRowMapper(), date);
    }

    @Override
    public List<IndustryArticle> getArticlesByDate(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where date between ? and ?", articleRowMapper(), startDate, endDate);
    }

    @Override
    public List<IndustryArticle> getLatestArticles() {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA + " where date = " +
                "(select max(date) from " + CURRENT_SCHEMA + ")", articleRowMapper());
    }

    @Override
    public Optional<IndustryArticle> getArticleByNumber(Long number) {
        List<IndustryArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where number = ?", articleRowMapper(), number);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<IndustryArticle> getArticleByName(String name) {
        List<IndustryArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where name = ?", articleRowMapper(), name);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<IndustryArticle> getArticleByLink(String link) {
        List<IndustryArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where link = ?", articleRowMapper(), link);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    /**
     * INSERT IndustryArticle
     */
    @Override
    public Long saveArticle(IndustryArticle article) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(CURRENT_SCHEMA).usingGeneratedKeyColumns("number")
                .executeAndReturnKey(new MapSqlParameterSource(article.toSerializedMapWithNoNumber())).longValue();
    }

    /**
     * UPDATE IndustryArticle
     */
    @Override
    public void updateArticle(IndustryArticle article) {
        jdbcTemplate.update("update " + CURRENT_SCHEMA +
                        " set press = ?, link = ?, date = ?, importance = ?," +
                        " subjectFirstCategory = ?, subjectSecondCategories = ? where name = ?",
                article.getPress().name(), article.getLink(), article.getDate(), article.getImportance(),
                article.getSubjectFirstCategory().name(), article.getSerializedSubjectSecondCategories(), article.getName());
    }

    /**
     * REMOVE IndustryArticle
     */
    @Override
    public void deleteArticleByName(String name) {
        jdbcTemplate.update("delete from " + CURRENT_SCHEMA + " where name = ?", name);
    }

    /**
     * Other private methods
     */
    private RowMapper<IndustryArticle> articleRowMapper() {
        return (resultSet, rowNumber) -> {
            List<SecondCategory> subjectSecondCategories = JsonUtils.deserializeEnumWithOneMapToList(objectMapper, SUBJECT_SECOND_CATEGORY, resultSet.getString(SUBJECT_SECOND_CATEGORIES), SecondCategory.class);
            return IndustryArticle.builder()
                    .number(resultSet.getLong(NUMBER))
                    .name(resultSet.getString(NAME))
                    .press(Press.valueOf(resultSet.getString(PRESS)))
                    .link(resultSet.getString(LINK))
                    .date(resultSet.getDate(DATE).toLocalDate())
                    .importance(resultSet.getInt(IMPORTANCE))
                    .subjectFirstCategory(FirstCategory.valueOf(resultSet.getString(SUBJECT_FIRST_CATEGORY)))
                    .subjectSecondCategories(subjectSecondCategories)
                    .build();
        };
    }
}
