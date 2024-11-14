package site.hixview.aggregate.repository;

import site.hixview.aggregate.domain.SiteMember;

import java.util.List;
import java.util.Optional;

public interface SiteMemberRepository {
    /**
     * SELECT SiteMember
     */
    List<SiteMember> getMembers();

    List<SiteMember> getMembersByName(String name);

    Optional<SiteMember> getMemberByNumber(Long number);

    Optional<SiteMember> getMemberByID(String id);

    Optional<SiteMember> getMemberByIDAndPassword(String id, String password);

    Optional<SiteMember> getMemberByEmail(String email);

    /**
     * INSERT SiteMember
     */
    Long saveMember(SiteMember member);

    /**
     * REMOVE SiteMember
     */
    void deleteMemberByNumber(Long number);
}
