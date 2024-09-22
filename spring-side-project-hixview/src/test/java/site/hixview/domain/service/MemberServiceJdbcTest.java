package site.hixview.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.member.Member;
import site.hixview.util.test.MemberTestUtils;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_MEMBER_ID;
import static site.hixview.domain.vo.ExceptionMessage.NO_MEMBER_WITH_THAT_ID;
import static site.hixview.domain.vo.name.SchemaName.TEST_MEMBERS_SCHEMA;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@Transactional
class MemberServiceJdbcTest implements MemberTestUtils {

    @Autowired
    MemberService memberService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public MemberServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_MEMBERS_SCHEMA, true);
    }

    @DisplayName("회원 가입")
    @Test
    public void membership() {
        // given
        Member member = testMember;

        // when
        member = memberService.registerMember(member);

        // then
        assertThat(memberService.findMembers().getFirst()).usingRecursiveComparison().isEqualTo(member);
    }

    @DisplayName("회원 중복 ID로 가입")
    @Test
    public void duplicatedMembershipWithSameIDTest() {
        // given & when
        memberService.registerMember(testMember);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.registerMember(testMember));
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_MEMBER_ID);
    }

    @DisplayName("회원 탈퇴")
    @Test
    public void withdrawMembership() {
        // given
        memberService.registerMember(testMember);

        // when
        memberService.removeMemberById(testMember.getId());

        // then
        assertThat(memberService.findMembers()).isEmpty();
    }

    @DisplayName("회원 존재하지 않는 ID로 제거")
    @Test
    public void removeMemberByFaultIDTest() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.removeMemberById(INVALID_VALUE));
        assertThat(e.getMessage()).isEqualTo(NO_MEMBER_WITH_THAT_ID);
    }
}