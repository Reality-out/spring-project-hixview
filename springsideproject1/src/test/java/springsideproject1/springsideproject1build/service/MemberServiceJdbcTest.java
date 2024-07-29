package springsideproject1.springsideproject1build.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static springsideproject1.springsideproject1build.Utility.*;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.ALREADY_EXIST_MEMBER_ID;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_MEMBER_WITH_THAT_ID;

@SpringBootTest
@Transactional
class MemberServiceJdbcTest {

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
        member = memberService.joinMember(member);

        // then
        assertThat(memberService.findMembers().getFirst()).usingRecursiveComparison().isEqualTo(member);
    }

    @DisplayName("중복 ID를 사용하는 회원가입")
    @Test
    public void membershipWithSameID() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestMember();

        // when
        memberService.joinMember(member1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.joinMember(member2));
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_MEMBER_ID);
    }

    @DisplayName("잘못된 ID를 통한 회원 삭제")
    @Test
    public void removeByFaultID() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.removeMember("NoneID123"));
        assertThat(e.getMessage()).isEqualTo(NO_MEMBER_WITH_THAT_ID);
    }
}