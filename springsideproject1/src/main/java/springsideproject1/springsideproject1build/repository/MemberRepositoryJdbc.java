package springsideproject1.springsideproject1build.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.utility.test.MemberTest.memberTable;

@Repository
public class MemberRepositoryJdbc implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MemberRepositoryJdbc(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * SELECT Member
     */
    @Override
    public List<Member> findAllMembers() {
        return jdbcTemplate.query("select * from " + memberTable, memberRowMapper());
    }

    @Override
    public Optional<Member> findMemberByIdentifier(Long identifier) {
        List<Member> oneMemberOrNull = jdbcTemplate.query("select * from " + memberTable + " where identifier = ?", memberRowMapper(), identifier);
        return oneMemberOrNull.isEmpty() ? Optional.empty() : Optional.of(oneMemberOrNull.getFirst());
    }

    @Override
    public Optional<Member> findMemberByID(String id) {
        List<Member> oneMemberOrNull = jdbcTemplate.query("select * from " + memberTable + " where id = ?", memberRowMapper(), id);
        return oneMemberOrNull.isEmpty() ? Optional.empty() : Optional.of(oneMemberOrNull.getFirst());
    }

    @Override
    public List<Member> findMemberByName(String name) {
        return jdbcTemplate.query("select * from " + memberTable + " where name = ?", memberRowMapper(), name);
    }

    /**
     * INSERT Member
     */
    @Override
    @Transactional
    public Long saveMember(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(memberTable).usingGeneratedKeyColumns("identifier");
        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(member.toMapWithNoIdentifier())).longValue();
    }

    /**
     * REMOVE Member
     */
    @Override
    @Transactional
    public void removeMemberByID(String id) {
        jdbcTemplate.execute("delete from " + memberTable + " where id = '" + id + "'");
    }

    /**
     * Other private methods
     */
    private RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNumber) -> Member.builder()
                        .identifier(resultSet.getLong("identifier"))
                        .id(resultSet.getString("id"))
                        .password(resultSet.getString("password"))
                        .name(resultSet.getString("name"))
                        .birth(resultSet.getDate("birth").toLocalDate())
                        .phoneNumber(resultSet.getString("phoneNumber"))
                        .build();
    }
}
