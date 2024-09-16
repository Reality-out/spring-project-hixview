package springsideproject1.springsideproject1build.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.member.Member;
import springsideproject1.springsideproject1build.domain.repository.MemberRepository;
import springsideproject1.springsideproject1build.util.test.MemberTestUtils;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springsideproject1.springsideproject1build.domain.vo.SchemaName.TEST_MEMBERS_SCHEMA;

@SpringBootTest
@Transactional
class MemberRepositoryImplTest implements MemberTestUtils {

    @Autowired
    MemberRepository memberRepository;

    private final JdbcTemplate jdbcTemplateTest;
    private final String IDENTIFIER = "identifier";

    @Autowired
    public MemberRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_MEMBERS_SCHEMA, true);
    }

    @DisplayName("회원들 획득")
    @Test
    public void getMembersTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

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
    public void getMemberByNameTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

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
    public void getMembersByNameTest() {
        // given
        Member member1 = testMember;
        String commonName = member1.getName();
        Member member2 = Member.builder().member(testNewMember).name(commonName).build();

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
    public void getMemberByBirthTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

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
    public void getMembersByBirthTest() {
        // given
        Member member1 = testMember;
        LocalDate commonBirth = member1.getBirth();
        Member member2 = Member.builder().member(testNewMember).birth(commonBirth).build();

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
    public void getMemberByNameAndBirthTest() {
        // given
        Member member = testMember;

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
    public void getMembersByNameAndBirthTest() {
        // given
        Member member1 = testMember;
        String commonName = member1.getName();
        LocalDate commonBirth = member1.getBirth();
        Member member2 = Member.builder().member(testNewMember).name(commonName).birth(commonBirth).build();

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
    public void getMemberByIdentifierTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

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
    public void getMemberByIDTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

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
    public void saveMemberTest() {
        // given
        Member member = testMember;

        // when
        memberRepository.saveMember(member);

        // then
        assertThat(memberRepository.getMembersByName(member.getName()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member);
    }

    @DisplayName("대시가 있는 버전과 없는 버전의 휴대폰 번호를 사용하는 회원 저장")
    @Test
    public void saveMemberWithVariousPhoneNumber() {
        // given
        Member member1 = testMember;
        Member member2 = Member.builder().member(testNewMember).phoneNumber("01023456789").build();

        // when
        member1 = Member.builder().member(member1).identifier(memberRepository.saveMember(member1)).build();
        member2 = Member.builder().member(member2).identifier(memberRepository.saveMember(member2)).build();

        // then
        assertThat(memberRepository.getMemberByID(member1.getId()).orElseThrow()).usingRecursiveComparison().isEqualTo(member1);
        assertThat(memberRepository.getMemberByID(member2.getId()).orElseThrow()).usingRecursiveComparison().isEqualTo(member2);
    }

    @DisplayName("회원 ID로 제거")
    @Test
    public void removeMemberByIDTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        memberRepository.deleteMember(member1.getId());
        memberRepository.deleteMember(member2.getId());
        assertThat(memberRepository.getMembers()).isEmpty();
    }
}