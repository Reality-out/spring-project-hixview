package springsideproject1.springsideproject1build.repository;

import springsideproject1.springsideproject1build.domain.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    /**
     * SELECT Member
     */
    List<Member> getMembers();

    List<Member> getMembersByName(String name);

    List<Member> getMembersByBirth(LocalDate birth);

    List<Member> getMembersByNameAndBirth(String name, LocalDate birth);

    Optional<Member> getMemberByIdentifier(Long identifier);

    Optional<Member> getMemberByID(String id);

    /**
     * INSERT Member
     */
    Long saveMember(Member member);

    /**
     * REMOVE Member
     */
    void deleteMember(String id);
}
