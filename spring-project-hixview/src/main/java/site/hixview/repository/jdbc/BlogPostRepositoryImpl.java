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
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.repository.BlogPostRepository;
import site.hixview.util.JsonUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static site.hixview.domain.vo.Word.*;

@Repository
@Primary
public class BlogPostRepositoryImpl implements BlogPostRepository {

    @Value("${schema.posts.blog}")
    private String CURRENT_SCHEMA;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BlogPostRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * SELECT BlogPost
     */
    @Override
    public List<BlogPost> getPosts() {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA, postRowMapper());
    }

    @Override
    public List<BlogPost> getLatestPosts(Classification classification) {
        return jdbcTemplate.query("with filtered_blog_posts as (select * from " + CURRENT_SCHEMA +
                " where classification = ?) select * from filtered_blog_posts where date =" +
                " (select max(date) from filtered_blog_posts)", postRowMapper(), classification.name());
    }

    @Override
    public Optional<BlogPost> getPostByNumber(Long number) {
        List<BlogPost> onePostOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where number = ?", postRowMapper(), number);
        return onePostOrNull.isEmpty() ? Optional.empty() : Optional.of(onePostOrNull.getFirst());
    }

    @Override
    public Optional<BlogPost> getPostByName(String name) {
        List<BlogPost> onePostOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where name = ?", postRowMapper(), name);
        return onePostOrNull.isEmpty() ? Optional.empty() : Optional.of(onePostOrNull.getFirst());
    }

    @Override
    public Optional<BlogPost> getPostByLink(String link) {
        List<BlogPost> onePostOrNull = jdbcTemplate.query(
                "select * from " + CURRENT_SCHEMA + " where link = ?", postRowMapper(), link);
        return onePostOrNull.isEmpty() ? Optional.empty() : Optional.of(onePostOrNull.getFirst());
    }

    /**
     * INSERT BlogPost
     */
    @Override
    public Long savePost(BlogPost post) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(CURRENT_SCHEMA).usingGeneratedKeyColumns("number")
                .executeAndReturnKey(new MapSqlParameterSource(post.toSerializedMapWithNoNumber())).longValue();
    }

    /**
     * UPDATE BlogPost
     */
    @Override
    public void updatePost(BlogPost post) {
        jdbcTemplate.update("update " + CURRENT_SCHEMA + " set date = ?, classification = ?, targetName = ?, " +
                        "targetImagePath = ?, targetArticleNames = ?, targetArticleLinks = ? where name = ?",
                post.getDate(), post.getClassification().name(), post.getTargetName(), post.getTargetImagePath(),
                post.getSerializedTargetArticleNames(), post.getSerializedTargetArticleLinks(), post.getName());
    }

    /**
     * REMOVE BlogPost
     */
    @Override
    public void deletePostByName(String name) {
        jdbcTemplate.update("delete from " + CURRENT_SCHEMA + " where name = ?", name);
    }

    /**
     * Other private methods
     */
    private RowMapper<BlogPost> postRowMapper() {
        return (resultSet, rowNumber) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> targetArticleNames = JsonUtils.deserializeWithOneMapToList(objectMapper, TARGET_ARTICLE_NAME, resultSet.getString(TARGET_ARTICLE_NAMES));
            List<String> targetArticleLinks = JsonUtils.deserializeWithOneMapToList(objectMapper, TARGET_ARTICLE_LINK, resultSet.getString(TARGET_ARTICLE_LINKS));
            return BlogPost.builder()
                    .number(resultSet.getLong(NUMBER))
                    .name(resultSet.getString(NAME))
                    .link(resultSet.getString(LINK))
                    .date(resultSet.getDate(DATE).toLocalDate())
                    .classification(Classification.valueOf(resultSet.getString(CLASSIFICATION)))
                    .targetName(resultSet.getString(TARGET_NAME))
                    .targetImagePath(resultSet.getString(TARGET_IMAGE_PATH))
                    .targetArticleNames(targetArticleNames)
                    .targetArticleLinks(targetArticleLinks)
                    .build();
        };
    }
}