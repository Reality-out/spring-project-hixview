package springsideproject1.springsideproject1production.repository;

import springsideproject1.springsideproject1production.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    /**
     * SELECT Member
     */
    List<Member> findAllMembers();

    Optional<Member> findMemberByIdentifier(Long identifier);

    Optional<Member> findMemberByID(String id);

    List<Member> findMemberByName(String name);

    /**
     * INSERT Member
     */
    void saveMember(Member member);

    /**
     * REMOVE Member
     */
    void removeMemberByID(String id);
}
