package BasicSpring.BasicSpringBuild.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberAppTest {

    MemberService memberService = new MemberServiceImpl();

    @Test
    public void join() {
        // given
        Member member = new Member(1L, "memberA", Grade.VIP);

        // when
        memberService.joinMember(member);
        Member findMember = memberService.findMember(1L);

        // then
        assertThat(member).isEqualTo(findMember);
    }
}