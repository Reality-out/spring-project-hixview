package springsideproject1.springsideproject1build.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static springsideproject1.springsideproject1build.Utility.memberRowMapper;

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
        return jdbcTemplate.query("select * from testmembers", memberRowMapper());
    }

    @Override
    public Optional<Member> findMemberByIdentifier(Long identifier) {
        List<Member> oneMemberOrNull = jdbcTemplate.query("select * from testmembers where identifier = ?", memberRowMapper(), identifier);
        return oneMemberOrNull.isEmpty() ? Optional.empty() : Optional.of(oneMemberOrNull.getFirst());
    }

    @Override
    public Optional<Member> findMemberByID(String id) {
        List<Member> oneMemberOrNull = jdbcTemplate.query("select * from testmembers where id = ?", memberRowMapper(), id);
        return oneMemberOrNull.isEmpty() ? Optional.empty() : Optional.of(oneMemberOrNull.getFirst());
    }

    @Override
    public List<Member> findMemberByName(String name) {
        return jdbcTemplate.query("select * from testmembers where name = ?", memberRowMapper(), name);
    }

    /**
     * INSERT Member
     */
    @Override
    @Transactional
    public Long saveMember(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("testmembers").usingGeneratedKeyColumns("identifier");

        Map<String, String> insertParam = new HashMap<>() {{
            put("ID", member.getId());
            put("password", member.getPassword());
            put("name", member.getName());
        }};

        Number memberKey = jdbcInsert.executeAndReturnKey(
                new MapSqlParameterSource(insertParam));

        return memberKey.longValue();
    }

    /**
     * REMOVE Member
     */
    @Override
    @Transactional
    public void removeMemberByID(String id) {
        jdbcTemplate.execute("delete from testmembers where id = '" + id + "'");
    }
}
