package springsideproject1.springsideproject1build.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springsideproject1.springsideproject1build.database.MemberDatabase;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.repository.MemberRepositoryInMemory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static springsideproject1.springsideproject1build.database.DatabaseUtils.clearMemberDatabase;

class MemberServiceInMemoryTest {
    MemberRepositoryInMemory memberRepositoryInMemory;
    MemberService memberService;

    @BeforeEach
    public void beforeEach() {
        memberRepositoryInMemory = new MemberRepositoryInMemory();
        memberService = new MemberService(memberRepositoryInMemory);
    }

    @AfterEach
    public void afterEach() {
        clearMemberDatabase();
    }

    @DisplayName("회원 가입 테스트")
    @Test
    public void membership() {
        // given
        Member member = new Member();
        member.setId("hunter10");
        member.setPassword("newPassword!");
        member.setName("배주민");

        // when
        memberService.joinMember(member);

        // then
        assertThat(memberService.findOneMemberByID("hunter10").get()).isEqualTo(member);
        assertThat(memberService.findOneMemberByID("noneID")).isEmpty();
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