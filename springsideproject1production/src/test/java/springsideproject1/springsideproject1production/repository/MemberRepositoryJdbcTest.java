package springsideproject1.springsideproject1production.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1production.domain.Member;
import springsideproject1.springsideproject1production.service.MemberService;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Component
@TestMethodOrder(MethodOrderer.DisplayName.class)
class MemberRepositoryJdbcTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public MemberRepositoryJdbcTest(DataSource dataSource) {
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

    @DisplayName("멤버 모두 가져오기")
    @Test
    public void findMembers() {
        // given
        Member member1 = new Member();
        member1.setId("parkjunhyeok");
        member1.setPassword("junhyeokpark");
        member1.setName("박준혁");

        Member member2 = new Member();
        member2.setId("parkhajin");
        member2.setPassword("hajinpark");
        member2.setName("박하진");

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findAllMembers()).usingRecursiveComparison().isEqualTo(List.of(member1, member2));
    }

    @DisplayName("멤버 식별자로 찾기")
    @Test
    public void findByIdentifier() {
        // given
        Member member1 = new Member();
        member1.setId("parkjunhyeok");
        member1.setPassword("junhyeokpark");
        member1.setName("박준혁");

        Member member2 = new Member();
        member2.setId("parkhajin");
        member2.setPassword("hajinpark");
        member2.setName("박하진");

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findMemberByIdentifier(Long.valueOf(1L)).get()).usingRecursiveComparison().isEqualTo(member1);
        assertThat(memberRepository.findMemberByIdentifier(Long.valueOf(2L)).get()).usingRecursiveComparison().isEqualTo(member2);
    }

    @DisplayName("멤버 ID로 찾기")
    @Test
    public void findByID() {
        // given
        Member member1 = new Member();
        member1.setId("Wehado!");
        member1.setPassword("junhyeokpark");
        member1.setName("박준혁");

        Member member2 = new Member();
        member2.setId("Beautiful16");
        member2.setPassword("hajinpark");
        member2.setName("박하진");

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findMemberByID("Wehado!").get()).usingRecursiveComparison().isEqualTo(member1);
        assertThat(memberRepository.findMemberByID("Beautiful16").get()).usingRecursiveComparison().isEqualTo(member2);
    }

    @DisplayName("멤버 이름으로 찾기")
    @Test
    public void findByName() {
        // given
        Member member1 = new Member();
        member1.setId("Rarafsd12");
        member1.setPassword("1tangkwa!");
        member1.setName("박준호");

        Member member2 = new Member();
        member2.setId("parkhajin");
        member2.setPassword("hajinpark");
        member2.setName("박하진");

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findMemberByName("박준호").getFirst()).usingRecursiveComparison().isEqualTo(member1);
        assertThat(memberRepository.findMemberByName("박하진").getFirst()).usingRecursiveComparison().isEqualTo(member2);
    }

    @DisplayName("이름이 중복되는 멤버 모두 찾기")
    @Test
    public void findAllByName() {
        // given
        Member member1 = new Member();
        member1.setId("HyeSung596");
        member1.setPassword("gamma137!");
        member1.setName("박준호");

        Member member2 = new Member();
        member2.setId("parkjunho");
        member2.setPassword("junhopark");
        member2.setName("박준호");

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findMemberByName("박준호")).usingRecursiveComparison().isEqualTo(List.of(member1, member2));
    }

    @DisplayName("메모리 저장 테스트")
    @Test
    public void save() {
        // given
        Member member = new Member();
        member.setIdentifier(Long.valueOf(1));
        member.setId("Ranger37");
        member.setPassword("12satellight!");
        member.setName("박구준");

        // when
        memberRepository.saveMember(member);

        // then
        assertThat(memberRepository.findMemberByName(member.getName()).getFirst())
                .usingRecursiveComparison().isEqualTo(member);
    }

    @DisplayName("멤버 ID로 제거하기")
    @Test
    public void removeByID() {
        // given
        Member member1 = new Member();
        member1.setId("ParkWnsGur12");
        member1.setPassword("junhyeokpark");
        member1.setName("박준혁");

        Member member2 = new Member();
        member2.setId("ParkHaJin34");
        member2.setPassword("hajinpark");
        member2.setName("박하진");

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        memberRepository.removeMemberByID("ParkWnsGur12");
        memberRepository.removeMemberByID("ParkHaJin34");
        assertThat(memberRepository.findAllMembers()).isEmpty();
    }
}