package springsideproject1.springsideproject1build.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.member.Member;
import springsideproject1.springsideproject1build.utility.test.MemberTestUtility;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.error.constant.EXCEPTION_MESSAGE.ALREADY_EXIST_MEMBER_ID;
import static springsideproject1.springsideproject1build.error.constant.EXCEPTION_MESSAGE.NO_MEMBER_WITH_THAT_ID;

@SpringBootTest
@Transactional
class MemberServiceJdbcTest implements MemberTestUtility {

    @Autowired
    MemberService memberService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public MemberServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, memberTable, true);
    }

    @DisplayName("회원 가입")
    @Test
    public void membership() {
        // given
        Member member = createTestMember();

        // when
        member = memberService.registerMember(member);

        // then
        assertThat(memberService.findMembers().getFirst()).usingRecursiveComparison().isEqualTo(member);
    }

    @DisplayName("회원 중복 ID로 가입")
    @Test
    public void duplicatedMembershipWithSameID() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestMember();

        // when
        memberService.registerMember(member1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.registerMember(member2));
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_MEMBER_ID);
    }

    @DisplayName("회원 존재하지 않는 ID로 제거")
    @Test
    public void removeMemberByFaultID() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.removeMember("NoneID123"));
        assertThat(e.getMessage()).isEqualTo(NO_MEMBER_WITH_THAT_ID);
    }
}