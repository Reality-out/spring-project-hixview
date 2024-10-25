package site.hixview.repository.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import site.hixview.domain.entity.SubjectCountry;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.repository.EconomyArticleRepository;
import site.hixview.util.JsonUtils;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static site.hixview.domain.vo.Word.*;

@Repository
@Primary
public class EconomyArticleRepositoryImpl implements EconomyArticleRepository {

    private static final Logger log = LoggerFactory.getLogger(EconomyArticleRepositoryImpl.class);
    @Value("${schema.articles.economy}")
    private String CURRENT_SCHEMA;

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public EconomyArticleRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        objectMapper = new ObjectMapper();
    }

    /**
     * SELECT EconomyArticle
     */
    @Override
    public List<EconomyArticle> getArticles() {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA, articleRowMapper());
    }

    @Override
    public List<EconomyArticle> getArticlesByDate(LocalDate date) {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA + " where date = ?", articleRowMapper(), date);
    }

    @Override
    public List<EconomyArticle> getArticlesByDate(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where date between ? and ?", articleRowMapper(), startDate, endDate);
    }

    @Override
    public List<EconomyArticle> getLatestArticles() {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA + " where date = " +
                "(select max(date) from " + CURRENT_SCHEMA + ")", articleRowMapper());
    }

    @Override
    public List<EconomyArticle> getLatestDomesticArticles() {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA +
                " where subjectCountry = '" + SubjectCountry.SOUTH_KOREA.name() +
                "' and date = (select max(date) from " + CURRENT_SCHEMA + ")", articleRowMapper());
    }

    @Override
    public List<EconomyArticle> getLatestForeignArticles() {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA +
                " where subjectCountry != '" + SubjectCountry.SOUTH_KOREA.name() +
                "' and date = (select max(date) from " + CURRENT_SCHEMA + ")", articleRowMapper());
    }

    @Override
    public Optional<EconomyArticle> getArticleByNumber(Long number) {
        List<EconomyArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where number = ?", articleRowMapper(), number);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<EconomyArticle> getArticleByName(String name) {
        List<EconomyArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where name = ?", articleRowMapper(), name);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    @Override
    public Optional<EconomyArticle> getArticleByLink(String link) {
        List<EconomyArticle> oneArticleOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where link = ?", articleRowMapper(), link);
        return oneArticleOrNull.isEmpty() ? Optional.empty() : Optional.of(oneArticleOrNull.getFirst());
    }

    /**
     * INSERT EconomyArticle
     */
    @Override
    public Long saveArticle(EconomyArticle article) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(CURRENT_SCHEMA).usingGeneratedKeyColumns("number")
                .executeAndReturnKey(new MapSqlParameterSource(article.toSerializedMapWithNoNumber())).longValue();
    }

    /**
     * UPDATE EconomyArticle
     */
    @Override
    public void updateArticle(EconomyArticle article) {
        jdbcTemplate.update("update " + CURRENT_SCHEMA +
                        " set press = ?, link = ?, date = ?, importance = ?," +
                        " subjectCountry = ?, targetEconomyContents = ? where name = ?",
                article.getPress().name(), article.getLink(), article.getDate(), article.getImportance(),
                article.getSubjectCountry().name(), article.getSerializedTargetEconomyContents(), article.getName());
    }

    /**
     * REMOVE EconomyArticle
     */
    @Override
    public void deleteArticleByName(String name) {
        jdbcTemplate.update("delete from " + CURRENT_SCHEMA + " where name = ?", name);
    }

    /**
     * Other private methods
     */
    private RowMapper<EconomyArticle> articleRowMapper() {
        return (resultSet, rowNumber) -> {
            List<String> targetEconomyContents = JsonUtils.deserializeWithOneMapToList(objectMapper, TARGET_ECONOMY_CONTENT, resultSet.getString(TARGET_ECONOMY_CONTENTS));
            return EconomyArticle.builder()
                    .number(resultSet.getLong(NUMBER))
                    .name(resultSet.getString(NAME))
                    .press(Press.valueOf(resultSet.getString(PRESS)))
                    .link(resultSet.getString(LINK))
                    .date(resultSet.getDate(DATE).toLocalDate())
                    .importance(resultSet.getInt(IMPORTANCE))
                    .subjectCountry(SubjectCountry.valueOf(resultSet.getString(SUBJECT_COUNTRY)))
                    .targetEconomyContents(targetEconomyContents)
                    .build();
        };
    }
}
