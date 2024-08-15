package springsideproject1.springsideproject1build.repository;

import springsideproject1.springsideproject1build.domain.member.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    /**
     * SELECT Member
     */
    List<Member> selectMembers();

    List<Member> selectMembersByName(String name);

    List<Member> selectMembersByBirth(LocalDate birth);

    List<Member> selectMembersByNameAndBirth(String name, LocalDate birth);

    Optional<Member> selectMemberByIdentifier(Long identifier);

    Optional<Member> selectMemberByID(String id);

    /**
     * INSERT Member
     */
    Long insertMember(Member member);

    /**
     * REMOVE Member
     */
    void deleteMember(String id);
}
