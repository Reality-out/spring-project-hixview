package site.hixview.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.repository.MemberRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static site.hixview.domain.vo.Word.*;

@Repository
@Primary
public class MemberRepositoryImpl implements MemberRepository {

    @Value("${schema.members}")
    private String CURRENT_SCHEMA;
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MemberRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * SELECT Member
     */
    @Override
    public List<Member> getMembers() {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA, memberRowMapper());
    }

    @Override
    public List<Member> getMembersByName(String name) {
        return jdbcTemplate.query("select * from " + CURRENT_SCHEMA + " where name = ?", memberRowMapper(), name);
    }

    @Override
    public Optional<Member> getMemberByIdentifier(Long identifier) {
        List<Member> oneMemberOrNull = jdbcTemplate.query("select * from " + CURRENT_SCHEMA + " where identifier = ?", memberRowMapper(), identifier);
        return oneMemberOrNull.isEmpty() ? Optional.empty() : Optional.of(oneMemberOrNull.getFirst());
    }

    @Override
    public Optional<Member> getMemberByID(String id) {
        List<Member> oneMemberOrNull = jdbcTemplate.query("select * from " + CURRENT_SCHEMA + " where id = ?", memberRowMapper(), id);
        return oneMemberOrNull.isEmpty() ? Optional.empty() : Optional.of(oneMemberOrNull.getFirst());
    }

    @Override
    public Optional<Member> getMemberByEmail(String email) {
        List<Member> oneMemberOrNull = jdbcTemplate.query("select * from " + CURRENT_SCHEMA + " where email = ?", memberRowMapper(), email);
        return oneMemberOrNull.isEmpty() ? Optional.empty() : Optional.of(oneMemberOrNull.getFirst());
    }

    /**
     * INSERT Member
     */
    @Override
    public Long saveMember(Member member) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(CURRENT_SCHEMA).usingGeneratedKeyColumns(IDENTIFIER)
                .executeAndReturnKey(new MapSqlParameterSource(member.toMapWithNoIdentifier())).longValue();
    }

    /**
     * REMOVE Member
     */
    @Override
    public void deleteMemberById(String id) {
        jdbcTemplate.execute("delete from " + CURRENT_SCHEMA + " where id = '" + id + "'");
    }

    /**
     * Other private methods
     */
    private RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNumber) -> Member.builder()
                        .identifier(resultSet.getLong(IDENTIFIER))
                        .id(resultSet.getString(ID))
                        .password(resultSet.getString(PASSWORD))
                        .name(resultSet.getString(NAME))
                        .email(resultSet.getString(EMAIL))
                        .build();
    }
}
