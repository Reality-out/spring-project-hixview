package site.hixview.domain.repository;

import site.hixview.domain.entity.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    /**
     * SELECT Member
     */
    List<Member> getMembers();

    List<Member> getMembersByName(String name);

    Optional<Member> getMemberByIdentifier(Long identifier);

    Optional<Member> getMemberByID(String id);

    Optional<Member> getMemberByIDAndPassword(String id, String password);

    Optional<Member> getMemberByEmail(String email);

    /**
     * INSERT Member
     */
    Long saveMember(Member member);

    /**
     * REMOVE Member
     */
    void deleteMemberById(String id);
}
