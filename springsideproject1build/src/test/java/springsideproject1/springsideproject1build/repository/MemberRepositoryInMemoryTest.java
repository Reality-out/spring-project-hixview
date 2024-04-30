package springsideproject1.springsideproject1build.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springsideproject1.springsideproject1build.domain.DatabaseHashMap;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.service.MemberService;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryInMemoryTest {
    MemberService memberService;
    MemberRepositoryInMemory memberRepositoryInMemory;

    @BeforeEach
    public void beforeEach() {
        memberRepositoryInMemory = new MemberRepositoryInMemory();
        memberService = new MemberService(memberRepositoryInMemory);
    }

    @AfterEach
    public void afterEach() {
        DatabaseHashMap.clearSettings();
    }

    @DisplayName("멤버 모두 가져오기")
    @Test
    public void findMembers() {
        // given
        Member member1 = new Member();
        Member member2 = new Member();

        // when
        memberRepositoryInMemory.saveMember(member1);
        memberRepositoryInMemory.saveMember(member2);

        // then
        assertThat(memberRepositoryInMemory.findAllMembers()).containsOnly(member1, member2);
    }

    @DisplayName("멤버 식별자로 찾기")
    @Test
    public void findByIdentifier() {
        // given
        Member member1 = new Member();
        Member member2 = new Member();

        // when
        memberRepositoryInMemory.saveMember(member1);
        memberRepositoryInMemory.saveMember(member2);

        // then
        assertThat(memberRepositoryInMemory.findMemberByIdentifier(Long.valueOf(1L)).get()).isEqualTo(member1);
        assertThat(memberRepositoryInMemory.findMemberByIdentifier(Long.valueOf(2L)).get()).isEqualTo(member2);
    }

    @DisplayName("멤버 ID로 찾기")
    @Test
    public void findByID() {
        // given
        Member member1 = new Member();
        member1.setId("Wehado!");

        Member member2 = new Member();
        member2.setId("Beautiful16");

        // when
        memberRepositoryInMemory.saveMember(member1);
        memberRepositoryInMemory.saveMember(member2);

        // then
        assertThat(memberRepositoryInMemory.findMemberByID("Wehado!").get()).isEqualTo(member1);
        assertThat(memberRepositoryInMemory.findMemberByID("Beautiful16").get()).isEqualTo(member2);
    }

    @DisplayName("멤버 이름으로 찾기")
    @Test
    public void findByName() {
        // given
        Member member1 = new Member();
        member1.setName("박준호");

        Member member2 = new Member();
        member2.setName("박하진");

        // when
        memberRepositoryInMemory.saveMember(member1);
        memberRepositoryInMemory.saveMember(member2);

        // then
        assertThat(memberRepositoryInMemory.findMemberByName("박준호").getFirst()).isEqualTo(member1);
        assertThat(memberRepositoryInMemory.findMemberByName("박하진").getFirst()).isEqualTo(member2);
    }

    @DisplayName("이름이 중복되는 멤버 모두 찾기")
    @Test
    public void findAllByName() {
        // given
        Member member1 = new Member();
        member1.setName("박준호");

        Member member2 = new Member();
        member2.setName("박준호");

        // when
        memberRepositoryInMemory.saveMember(member1);
        memberRepositoryInMemory.saveMember(member2);

        // then
        assertThat(memberRepositoryInMemory.findMemberByName("박준호")).containsOnly(member1, member2);
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
        memberRepositoryInMemory.saveMember(member);

        // then
        assertThat(memberRepositoryInMemory.findMemberByName(member.getName()).getFirst())
                .usingRecursiveComparison().isEqualTo(member);
    }

    @DisplayName("멤버 ID로 제거하기")
    @Test
    public void removeByID() {
        // given
        Member member1 = new Member();
        member1.setId("ParkWnsGur12");

        Member member2 = new Member();
        member2.setId("ParkWnsGur34");

        // when
        memberRepositoryInMemory.saveMember(member1);
        memberRepositoryInMemory.saveMember(member2);

        // then
        memberRepositoryInMemory.removeMemberByID("ParkWnsGur12");
        memberRepositoryInMemory.removeMemberByID("ParkWnsGur34");
        assertThat(memberRepositoryInMemory.findAllMembers()).isEmpty();
    }
}