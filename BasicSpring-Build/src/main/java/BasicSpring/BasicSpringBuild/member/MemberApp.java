package BasicSpring.BasicSpringBuild.member;

public class MemberApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member memberA = new Member(1L, "memberA", Grade.VIP);
        memberService.joinMember(memberA);

        Member findMember = memberService.findMember(1L);
        System.out.println("new memberA = " + memberA.getName());
        System.out.println("findMember = " + findMember.getName());
    }
}
