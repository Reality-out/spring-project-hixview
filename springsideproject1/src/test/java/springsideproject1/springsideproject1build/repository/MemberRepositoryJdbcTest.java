package springsideproject1.springsideproject1build.repository;

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
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryJdbcTest implements MemberTestUtility {

    @Autowired
    MemberRepository memberRepository;

    private final JdbcTemplate jdbcTemplateTest;
    private final String IDENTIFIER = "identifier";

    @Autowired
    public MemberRepositoryJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, memberTable, true);
    }

    @DisplayName("회원들 획득")
    @Test
    public void getMembers() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMembers())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 이름으로 획득")
    @Test
    public void getMemberByName() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMembersByName(member1.getName()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member1);
        assertThat(memberRepository.getMembersByName(member2.getName()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member2);
    }

    @DisplayName("회원들 이름으로 획득")
    @Test
    public void getMembersByName() {
        // given
        Member member1 = createTestMember();
        String commonName = member1.getName();
        Member member2 = Member.builder().member(createTestNewMember()).name(commonName).build();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMembersByName(commonName))
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 생일로 획득")
    @Test
    public void getMemberByBirth() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMembersByBirth(member1.getBirth()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member1);
        assertThat(memberRepository.getMembersByBirth(member2.getBirth()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member2);
    }

    @DisplayName("회원들 생일로 획득")
    @Test
    public void getMembersByBirth() {
        // given
        Member member1 = createTestMember();
        LocalDate commonBirth = member1.getBirth();
        Member member2 = Member.builder().member(createTestNewMember()).birth(commonBirth).build();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMembersByBirth(commonBirth))
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 이름과 생일로 획득")
    @Test
    public void getMemberByNameAndBirth() {
        // given
        Member member = createTestMember();

        // when
        memberRepository.saveMember(member);

        // then
        assertThat(memberRepository.getMembersByNameAndBirth(member.getName(), member.getBirth()))
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(List.of(member));
    }

    @DisplayName("회원들 이름과 생일로 획득")
    @Test
    public void getMembersByNameAndBirth() {
        // given
        Member member1 = createTestMember();
        String commonName = member1.getName();
        LocalDate commonBirth = member1.getBirth();
        Member member2 = Member.builder().member(createTestNewMember()).name(commonName).birth(commonBirth).build();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMembersByBirth(commonBirth))
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 식별자로 획득")
    @Test
    public void getMemberByIdentifier() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMemberByIdentifier(1L).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member1);
        assertThat(memberRepository.getMemberByIdentifier(2L).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member2);
    }

    @DisplayName("회원 ID로 획득")
    @Test
    public void getMemberByID() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMemberByID(member1.getId()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member1);
        assertThat(memberRepository.getMemberByID(member2.getId()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member2);
    }

    @DisplayName("회원 저장")
    @Test
    public void saveMember() {
        // given
        Member member = createTestMember();

        // when
        memberRepository.saveMember(member);

        // then
        assertThat(memberRepository.getMembersByName(member.getName()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member);
    }

    @DisplayName("회원 ID로 제거")
    @Test
    public void removeMemberByID() {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        memberRepository.deleteMember(member1.getId());
        memberRepository.deleteMember(member2.getId());
        assertThat(memberRepository.getMembers()).isEmpty();
    }
}