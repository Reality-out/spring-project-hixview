package BasicSpring.BasicSpringBuild.member;

public interface MemberService {
    void joinMember(Member member);

    Member findMember(Long memberId);
}
