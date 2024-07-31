package springsideproject1.springsideproject1build.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.utility.test.MemberTest;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryJdbcTest implements MemberTest {

    @Autowired
    MemberRepository memberRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public MemberRepositoryJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, memberTable, true);
    }

    @DisplayName("멤버 모두 가져오기")
    @Test
    public void findAll() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findAllMembers())
                .usingRecursiveComparison()
                .ignoringFields("identifier")
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("멤버 식별자로 찾기")
    @Test
    public void findByIdentifier() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findMemberByIdentifier(1L).get())
                .usingRecursiveComparison()
                .ignoringFields("identifier")
                .isEqualTo(member1);
        assertThat(memberRepository.findMemberByIdentifier(2L).get())
                .usingRecursiveComparison()
                .ignoringFields("identifier")
                .isEqualTo(member2);
    }

    @DisplayName("멤버 ID로 찾기")
    @Test
    public void findByID() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findMemberByID(member1.getId()).get())
                .usingRecursiveComparison()
                .ignoringFields("identifier")
                .isEqualTo(member1);
        assertThat(memberRepository.findMemberByID(member2.getId()).get())
                .usingRecursiveComparison()
                .ignoringFields("identifier")
                .isEqualTo(member2);
    }

    @DisplayName("멤버 이름으로 찾기")
    @Test
    public void findByName() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findMemberByName(member1.getName()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields("identifier")
                .isEqualTo(member1);
        assertThat(memberRepository.findMemberByName(member2.getName()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields("identifier")
                .isEqualTo(member2);
    }

    @DisplayName("이름이 중복되는 멤버 모두 찾기")
    @Test
    public void findAllByName() {
        // given
        Member member1 = createTestMember();
        String commonName = member1.getName();

        Member member2 = new Member.MemberBuilder()
                .id("AaK3619")
                .password("PwB1298!")
                .name(commonName)
                .build();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.findMemberByName(commonName))
                .usingRecursiveComparison()
                .ignoringFields("identifier")
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("멤버 저장")
    @Test
    public void save() {
        // given
        Member member = createTestMember();

        // when
        memberRepository.saveMember(member);

        // then
        assertThat(memberRepository.findMemberByName(member.getName()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields("identifier")
                .isEqualTo(member);
    }

    @DisplayName("멤버 ID로 제거하기")
    @Test
    public void removeByID() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        memberRepository.removeMemberByID(member1.getId());
        memberRepository.removeMemberByID(member2.getId());
        assertThat(memberRepository.findAllMembers()).isEmpty();
    }
}