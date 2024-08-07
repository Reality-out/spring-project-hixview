package springsideproject1.springsideproject1build.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.member.Member;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.utility.ConstantUtility.ID;
import static springsideproject1.springsideproject1build.utility.ConstantUtility.NAME;
import static springsideproject1.springsideproject1build.utility.test.MemberTestUtility.memberTable;

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
    public List<Member> getMembers() {
        return jdbcTemplate.query("select * from " + memberTable, memberRowMapper());
    }

    @Override
    public List<Member> getMembersByName(String name) {
        return jdbcTemplate.query("select * from " + memberTable + " where name = ?", memberRowMapper(), name);
    }

    @Override
    public List<Member> getMembersByBirth(LocalDate birth) {
        return jdbcTemplate.query("select * from " + memberTable + " where birth = ?", memberRowMapper(), birth);
    }

    @Override
    public List<Member> getMembersByNameAndBirth(String name, LocalDate birth) {
        return jdbcTemplate.query("select * from " + memberTable + " where name = ? and birth = ?", memberRowMapper(), name, birth);
    }

    @Override
    public Optional<Member> getMemberByIdentifier(Long identifier) {
        List<Member> oneMemberOrNull = jdbcTemplate.query("select * from " + memberTable + " where identifier = ?", memberRowMapper(), identifier);
        return oneMemberOrNull.isEmpty() ? Optional.empty() : Optional.of(oneMemberOrNull.getFirst());
    }

    @Override
    public Optional<Member> getMemberByID(String id) {
        List<Member> oneMemberOrNull = jdbcTemplate.query("select * from " + memberTable + " where id = ?", memberRowMapper(), id);
        return oneMemberOrNull.isEmpty() ? Optional.empty() : Optional.of(oneMemberOrNull.getFirst());
    }

    /**
     * INSERT Member
     */
    @Override
    @Transactional
    public Long saveMember(Member member) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName(memberTable).usingGeneratedKeyColumns("identifier")
                .executeAndReturnKey(new MapSqlParameterSource(member.toMapWithNoIdentifier())).longValue();
    }

    /**
     * REMOVE Member
     */
    @Override
    @Transactional
    public void deleteMember(String id) {
        jdbcTemplate.execute("delete from " + memberTable + " where id = '" + id + "'");
    }

    /**
     * Other private methods
     */
    private RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNumber) -> Member.builder()
                        .identifier(resultSet.getLong("identifier"))
                        .id(resultSet.getString(ID))
                        .password(resultSet.getString("password"))
                        .name(resultSet.getString(NAME))
                        .birth(resultSet.getDate("birth").toLocalDate())
                        .phoneNumber(resultSet.getString("phoneNumber"))
                        .build();
    }
}
