package site.hixview.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.repository.MemberRepository;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.util.MemberTestUtils;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.domain.vo.name.EntityName.Member.IDENTIFIER;

@OnlyRealRepositoryContext
class MemberRepositoryImplTest implements MemberTestUtils {

    @Autowired
    private MemberRepository memberRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    MemberRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_MEMBERS_SCHEMA, true);
    }

    @DisplayName("회원들 획득")
    @Test
    void getMembersTest() {
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
    void getMemberByNameTest() {
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
    void getMembersByNameTest() {
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
    void getMemberByBirthdayTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMembersByBirthday(member1.getBirthday()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member1);

        assertThat(memberRepository.getMembersByBirthday(member2.getBirthday()).getFirst())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(member2);
    }

    @DisplayName("회원들 생일로 획득")
    @Test
    void getMembersByBirthdayTest() {
        // given
        Member member1 = testMember;
        LocalDate commonBirthday = member1.getBirthday();
        Member member2 = Member.builder().member(testNewMember).birthday(commonBirthday).build();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMembersByBirthday(commonBirthday))
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 이름과 생일로 획득")
    @Test
    void getMemberByNameAndBirthdayTest() {
        // given
        Member member = testMember;

        // when
        memberRepository.saveMember(member);

        // then
        assertThat(memberRepository.getMembersByNameAndBirthday(member.getName(), member.getBirthday()))
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(List.of(member));
    }

    @DisplayName("회원들 이름과 생일로 획득")
    @Test
    void getMembersByNameAndBirthdayTest() {
        // given
        Member member1 = testMember;
        String commonName = member1.getName();
        LocalDate commonBirthday = member1.getBirthday();
        Member member2 = Member.builder().member(testNewMember).name(commonName).birthday(commonBirthday).build();

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        assertThat(memberRepository.getMembersByBirthday(commonBirthday))
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER)
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 식별자로 획득")
    @Test
    void getMemberByIdentifierTest() {
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
    void getMemberByIDTest() {
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

    @DisplayName("비어 있는 회원 획득")
    @Test
    void getEmptyMemberTest() {
        // given & when
        Member member = Member.builder().member(testMember).identifier(1L).build();

        // then
        for (Optional<Member> emptyMember : List.of(
                memberRepository.getMemberByIdentifier(member.getIdentifier()),
                memberRepository.getMemberByID(member.getId()))) {
            assertThat(emptyMember).isEmpty();
        }
    }

    @DisplayName("회원 저장")
    @Test
    void saveMemberTest() {
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
    void saveMemberWithVariousPhoneNumber() {
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
    void removeMemberByIDTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

        // when
        memberRepository.saveMember(member1);
        memberRepository.saveMember(member2);

        // then
        memberRepository.deleteMemberById(member1.getId());
        memberRepository.deleteMemberById(member2.getId());
        assertThat(memberRepository.getMembers()).isEmpty();
    }
}