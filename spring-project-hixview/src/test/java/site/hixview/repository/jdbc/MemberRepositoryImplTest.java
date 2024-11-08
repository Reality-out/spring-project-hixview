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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        member1 = Member.builder().member(member1).identifier(memberRepository.saveMember(member1)).build();
        member2 = Member.builder().member(member2).identifier(memberRepository.saveMember(member2)).build();

        // then
        assertThat(memberRepository.getMembers()).isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 이름으로 획득")
    @Test
    void getMemberByNameTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

        // when
        member1 = Member.builder().member(member1).identifier(memberRepository.saveMember(member1)).build();
        member2 = Member.builder().member(member2).identifier(memberRepository.saveMember(member2)).build();

        // then
        assertThat(memberRepository.getMembersByName(member1.getName()).getFirst()).isEqualTo(member1);
        assertThat(memberRepository.getMembersByName(member2.getName()).getFirst()).isEqualTo(member2);
    }

    @DisplayName("회원들 이름으로 획득")
    @Test
    void getMembersByNameTest() {
        // given
        Member member1 = testMember;
        String commonName = member1.getName();
        Member member2 = Member.builder().member(testNewMember).name(commonName).build();

        // when
        member1 = Member.builder().member(member1).identifier(memberRepository.saveMember(member1)).build();
        member2 = Member.builder().member(member2).identifier(memberRepository.saveMember(member2)).build();

        // then
        assertThat(memberRepository.getMembersByName(commonName)).isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 식별자로 획득")
    @Test
    void getMemberByIdentifierTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

        // when
        member1 = Member.builder().member(member1).identifier(memberRepository.saveMember(member1)).build();
        member2 = Member.builder().member(member2).identifier(memberRepository.saveMember(member2)).build();

        // then
        assertThat(memberRepository.getMemberByIdentifier(1L).orElseThrow()).isEqualTo(member1);
        assertThat(memberRepository.getMemberByIdentifier(2L).orElseThrow()).isEqualTo(member2);
    }

    @DisplayName("회원 ID로 획득")
    @Test
    void getMemberByIDTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

        // when
        member1 = Member.builder().member(member1).identifier(memberRepository.saveMember(member1)).build();
        member2 = Member.builder().member(member2).identifier(memberRepository.saveMember(member2)).build();

        // then
        assertThat(memberRepository.getMemberByID(member1.getId()).orElseThrow()).isEqualTo(member1);
        assertThat(memberRepository.getMemberByID(member2.getId()).orElseThrow()).isEqualTo(member2);
    }

    @DisplayName("회원 ID와 비밀번호로 획득")
    @Test
    void getMemberByIDAndPasswordTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

        // when
        member1 = Member.builder().member(member1).identifier(memberRepository.saveMember(member1)).build();
        member2 = Member.builder().member(member2).identifier(memberRepository.saveMember(member2)).build();

        // then
        assertThat(memberRepository.getMemberByIDAndPassword(member1.getId(), member1.getPassword()).orElseThrow()).isEqualTo(member1);
        assertThat(memberRepository.getMemberByIDAndPassword(member2.getId(), member2.getPassword()).orElseThrow()).isEqualTo(member2);
    }

    @DisplayName("회원 이메일로 획득")
    @Test
    void getMemberByEmailTest() {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

        // when
        member1 = Member.builder().member(member1).identifier(memberRepository.saveMember(member1)).build();
        member2 = Member.builder().member(member2).identifier(memberRepository.saveMember(member2)).build();

        // then
        assertThat(memberRepository.getMemberByEmail(member1.getEmail()).orElseThrow()).isEqualTo(member1);
        assertThat(memberRepository.getMemberByEmail(member2.getEmail()).orElseThrow()).isEqualTo(member2);
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
        member = Member.builder().member(member).identifier(memberRepository.saveMember(member)).build();

        // then
        assertThat(memberRepository.getMembersByName(member.getName()).getFirst()).isEqualTo(member);
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