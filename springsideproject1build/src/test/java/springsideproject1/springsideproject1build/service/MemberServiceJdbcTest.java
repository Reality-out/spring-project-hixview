package springsideproject1.springsideproject1build.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.repository.MemberRepository;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Component
@TestMethodOrder(MethodOrderer.DisplayName.class)
class MemberServiceJdbcTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public MemberServiceJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable("testmembers");
    }

    private void resetTable(String tableName) {
        jdbcTemplateTest.execute("DELETE FROM " + tableName);
        jdbcTemplateTest.execute("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1");
    }

    @DisplayName("회원 가입 테스트")
    @Test
    public void membership() {
        // given
        Member member = new Member();
        member.setId("ABcd1234!");
        member.setPassword("EFgh1234!");
        member.setName("박진하");

        // when
        memberService.joinMember(member);

        // then
        assertThat(memberService.findMembers().getFirst()).usingRecursiveComparison().isEqualTo(member);
    }

    @DisplayName("중복 ID 가입 테스트")
    @Test
    public void membershipWithSameID() {
        // given
        Member member1 = new Member();
        member1.setId("ABcd1234!");
        member1.setPassword("EFgh1234!");
        member1.setName("박진하");

        Member member2 = new Member();
        member2.setId("ABcd1234!");
        member2.setPassword("EFgh1234!");
        member2.setName("박하진");

        // when
        memberService.joinMember(member1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.joinMember(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 ID입니다.");
    }

    @DisplayName("잘못된 ID를 통한 회원 삭제")
    @Test
    public void removeByFaultID() {
        // then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.removeMember("NoneID123"));
        assertThat(e.getMessage()).isEqualTo("해당 ID와 일치하는 멤버가 없습니다.");
    }
}