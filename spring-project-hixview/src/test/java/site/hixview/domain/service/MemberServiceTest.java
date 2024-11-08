package site.hixview.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.repository.MemberRepository;
import site.hixview.support.context.OnlyRealServiceContext;
import site.hixview.support.util.MemberTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_MEMBER_ID;
import static site.hixview.domain.vo.ExceptionMessage.NO_MEMBER_WITH_THAT_ID;

@OnlyRealServiceContext
class MemberServiceTest implements MemberTestUtils {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 가입")
    @Test
    void membership() {
        // given
        Member member = Member.builder().member(testMember).identifier(1L).build();
        when(memberRepository.getMembers()).thenReturn(List.of(member));
        when(memberRepository.getMemberByID(member.getId())).thenReturn(Optional.empty());
        when(memberRepository.saveMember(member)).thenReturn(1L);

        // when
        member = memberService.registerMember(member);

        // then
        assertThat(memberService.findMembers()).isEqualTo(List.of(member));
    }

    @DisplayName("회원 중복 ID로 가입")
    @Test
    void duplicatedMembershipWithSameIDTest() {
        // given
        Member member = testMember;
        String duplicatedId = member.getId();
        when(memberRepository.getMemberByID(duplicatedId))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(member));
        when(memberRepository.saveMember(member)).thenReturn(1L);
        memberService.registerMember(member);

        // when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.registerMember(member));

        // then
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_MEMBER_ID);
    }

    @DisplayName("회원 탈퇴")
    @Test
    void withdrawMembership() {
        // given
        Member member = testMember;
        String id = member.getId();
        when(memberRepository.getMembers()).thenReturn(Collections.emptyList());
        when(memberRepository.getMemberByID(id))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(member));
        when(memberRepository.saveMember(member)).thenReturn(1L);
        doNothing().when(memberRepository).deleteMemberById(id);
        memberService.registerMember(member);

        // when
        memberService.removeMemberById(id);

        // then
        assertThat(memberService.findMembers()).isEmpty();
    }

    @DisplayName("회원 존재하지 않는 ID로 제거")
    @Test
    void removeMemberByFaultIDTest() {
        // given
        when(memberRepository.getMemberByID(any())).thenReturn(Optional.empty());

        // when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.removeMemberById(INVALID_VALUE));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_MEMBER_WITH_THAT_ID);
    }
}