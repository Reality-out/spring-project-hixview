package springsideproject1.springsideproject1build.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.member.Member;
import springsideproject1.springsideproject1build.util.test.MemberTestUtils;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.domain.vo.EXCEPTION_MESSAGE.ALREADY_EXIST_MEMBER_ID;
import static springsideproject1.springsideproject1build.domain.vo.EXCEPTION_MESSAGE.NO_MEMBER_WITH_THAT_ID;
import static springsideproject1.springsideproject1build.domain.vo.DATABASE.TEST_MEMBER_TABLE;

@SpringBootTest
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
        resetTable(jdbcTemplateTest, TEST_MEMBER_TABLE, true);
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

    @DisplayName("회원 존재하지 않는 ID로 제거")
    @Test
    public void removeMemberByFaultIDTest() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.removeMember("NoneID123"));
        assertThat(e.getMessage()).isEqualTo(NO_MEMBER_WITH_THAT_ID);
    }
}