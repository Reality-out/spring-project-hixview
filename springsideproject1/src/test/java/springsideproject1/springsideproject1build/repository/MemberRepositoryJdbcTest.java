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
        assertThat(memberRepository.getMembers())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(List.of(member1, member2));
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
        assertThat(memberRepository.getMembersByName(member1.getName()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member1);
        assertThat(memberRepository.getMembersByName(member2.getName()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member2);
    }

    @DisplayName("이름이 중복되는 멤버 모두 찾기")
    @Test
    public void findAllByName() {
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

    @DisplayName("멤버 생일로 찾기")
    @Test
    public void findByBirth() {
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

    @DisplayName("생일이 중복되는 멤버 모두 찾기")
    @Test
    public void findAllByBirth() {
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

    @DisplayName("멤버 이름과 생일로 찾기")
    @Test
    public void findByNameAndBirth() {
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

    @DisplayName("이름과 생일이 중복되는 멤버 모두 찾기")
    @Test
    public void findAllByNameAndBirth() {
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
        assertThat(memberRepository.getMemberByIdentifier(1L).get())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member1);
        assertThat(memberRepository.getMemberByIdentifier(2L).get())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
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
        assertThat(memberRepository.getMemberByID(member1.getId()).get())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member1);
        assertThat(memberRepository.getMemberByID(member2.getId()).get())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member2);
    }

    @DisplayName("멤버 저장하기")
    @Test
    public void save() {
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
        memberRepository.deleteMember(member1.getId());
        memberRepository.deleteMember(member2.getId());
        assertThat(memberRepository.getMembers()).isEmpty();
    }
}